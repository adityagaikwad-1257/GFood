package com.adi.gfood.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.adi.gfood.fragments.onboard.FirstFragment;
import com.adi.gfood.fragments.onboard.SecondFragment;
import com.adi.gfood.fragments.onboard.ThirdFragment;

public class OnBoardAdapter extends FragmentStateAdapter {

    public OnBoardAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment;

        switch (position){
            case 0:
                fragment = new FirstFragment();
                break;
            case 1:
                fragment = new SecondFragment();
                break;
            case 2:
                fragment = new ThirdFragment();
                break;
            default:
                fragment = null;
        }

        return fragment;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
