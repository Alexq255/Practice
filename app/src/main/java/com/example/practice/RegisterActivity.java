package com.example.practice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
private EditText email_Register;
private EditText password_Register;
private Button btn_register;
private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        email_Register = findViewById(R.id.email_register);
        password_Register = findViewById(R.id.password_register);
        btn_register = findViewById(R.id.btn_register);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email_Register.getText().toString().isEmpty()|| password_Register.getText().toString().isEmpty()){
                    Toast.makeText(RegisterActivity.this,"Поля пустые",Toast.LENGTH_SHORT).show();
            }else{
                    mAuth.createUserWithEmailAndPassword(email_Register.getText().toString(), password_Register.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        Intent intent = new Intent(RegisterActivity.this, Glavnaya.class);
                                        startActivity(intent);
                                    }else{
                                        Toast.makeText(RegisterActivity.this,"Ошибка",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }

            }
        });
    }
}