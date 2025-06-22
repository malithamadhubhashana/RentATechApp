package com.s23010168.myproject.ui.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.s23010168.myproject.R;
import com.s23010168.myproject.ui.auth.LoginActivity;
import com.s23010168.myproject.ui.auth.SignupActivity;

public class WelcomeActivity extends AppCompatActivity {

    private ImageView ivLogo;
    private TextView tvAppName, tvTagline, tvSubtitle;
    private MaterialButton btnSignUp, btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // Hide action bar for full screen welcome
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        initViews();
        setupAnimations();
        setupClickListeners();
    }

    private void initViews() {
        ivLogo = findViewById(R.id.iv_logo);
        tvAppName = findViewById(R.id.tv_app_name);
        tvTagline = findViewById(R.id.tv_tagline);
        tvSubtitle = findViewById(R.id.tv_subtitle);
        btnSignUp = findViewById(R.id.btn_sign_up);
        btnLogin = findViewById(R.id.btn_login);
    }

    private void setupAnimations() {
        // Fade in animations
        Animation fadeInUp = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        fadeInUp.setDuration(1000);

        // Staggered animations
        ivLogo.startAnimation(fadeInUp);
        
        Animation fadeInUpDelayed1 = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        fadeInUpDelayed1.setDuration(1000);
        fadeInUpDelayed1.setStartOffset(300);
        tvAppName.startAnimation(fadeInUpDelayed1);
        
        Animation fadeInUpDelayed2 = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        fadeInUpDelayed2.setDuration(1000);
        fadeInUpDelayed2.setStartOffset(600);
        tvTagline.startAnimation(fadeInUpDelayed2);
        
        Animation fadeInUpDelayed3 = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        fadeInUpDelayed3.setDuration(1000);
        fadeInUpDelayed3.setStartOffset(900);
        tvSubtitle.startAnimation(fadeInUpDelayed3);
        
        Animation fadeInUpDelayed4 = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        fadeInUpDelayed4.setDuration(1000);
        fadeInUpDelayed4.setStartOffset(1200);
        btnSignUp.startAnimation(fadeInUpDelayed4);
        btnLogin.startAnimation(fadeInUpDelayed4);
    }

    private void setupClickListeners() {
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, SignupActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Prevent going back to splash screen
        moveTaskToBack(true);
    }
} 