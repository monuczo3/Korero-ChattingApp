package com.monu.korero;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.monu.korero.Models.Users;
import com.monu.korero.databinding.ActivitySignUpBinding;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding binding;
    private FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        dialog = new ProgressDialog(SignUpActivity.this);
        dialog.setTitle("Loading...");
        dialog.setMessage("Your Account is getting created.");


        binding.tvAlreadyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movingToSignIn();
            }
        });


        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        binding.btnSignup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.show();
                mAuth.createUserWithEmailAndPassword(binding.etEmail.getText().toString(),binding.etPassword.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if(task.isSuccessful()){
                                    Users users = new Users(binding.etUserName.getText().toString(),
                                            binding.etEmail.getText().toString(),
                                            binding.etPassword.getText().toString());
                                    String id = task.getResult().getUser().getUid();
                                    firebaseDatabase.getReference().child("Users").child(id).setValue(users);
                                    dialog.dismiss();
                                    Toast.makeText(SignUpActivity.this, "User SignUp Successfully", Toast.LENGTH_SHORT).show();
                                }
                                else{

                                    Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

    }

    private void movingToSignIn() {
        Intent intent = new Intent(SignUpActivity.this,SignIn.class);
        startActivity(intent);
    }
}