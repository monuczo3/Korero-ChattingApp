package com.monu.korero.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.monu.korero.fragments.CallsFragment;
import com.monu.korero.fragments.ChatsFragment;
import com.monu.korero.fragments.StatusFragment;

public class viewPagerAdapter extends FragmentPagerAdapter {


    public viewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0: return new ChatsFragment();
            case 1: return new StatusFragment();
            case 2: return new CallsFragment();
            default: return new ChatsFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if(position == 0){
            title = "CHATS";
        }
        else if(position == 1){
            title = "STATUS";
        }
        else if(position == 2){
            title = "CALLS";
        }
        return title;
    }
}
