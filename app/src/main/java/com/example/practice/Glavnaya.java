package com.example.practice;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

public class Glavnaya extends AppCompatActivity {

    private EditText EditName,EditSurname,EditScam;
    private DatabaseReference mBase;
    private String USERKEY = "User";
    private ImageView loggout_btn;
    private FirebaseAuth mAuth;
    private TextView twAccount;
    private Button btnChoose;
    private ImageView imageView;
    private Uri uploadUri;
    private final int PICK_IMAGE_REQUEST = 71;
    private StorageReference mStorageRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glavnaya);
        EditName = findViewById(R.id.EditName);
        EditSurname = findViewById(R.id.EditSurname);
        EditScam = findViewById(R.id.EditScam);
        mBase = FirebaseDatabase.getInstance().getReference(USERKEY);
        loggout_btn = findViewById(R.id.loggout_btn);
        twAccount = findViewById(R.id.twAccount);
        btnChoose = findViewById(R.id.btnChoose);
        imageView = findViewById(R.id.imgView);
        mStorageRef = FirebaseStorage.getInstance().getReference("Image_db");
    }
    private void saveUser()
    {
        String id = mBase.push().getKey();
        String name = EditName.getText().toString();
        String secname = EditSurname.getText().toString();
        String email = EditScam.getText().toString();
        UserAdd newUserAdd = new UserAdd(id,name,secname,email,uploadUri.toString());
        if (!TextUtils.isEmpty(name)&&!TextUtils.isEmpty(secname)&&!TextUtils.isEmpty(email)){
            if (id != null)mBase.child(id).setValue(newUserAdd);


        }else{
            Toast.makeText(Glavnaya.this,"Возможно некоторые поля пустые!",Toast.LENGTH_SHORT).show();
        }
    }
    public void onClickSave(View view) {

       uploadImage();
    }

    public void onClickRead(View view) {
        Intent Intent = new Intent(Glavnaya.this, ReadActivity.class);
        startActivity(Intent);


    }

    public void Logout(View view) {
        mAuth.signOut();
        Intent Intent = new Intent(Glavnaya.this, MainActivity.class);
        startActivity(Intent);
    }

    public void btnChoose(View view) {
        chooseImage();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1&&data !=null && data.getData () != null);
        {
            if (resultCode == RESULT_OK)
            {
                Log.d("Mylog","Image URI : "+ data.getData());
                imageView.setImageURI(data.getData());

            }
        }
    }

    private void chooseImage()
    {
        Intent intentChooser = new Intent();
        intentChooser.setType("image/*");
        intentChooser.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intentChooser,1);
    }
    private void uploadImage()
    {
        Bitmap bitmap =((BitmapDrawable)imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] byteArray= baos.toByteArray();
        StorageReference mRef = mStorageRef.child(System.currentTimeMillis()+"MyImg");
        UploadTask up = mRef.putBytes(byteArray);
        Task<Uri> task = up.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                return mRef.getDownloadUrl();

            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                uploadUri = task.getResult();
                saveUser();

            }


        });










    }


    public void onClickTovarka(View view) {
        Intent Intent = new Intent(Glavnaya.this, TovarkaActivity.class);
        startActivity(Intent);
    }

    public void AddTovar(View view) {
        Intent Intent = new Intent(Glavnaya.this, TovarAddAct.class);
        startActivity(Intent);
    }
}