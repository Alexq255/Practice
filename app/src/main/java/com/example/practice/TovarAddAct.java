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
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import java.util.Locale;

public class TovarAddAct extends AppCompatActivity {
    private EditText editNazvanie,editDescriprion,editPrice,editWarranty,editCategory;
    private DatabaseReference mBase;
    private String GROUPKEY;
    private FirebaseAuth mAuth;
    private TextView twAccount;
    private Button saveAll,chooseImg;
    private ImageView imgViewAdd;
    private Uri uploadUri,uploadUri2,uploadUri3;
    private StorageReference mStorageRef;
    private CheckBox CheckerAsic,CheckerProducts,CheckerSevices;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tovar_add);
        init();


    }
    private void init(){
        editNazvanie = findViewById(R.id.editNazvanie);
        CheckerAsic = findViewById(R.id.CheckerAsic);
        CheckerProducts = findViewById(R.id.CheckerProducts);
        CheckerSevices = findViewById(R.id.CheckerServices);
        editDescriprion = findViewById(R.id.editDescription);
        editPrice = findViewById(R.id.editPrice);
        editWarranty = findViewById(R.id.editWarranty);
        editCategory = findViewById(R.id.editCategory);

        twAccount = findViewById(R.id.twAccount);
        imgViewAdd = findViewById(R.id.imgViewAdd);
        mStorageRef = FirebaseStorage.getInstance().getReference("Image_db");
        CheckCategory();
    }
    private void CheckCategory(){

        CheckerAsic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (CheckerAsic.isChecked()){
                    // SwitchNumber = 0;
                    GROUPKEY = "Asic";
                    mBase = FirebaseDatabase.getInstance().getReference(GROUPKEY);
                    //asic

                }else{


                }
            }
        });
        CheckerProducts.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (CheckerProducts.isChecked()){
                    // SwitchNumber = 1;
                    GROUPKEY = "Products";
                    mBase = FirebaseDatabase.getInstance().getReference(GROUPKEY);
                    //Products

                }else{


                }
            }
        });
        CheckerSevices.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (CheckerSevices.isChecked()){
                    //SwitchNumber = 2;
                    GROUPKEY = "Services";
                    mBase = FirebaseDatabase.getInstance().getReference(GROUPKEY);
                    //Services

                }else{


                }
            }
        });

    }




    private void saveData()
    {
        String id = mBase.push().getKey();
        String nazvanie = editNazvanie.getText().toString();
        String description = editDescriprion.getText().toString();
        String fullPrice = editPrice.getText().toString();
        String warranty = editWarranty.getText().toString();
        String Category = editCategory.getText().toString();

        TovarAddClass newTovarAdd = new TovarAddClass(id,nazvanie,description,fullPrice,uploadUri.toString(),warranty,Category,uploadUri2.toString(),uploadUri3.toString());
        if (!TextUtils.isEmpty(nazvanie)&&!TextUtils.isEmpty(description)&&!TextUtils.isEmpty(fullPrice)&&!TextUtils.isEmpty(warranty)
        &&!TextUtils.isEmpty(Category)){
            if (id != null)mBase.child(id).setValue(newTovarAdd);


        }else{
            Toast.makeText(TovarAddAct.this,"???????????????? ?????????????????? ???????? ????????????!",Toast.LENGTH_SHORT).show();
        }
    }

    public void SaveDate(View view) {

        saveData();
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
                if(uploadUri == null){
                    uploadUri = task.getResult();
                    Toast.makeText(TovarAddAct.this,"?????????????????? ???????????? ????????????????!!",Toast.LENGTH_SHORT).show();
                }
                else if(uploadUri2 == null) {
                    uploadUri2 = task.getResult();
                    Toast.makeText(TovarAddAct.this,"?????????????????? ???????????? ????????????????!!",Toast.LENGTH_SHORT).show();
                }
                else if(uploadUri3 == null){
                    uploadUri3 = task.getResult();
                    Toast.makeText(TovarAddAct.this,"?????????????????? ???????????? ????????????????!!",Toast.LENGTH_SHORT).show();
                }

            }


        });

    }

    public void goBack(View view) {
        Intent intent = new Intent(TovarAddAct.this, Glavnaya.class);
        startActivity(intent);
    }

    public void SaveIMG(View view) {
        uploadImage();
    }
}


