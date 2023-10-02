package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;
    DatabaseHelper databaseHelper;

    RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();//initialising objects
        recyclerView=findViewById(R.id.recyclerview);

        floatingActionButton=findViewById(R.id.floating);

        databaseHelper=DatabaseHelper.getDataBase(this);

        LoadAllDataInBackGround();//this is the background task,prevents screen from going inactive due to data loading

        //Log.i("info s ",String.valueOf(contactsArrayList.size()));

        layoutManager=new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(AppObjects.myAdapter);


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),AddorModifyActivity.class);
                intent.putExtra("BoolValue",false); // true for modify and false for add
                startActivity(intent);
            }
        });

    }
    public void init(){
        Toast.makeText(getApplicationContext(),"hgf",Toast.LENGTH_SHORT).show();
        AppObjects.notesArrayList=new ArrayList<>();
        AppObjects.myAdapter=new MyAdapter(getApplicationContext(),AppObjects.notesArrayList,MainActivity.this);
    }

    public void LoadAllDataInBackGround(){

        Toast.makeText(getApplicationContext(),String.valueOf(AppObjects.i++),Toast.LENGTH_SHORT).show();
        //ExecutorService is a JDK API that simplifies running tasks in asynchronous mode.
        ExecutorService executor= Executors.newSingleThreadExecutor();

        //Handler allows communicating back with UI thread from other background thread
        Handler handler=new Handler(Looper.getMainLooper());
        executor.execute(new Runnable() {
            @Override
            public void run() {
                AppObjects.notesArrayList.addAll(databaseHelper.notesDAO().getAllNotes());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        AppObjects.myAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_view,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.deleteAllmenu) {
            AppObjects.notesArrayList.clear();;
            databaseHelper.notesDAO().deleteAllData();
            AppObjects.myAdapter.notifyDataSetChanged();
        }
        return true;
    }
}