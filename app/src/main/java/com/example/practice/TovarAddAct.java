package com.example.practice;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class TovarAddAct extends AppCompatActivity {
    private EditText editNazvanie,editDescriprion,editPrice;
    private DatabaseReference mBase;
    private String GROUPKEY = "Tovar";
    private FirebaseAuth mAuth;
    private TextView twAccount;
    private Button saveAll,chooseImg;
    private ImageView imgViewAdd;
    private Uri uploadUri;
    private StorageReference mStorageRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tovar_add);
        init();
    }
    private void init(){
        editNazvanie = findViewById(R.id.editNazvanie);
        editDescriprion = findViewById(R.id.editDescription);
        editPrice = findViewById(R.id.editPrice);
        mBase = FirebaseDatabase.getInstance().getReference(GROUPKEY);
        twAccount = findViewById(R.id.twAccount);
        imgViewAdd = findViewById(R.id.imgViewAdd);
        mStorageRef = FirebaseStorage.getInstance().getReference("Image_db");
    }


    private void saveData()
    {
        String id = mBase.push().getKey();
        String nazvanie = editNazvanie.getText().toString();
        String description = editDescriprion.getText().toString();
        String fullPrice = editPrice.getText().toString();
        TovarAddClass newTovarAdd = new TovarAddClass(id,nazvanie,description,fullPrice,uploadUri.toString());
        if (!TextUtils.isEmpty(nazvanie)&&!TextUtils.isEmpty(description)&&!TextUtils.isEmpty(fullPrice)){
            if (id != null)mBase.child(id).setValue(newTovarAdd);


        }else{
            Toast.makeText(TovarAddAct.this,"Возможно некоторые поля пустые!",Toast.LENGTH_SHORT).show();
        }
    }

    public void SaveDate(View view) {

        uploadImage();
    }

    public void ChooseIMG(View view) {
        Intent intentChooser = new Intent();
        intentChooser.setType("image/*");
        intentChooser.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intentChooser,1);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1&&data !=null && data.getData () != null);
        {
            if (resultCode == RESULT_OK)
            {
                Log.d("Mylog","Image URI : "+ data.getData());
                imgViewAdd.setImageURI(data.getData());

            }
        }
    }
    private void uploadImage() {
        Bitmap bitmap = ((BitmapDrawable) imgViewAdd.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] byteArray = baos.toByteArray();
        StorageReference mRef = mStorageRef.child(System.currentTimeMillis() + "MyImg");
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
                saveData();

            }


        });

    }

    public void goBack(View view) {
        Intent intent = new Intent(TovarAddAct.this, Glavnaya.class);
        startActivity(intent);
    }
}