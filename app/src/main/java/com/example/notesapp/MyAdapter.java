package com.example.notesapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    ArrayList<Notes> arrayList;
    Context context;
    MainActivity mainActivity;
    public MyAdapter(Context context,ArrayList<Notes>arrayList,MainActivity mainActivity) {
        this.arrayList=arrayList;
        this.context=context;
        this.mainActivity=mainActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.note_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Notes contacts=arrayList.get(position);
        holder.note.setText(arrayList.get(position).getNote());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //mainActivity.addorEdit(true,contacts,holder.getAdapterPosition());
                Intent intent=new Intent(context,AddorModifyActivity.class);
                intent.putExtra("BoolValue",true); // true for modify and false for add
                intent.putExtra("Note",holder.note.getText().toString());
                intent.putExtra("position",holder.getAdapterPosition());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView note;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            note=itemView.findViewById(R.id.note);
        }
    }

}
