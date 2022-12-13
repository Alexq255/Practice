package com.example.practice;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.security.identity.IdentityCredentialStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;

public class ChangeActivity extends AppCompatActivity {
    private EditText twnazvanie2,twdescription2,twfullprice2,ShopCounter2,twCategory2,twWarranty2,counter2;
    private ImageView imgTovar2;
    private String SHOPCART = "Asic",ids;
    private DatabaseReference mBase;
    private DatabaseReference Cbbase;
    private FirebaseAuth mAuth;
    private Uri uploadUri;
    private StorageReference mStorageRef;
    private ListView CountView;
    private ArrayAdapter<String> adapter;
    private List<String> listdata;
    private List<Korzina> listTemp;
    private TextView idZap;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change);
        init();
        getIntentMain();
        getDataFromDB();





    }


    private void init(){

        twnazvanie2 = findViewById(R.id.twNazavanie2);
        twdescription2 = findViewById(R.id.twdescription2);

        twfullprice2 = findViewById(R.id.twfullprice2);
        twCategory2 = findViewById(R.id.twCategory2);
        twWarranty2 = findViewById(R.id.twWarranty2);
        idZap = findViewById(R.id.idZap);

        imgTovar2 = findViewById(R.id.imgTovar2);
        mBase = FirebaseDatabase.getInstance().getReference(SHOPCART);
        mStorageRef = FirebaseStorage.getInstance().getReference("Image_db");
        Cbbase = FirebaseDatabase.getInstance().getReference();




    }
    private void getIntentMain(){
        Intent is = getIntent();
        if (is!=null){
            Picasso.get().load(is.getStringExtra("tovar_imgTovar")).into(imgTovar2);
            twnazvanie2.setText(is.getStringExtra("tovar_nazvanie"));
            twdescription2.setText(is.getStringExtra("tovar_description"));
            twfullprice2.setText(is.getStringExtra("tovar_fullprice"));
            twWarranty2.setText(is.getStringExtra("tovar_warranty"));
            twCategory2.setText(is.getStringExtra("tovar_Category"));
            idZap.setText(is.getStringExtra("tovar_id"));


        }

    }

    public void toList(View view) {
        Intent intent = new Intent(ChangeActivity.this, TovarkaActivity.class);
        startActivity(intent);
    }

    public void OrderTo(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ChangeActivity.this);
        builder.setTitle("Добавление в корзину")
                .setCancelable(false)
                .setIcon(R.drawable.cart)
                .setMessage("Добавить товар в корзину?")
                .setPositiveButton("OK",

                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int id) {
                                uploadImage();
                                Toast.makeText(ChangeActivity.this,"Выбранный товар успешно добавлен",Toast.LENGTH_SHORT).show();

                            }

                        })
                .setNegativeButton("Отмена",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int id) {

                            }
                        });

        AlertDialog alert =builder.create();
        alert.show();
    }

    private void getDataFromDB(){
        ValueEventListener vListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {



            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        };
        mBase.addValueEventListener(vListener);
    }





    private void saveData()
    {
        String id = idZap.getText().toString();
        String nazvanie = twnazvanie2.getText().toString();
        String description = twdescription2.getText().toString();
        String fullPrice = twfullprice2.getText().toString();
        String warranty = twWarranty2.getText().toString();
        String category = twCategory2.getText().toString();
        TovarAddClass Cart = new TovarAddClass(id,nazvanie,description,fullPrice,uploadUri.toString(),warranty,category);
        if (!TextUtils.isEmpty(nazvanie)&&!TextUtils.isEmpty(description)&&!TextUtils.isEmpty(fullPrice)){
            if (id != null)mBase.child(id).setValue(Cart);




        }else{
            Toast.makeText(ChangeActivity.this,"Возможно некоторые поля пустые!",Toast.LENGTH_SHORT).show();
        }
    }

    public void SaveDate(View view) {

        uploadImage();
    }


    private void uploadImage() {
        Bitmap bitmap = ((BitmapDrawable) imgTovar2.getDrawable()).getBitmap();
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



    public void ShopCart(View view) {
        Intent intent = new Intent(getApplicationContext(),ShopCartAct.class);
        startActivity(intent);
    }

    public void SaveAll(View view) {
        uploadImage();
    }

    public void ImgChoose(View view) {
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
                imgTovar2.setImageURI(data.getData());

            }
        }
    }
}