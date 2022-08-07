package com.eamatracker.HelpCenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.eamatracker.R;
import com.google.android.material.appbar.MaterialToolbar;

public class HelpCenterActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_center);


        // Define views
        LottieAnimationView helpAnim = findViewById(R.id.helpAnim);
        MaterialToolbar helpToolbar = findViewById(R.id.helpToolbar);

        // Customer service Lottie anim
        helpAnim.setAnimation(R.raw.customer_service);


        // Back
        helpToolbar.setNavigationOnClickListener(v -> supportFinishAfterTransition());

    }

    public void openLiveChat(View view) {

        // Start live chat actity
        startActivity(new Intent(this, LiveChatActivity.class));
    }
}