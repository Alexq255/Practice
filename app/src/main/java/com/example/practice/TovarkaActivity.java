package com.example.practice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TovarkaActivity extends AppCompatActivity {

    private ListView tovarList;
    private ArrayAdapter<String> adapter;
    private List<String> listdata;
    private List<TovarAddClass> listTemp;
    private DatabaseReference mBase;
    private String GROUPKEY = "Tovar";
    private TextView Countl;
    private EditText FindText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tovarka);
        init();

    }

    private void init(){
        tovarList = findViewById(R.id.tovarList);
        FindText = findViewById(R.id.FindText);
        listdata = new ArrayList<>();
        Countl = findViewById(R.id.Countl);
        listTemp = new ArrayList<TovarAddClass>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,listdata);
        tovarList.setAdapter(adapter);
        mBase = FirebaseDatabase.getInstance().getReference(GROUPKEY);
        getDataFromDB();
        setOnclickItem();


    }

    private void getDataFromDB(){
        ValueEventListener vListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (listdata.size()<0)listdata.clear();
                if (listTemp.size()<0)listTemp.clear();
                for (DataSnapshot ds : snapshot.getChildren()){
                    TovarAddClass tovar = ds.getValue(TovarAddClass.class);
                    assert tovar != null;
                    listdata.add(tovar.nazvanie);
                    listTemp.add(tovar);
                    Countl.setText("Общее кол-во предложений:"+tovarList.getAdapter().getCount());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mBase.addValueEventListener(vListener);
    }
    private void setOnclickItem(){
        tovarList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TovarAddClass tovarAdd = listTemp.get(i);
                Intent intent = new Intent(TovarkaActivity.this, FullTovarAct.class);
                intent.putExtra("Tovar_imgTovar",tovarAdd.imgTovar);
                intent.putExtra("Tovar_nazvanie",tovarAdd.nazvanie);
                intent.putExtra("Tovar_description",tovarAdd.description);
                intent.putExtra("tovar_fullprice",tovarAdd.fullprice);
                startActivity(intent);
            }
        });
    }




}