package com.example.practice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ShowDataActivity extends AppCompatActivity {
private TextView twName,twSecname,twMail;
private ImageView imgBD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data);
        init();
        getIntentMain();

    }
    private void init(){
        twName = findViewById(R.id.twName);
        twSecname = findViewById(R.id.twdescription);
        twMail = findViewById(R.id.twNazavanie);
        imgBD = findViewById(R.id.imgTovar);


    }
    private void getIntentMain(){
        Intent i = getIntent();
        if (i!=null){
            Picasso.get().load(i.getStringExtra("user_image_id")).into(imgBD);
            twMail.setText(i.getStringExtra("user_name"));
            twSecname.setText(i.getStringExtra("user_secname"));
            twName.setText(i.getStringExtra("user_email"));

        }
    }

    public void Send(View view) {
        Uri number = Uri.parse("tel:88005553535");
        Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
        startActivity(callIntent);



    }

    public void goBack(View view) {
        Intent intent = new Intent(ShowDataActivity.this, ReadActivity.class);
        startActivity(intent);
    }
}