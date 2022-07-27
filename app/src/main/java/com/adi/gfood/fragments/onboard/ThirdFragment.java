package com.adi.gfood.fragments.onboard;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.adi.gfood.R;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class ThirdFragment extends Fragment {

    public ThirdFragment() {
        super(R.layout.fragment_onboard_three);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        YoYo.with(Techniques.Bounce)
                .repeat(100)
                .playOn(view.findViewById(R.id.ontv));
        super.onViewCreated(view, savedInstanceState);
    }
}
