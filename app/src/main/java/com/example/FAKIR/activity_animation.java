package com.example.FAKIR;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class activity_animation  extends AppCompatActivity {

    private static int  SPLASH_SCREEN = 1650;

    // varibles
    Animation topanim,bottomanim;
    ImageView image;
    TextView logo,slogan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_animation);

        //Animation
        topanim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomanim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
        // Hooks
        image=findViewById(R.id.imageView);
        logo=findViewById(R.id.textView1);
        slogan=findViewById(R.id.textView3);

        image.setAnimation(topanim);
        logo.setAnimation(bottomanim);
        slogan.setAnimation(bottomanim);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                activity_animation.this.startActivity(new Intent(activity_animation.this,activity_login .class));
                activity_animation.this.finish();
            }
        } ,SPLASH_SCREEN);
    }
}


