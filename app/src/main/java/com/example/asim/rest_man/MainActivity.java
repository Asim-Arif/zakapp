package com.example.asim.rest_man;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button btnReceiving=(Button) findViewById(R.id.cmdReceiving);


        Button btnAuthorization=(Button) findViewById(R.id.cmdAuthorization);


        Button btnReceivingList=(Button) findViewById(R.id.cmdReceivingList);

        ImageButton cmdSettings = (ImageButton) findViewById(R.id.ImgBtnSettings);
        cmdSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), sql_settings.class);
                startActivity(i);
            }
        });
    }
}
