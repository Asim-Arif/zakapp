package com.example.asim.rest_man;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class splash_screen extends Activity {

    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 1000;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.splash_screen);
        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                //Intent mainIntent = new Intent(splash_screen.this,MainActivity.class);
                Intent mainIntent = new Intent(splash_screen.this,pos.class);
                splash_screen.this.startActivity(mainIntent);
                splash_screen.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}