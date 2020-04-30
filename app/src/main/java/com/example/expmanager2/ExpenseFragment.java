package com.example.expmanager2;


import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.expmanager2.Model.Data;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.units.qual.A;

import java.text.DateFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class ExpenseFragment extends Fragment {

    private RecyclerView recyclerView2;
    //DB Reference && Auth
    private DatabaseReference dataRef, usersRef;
    private FirebaseAuth mAuth;
    private String currentUserID;

    //TOtal income textview
    private TextView expenseTotalSum;

    //EDIT TEXT DATA ITEM for UPDATE
    private EditText editAmount;
    private EditText editType;
    private EditText editNote;
    private Button btnUpdate;
    private Button btnDelete;

    //Global data variables...
    private String expType;
    private String expNote;
    private int  expAmount;
    private String post_key;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_expense, container, false);
        //Setting value for incometotalsum
        expenseTotalSum = myView.findViewById(R.id.expense_txt_result);


        recyclerView2 = myView.findViewById(R.id.recycler_id_expense);
        recyclerView2.setLayoutManager(new LinearLayoutManager(getContext()));


        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        dataRef = FirebaseDatabase.getInstance().getReference().child("ExpenseData").child(currentUserID);
        usersRef = FirebaseDatabase.getInstance().getReference().child("ExpenseData").child(currentUserID);


        //ADD ValueEventListener
        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //To calculate total income values
                int totalValue = 0;
                for(DataSnapshot mySnapShot: dataSnapshot.getChildren()){
                    Data data = mySnapShot.getValue(Data.class);
                    totalValue+= data.getAmount();
                    String stTotalValue = String.valueOf(totalValue);
                    expenseTotalSum.setText(stTotalValue+".00");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





        return myView;
    }


    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Data>()
                .setQuery(dataRef, Data.class)
                .build();

        FirebaseRecyclerAdapter<Data, DataViewHolder2> adapter = new FirebaseRecyclerAdapter<Data, DataViewHolder2>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final DataViewHolder2 holder, final int position, @NonNull final Data model) {
                String userIDs = getRef(position).getKey();
                usersRef.child(userIDs).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild("date")){
                            String date = dataSnapshot.child("date").getValue().toString();
                            String type = dataSnapshot.child("type").getValue().toString();
                            String note = dataSnapshot.child("note").getValue().toString();
                            String amount = dataSnapshot.child("amount").getValue().toString();

                            holder.date1.setText(date);
                            holder.type1.setText(type);
                            holder.note1.setText(note);
                            holder.amount1.setText(amount);

                            holder.mView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    post_key = getRef(position).getKey();
                                    expType = model.getType();
                                    expNote = model.getNote();
                                    expAmount = model.getAmount();

                                    updateDataItem();
                                }
                            });

                            Toast.makeText(getContext(), "show me yourselef", Toast.LENGTH_SHORT).show();


                        } else{
                            //String date = dataSnapshot.child("date").getValue().toString();
                            //String type = dataSnapshot.child("type").getValue().toString();
                            //String note = dataSnapshot.child("note").getValue().toString();
                            //String amount = dataSnapshot.child("amount").getValue().toString();
                            //Toast.makeText(getContext(), "What happened...", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @NonNull
            @Override
            public DataViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_recycler_data,parent,false);
                DataViewHolder2 viewHolder = new DataViewHolder2(view);
                return viewHolder;
            }
        };


        recyclerView2.setAdapter(adapter);
        adapter.startListening();


    }


    public static class DataViewHolder2 extends  RecyclerView.ViewHolder{

        TextView date1, type1, note1, amount1;
        View mView;

        public DataViewHolder2(@NonNull View itemView) {
            super(itemView);
            mView = itemView;


            //Initializing...
            date1 = itemView.findViewById(R.id.date_txt_expense);
            type1 = itemView.findViewById(R.id.type_txt_expense);
            note1 = itemView.findViewById(R.id.note_txt_expense);
            amount1=itemView.findViewById(R.id.amount_txt_expense);


        }

        private void setType(String type){
            TextView mType = mView.findViewById(R.id.type_txt_expense);
            mType.setText(type);
        }

        private void setNote(String note){
            TextView mNote = mView.findViewById(R.id.note_txt_expense);
            mNote.setText(note);
        }

        private void setDate(String date){
            TextView mDate = mView.findViewById(R.id.date_txt_expense);
            mDate.setText(date);
        }

        private void setAmount(int amount){
            TextView mAmount = mView.findViewById(R.id.amount_txt_expense);
            String stamount = String.valueOf(amount);
            mAmount.setText(stamount);
        }

    }

        private void updateDataItem(){
            AlertDialog.Builder myDialog = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View myView = inflater.inflate(R.layout.update_data_item,null);
            myDialog.setView(myView);

            editAmount = myView.findViewById(R.id.amount_edittxt);
            editNote   = myView.findViewById(R.id.note_edittxt);
            editType   = myView.findViewById(R.id.type_edittxt);
            btnUpdate  = myView.findViewById(R.id.btn_update);
            btnDelete  = myView.findViewById(R.id.btn_delete);

            editType.setText(expType);
            editType.setSelection(expType.length());
            editNote.setText(expNote);
            editNote.setSelection(expNote.length());
            editAmount.setText(String.valueOf(expAmount));
            editAmount.setSelection(String.valueOf(expAmount).length());

            final AlertDialog  dialog = myDialog.create();

            btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        expType = editType.getText().toString().trim();
                        expNote = editNote.getText().toString().trim();
                        String stAmount = String.valueOf(expAmount);
                        stAmount = editAmount.getText().toString().trim();
                        int intAmount = Integer.parseInt(stAmount);
                        String mDate = DateFormat.getDateInstance().format(new Date());

                        Data data = new Data(intAmount,expType, expNote,post_key,mDate);
                        dataRef.child(post_key).setValue(data);

                        dialog.dismiss();

                }
            });

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dataRef.child(post_key).removeValue();
                    dialog.dismiss();
                }
            });

            dialog.show();
        }

}





















