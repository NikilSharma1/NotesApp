package com.example.notesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddorModifyActivity extends AppCompatActivity {

    Boolean status;
    String note;

    Button addorUpdate;
    Button deleteorCancel;
    int position;

    EditText noteEditText;

    DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_modify);

        addorUpdate=findViewById(R.id.addorupdate);
        deleteorCancel=findViewById(R.id.deleteorcancel);
        noteEditText=findViewById(R.id.edit_add_mod);
        databaseHelper=DatabaseHelper.getDataBase(this);

        Intent intent=getIntent();
        note="";
        position=-1;

        status=intent.getBooleanExtra("BoolValue",false);
        note=intent.getStringExtra("Note");
        position=intent.getIntExtra("position",0);
        init();
        if(status){
            //it means activity has been opened through intent from the recyclerview adapter which means we have to either
            // modify or delete the note.
            addorUpdate.setText("Update");
            deleteorCancel.setText("Delete");
            noteEditText.setText(note);
        }else{
            addorUpdate.setText("Add");
            deleteorCancel.setText("Cancel");
        }
    }
    public void init(){
        addorUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(status){
                    modify(note, position);
                }else {
                    addnote();

                }
            }
        });
        deleteorCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(status){
                    deleteNote(note, position);
                }else cancelAdd();
            }
        });
    }
    public void addnote() {
        String note=noteEditText.getText().toString();
        if(TextUtils.isEmpty(note)){
            Toast.makeText(getApplicationContext(),"Please Do Not Leave Any Field Empty",Toast.LENGTH_SHORT).show();
            return;
        }
        long id=databaseHelper.notesDAO().addData(new Notes(note));
        AppObjects.notesArrayList.add(0,new Notes(id,note));
        AppObjects.myAdapter.notifyDataSetChanged();
        //startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
    public void modify(String note,int position) {
        //Log.i("info","reached at updateDataSet");
        Notes notes=AppObjects.notesArrayList.get(position);
        notes.setNote(note);

        databaseHelper.notesDAO().updateData(notes);
        AppObjects.notesArrayList.set(position,notes);
        //Log.i("info",contactsArrayList.get(position).getName());
        AppObjects.myAdapter.notifyDataSetChanged();
        finish();
    }
    public void deleteNote(String notes,int position){
        Notes note=new Notes(AppObjects.notesArrayList.get(position).getKey_Id(),notes);
        AppObjects.notesArrayList.remove(position);
        databaseHelper.notesDAO().deleteData(note);
        AppObjects.myAdapter.notifyDataSetChanged();
        finish();
    }
    public void cancelAdd(){
        finish();
    }
}