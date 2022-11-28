package com.example.practice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Glavnaya extends AppCompatActivity {

private EditText EditName,EditSurname,EditScam;
private DatabaseReference mBase;
private String USERKEY = "User";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glavnaya);
        EditName = findViewById(R.id.EditName);
        EditSurname = findViewById(R.id.EditSurname);
        EditScam = findViewById(R.id.EditScam);
        mBase = FirebaseDatabase.getInstance().getReference(USERKEY);
    }

    public void onClickSave(View view) {
       String id = mBase.getKey();
       String name = EditName.getText().toString();
       String secname = EditSurname.getText().toString();
       String email = EditScam.getText().toString();
       UserAdd newUserAdd = new UserAdd(id,name,secname,email);
       mBase.push().setValue(newUserAdd);
    }

    public void onClickRead(View view) {

    }
}