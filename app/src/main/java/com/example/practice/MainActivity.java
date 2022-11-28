package com.example.practice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private EditText email_login;
    private EditText password_login;
    private Button loginbtn;
    private TextView textView3;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email_login = findViewById(R.id.email_login);
        password_login = findViewById(R.id.password_login);
        loginbtn = findViewById(R.id.loginbtn);
        textView3 = findViewById(R.id.textView3);
        mAuth = FirebaseAuth.getInstance();
        textView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(Intent);
            }
        });
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email_login.getText().toString().isEmpty()|| password_login.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Поля не могут быть пусты",Toast.LENGTH_SHORT).show();
                }else{
                    mAuth.signInWithEmailAndPassword(email_login.getText().toString(),password_login.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        Intent Intent = new Intent(MainActivity.this, Glavnaya.class);
                                        startActivity(Intent);
                                    }else{
                                        Toast.makeText(MainActivity.this, "Вась счетчик опломбировали войди на тп рф ",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }

            }
        });
    }
}