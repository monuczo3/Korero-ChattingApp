package com.monu.korero;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.monu.korero.Adapters.chatAdapter;
import com.monu.korero.Models.messageModel;
import com.monu.korero.databinding.ActivityChatDetailBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class ChatDetailActivity extends AppCompatActivity {
    ActivityChatDetailBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        final String senderId = auth.getUid();
        String receiverId = getIntent().getStringExtra("UserId");
        String userName = getIntent().getStringExtra("UserName");
        String ProfilePic = getIntent().getStringExtra("profilePic");

        binding.userName.setText(userName);
        Picasso.get().load(ProfilePic).placeholder(R.drawable.default_person_icon).into(binding.profileImage);

        binding.imgBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatDetailActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        final ArrayList<messageModel> models = new ArrayList<>();

        final chatAdapter chatAdapter = new chatAdapter(models,this, receiverId);
        binding.chatRecyclerView.setAdapter(chatAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
       // scroll to bottom on new message

        binding.chatRecyclerView.setLayoutManager(layoutManager);


        String senderRoom = senderId+receiverId;
        String receiverRoom = receiverId+senderId;

        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.textsend.getText().toString().isEmpty()){
                    binding.textsend.setError("Enter a message");
                    return;
                }
                String message = binding.textsend.getText().toString();
                final messageModel model = new messageModel(senderId,message);
                model.setTimestamp(new Date().getTime());
                binding.textsend.setText("");

                database.getReference().child("chats").child(senderRoom).push()
                        .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        database.getReference().child("chats").child(receiverRoom).push()
                                .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        });
                    }
                });
            }
        });

        database.getReference().child("chats")
                .child(senderRoom)
                .addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        models.clear();
                        for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                            messageModel model = dataSnapshot.getValue(messageModel.class);
                            model.setMessageId(dataSnapshot.getKey());
                            models.add(model);
                        }
                        chatAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}