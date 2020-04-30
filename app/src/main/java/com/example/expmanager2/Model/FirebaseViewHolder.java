package com.example.expmanager2.Model;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expmanager2.R;

public class FirebaseViewHolder extends RecyclerView.ViewHolder {

    public TextView date, type, note, amount;


    public FirebaseViewHolder(@NonNull View itemView) {
        super(itemView);

        date = itemView.findViewById(R.id.date_txt_income);
        type = itemView.findViewById(R.id.type_txt_income);
        note = itemView.findViewById(R.id.note_txt_income);
        amount = itemView.findViewById(R.id.amount_txt_income);

    }

    public void setData(Data data){
        String date = data.getDate();
        String type = data.getType();
        String note = data.getNote();
        String amount = String.valueOf(data.getAmount());
    }

}
