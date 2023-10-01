package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
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
    MyAdapter myAdapter;
    ArrayList<Contacts>contactsArrayList;
    Dialog dialog;
    Button addorUpdatebutton;
    Button cancelbutton;
    EditText editTextname;
    EditText editTextemail;
    String updatedname;
    String updatedemail;
    RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView=findViewById(R.id.recyclerview);
        contactsArrayList=new ArrayList<>();
        recyclerView.setHasFixedSize(true);


        floatingActionButton=findViewById(R.id.floating);

        databaseHelper=DatabaseHelper.getDataBase(this);

        LoadAllDataInBackGround();//this is the background task,prevents screen from going inactive due to data loading

        //Log.i("info s ",String.valueOf(contactsArrayList.size()));
        myAdapter=new MyAdapter(getApplicationContext(),contactsArrayList,MainActivity.this);
        layoutManager=new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(myAdapter);

        dialog=new Dialog(this);
        dialog.setContentView(R.layout.add_layout);

        addorUpdatebutton=dialog.findViewById(R.id.add);
        cancelbutton=dialog.findViewById(R.id.cancel);
        editTextname=dialog.findViewById(R.id.name);
        editTextemail=dialog.findViewById(R.id.email);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addorEdit(false,null,-1);
            }
        });

    }
    public void insert(){
        String name=editTextname.getText().toString();
        String email=editTextemail.getText().toString();
        if(TextUtils.isEmpty(name) || TextUtils.isEmpty(email)){
            Toast.makeText(getApplicationContext(),"Please Do Not Leave Any Field Empty",Toast.LENGTH_SHORT).show();
            return;
        }
        long id=databaseHelper.contactsDAO().addData(new Contacts(name,email));

        contactsArrayList.add(0,new Contacts((int)id,name,email));

        editTextname.setText("");
        editTextemail.setText("");
        dialog.dismiss();
        myAdapter.notifyDataSetChanged();
    }

    public void updateDataSet(String name,String email,int position){
        //Log.i("info","reached at updateDataSet");
        Contacts contacts=contactsArrayList.get(position);
        contacts.setName(name);
        contacts.setEmail(email);

        databaseHelper.contactsDAO().updateData(contacts);
        contactsArrayList.set(position,contacts);
        //Log.i("info",contactsArrayList.get(position).getName());
        myAdapter.notifyDataSetChanged();

    }

    public void delete(String name,String email,int position){
        Contacts contacts=new Contacts(contactsArrayList.get(position).getKey_Id(),name,email);
        contactsArrayList.remove(position);
        myAdapter.notifyDataSetChanged();
        databaseHelper.contactsDAO().deleteData(contacts);
    }

    public void addorEdit( boolean status,final Contacts contacts, int position) {
        //Toast.makeText(this," "+status,Toast.LENGTH_SHORT).show();
        //Log.i("info",""+status);
        if(status){//it means data needs to updated or deleted and not to be added
            editTextname.setText(contactsArrayList.get(position).getName());
            editTextemail.setText(contactsArrayList.get(position).getEmail());
            addorUpdatebutton.setText("Update");
            cancelbutton.setText("Delete");
            dialog.show();
            Log.i("info","reached at update");
            updatedname=editTextname.getText().toString();
            updatedemail = editTextemail.getText().toString();
            editTextname.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    updatedname =editTextname.getText().toString();
                    updatedemail =editTextemail.getText().toString();
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            editTextemail.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    updatedname =editTextname.getText().toString();
                    updatedemail =editTextemail.getText().toString();
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            addorUpdatebutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                        //Log.i("info","update pressed"+updatedname+" -> "+ updatedemail);
                        updateDataSet(updatedname, updatedemail,position);
                        dialog.dismiss();

                }
            });
            cancelbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    delete(updatedname, updatedemail,position);
                    dialog.dismiss();
                }
            });
        }else{// it means it needs to be added
            editTextname.setText("");
            editTextemail.setText("");
            addorUpdatebutton.setText("Add");
            cancelbutton.setText("Cancel");
            dialog.show();

            addorUpdatebutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                            insert();
                }
            });
            cancelbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
        }
    }

    public void LoadAllDataInBackGround(){
        //ExecutorService is a JDK API that simplifies running tasks in asynchronous mode.
        ExecutorService executor= Executors.newSingleThreadExecutor();

        //Handler allows communicating back with UI thread from other background thread
        Handler handler=new Handler(Looper.getMainLooper());
        executor.execute(new Runnable() {
            @Override
            public void run() {
                contactsArrayList.addAll(databaseHelper.contactsDAO().getAllContacts());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        myAdapter.notifyDataSetChanged();
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
            contactsArrayList.clear();
            databaseHelper.contactsDAO().deleteAllData();
            myAdapter.notifyDataSetChanged();
        }
        return true;
    }
}