package com.example.expmanager2;


import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.expmanager2.Model.Data;
import com.example.expmanager2.Model.FirebaseViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.common.data.DataHolder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class IncomeFragment extends Fragment {

    private RecyclerView recyclerView;
    //DB Reference && Auth
    private DatabaseReference dataRef, usersRef;
    private FirebaseAuth mAuth;
    private String currentUserID;

    //TOtal income textview
    private TextView incomeTotalSum;


    //FOR UPDATING INCOME
    private EditText editAmount;
    private EditText editType;
    private EditText editNote;
    private Button btnUpdate;
    private Button btnDelete;
    //Data value items for update...
    private String uptype;
    private String upnote;
    private int    upamount;
    private String post_key;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myview = inflater.inflate(R.layout.fragment_income, container, false);
        //Setting value for incometotalsum
        incomeTotalSum = myview.findViewById(R.id.income_txt_result);


        recyclerView = myview.findViewById(R.id.recycler_id_income);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        dataRef = FirebaseDatabase.getInstance().getReference().child("IncomeData").child(currentUserID);
        usersRef = FirebaseDatabase.getInstance().getReference().child("IncomeData").child(currentUserID);


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
                    incomeTotalSum.setText(stTotalValue+".00");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });







        return myview;

    }
/////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Data>()
                .setQuery(dataRef, Data.class)
                .build();

        FirebaseRecyclerAdapter<Data, DataViewHolder> adapter = new FirebaseRecyclerAdapter<Data, DataViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final DataViewHolder holder, final int position, @NonNull final Data model) {
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
                                        uptype = model.getType();
                                        upnote = model.getNote();
                                        upamount = model.getAmount();

                                        updateDataItem();
                                    }
                                });


                            } else{
                                //String date = dataSnapshot.child("date").getValue().toString();
                                //String type = dataSnapshot.child("type").getValue().toString();
                                //String note = dataSnapshot.child("note").getValue().toString();
                                //String amount = dataSnapshot.child("amount").getValue().toString();
                                Toast.makeText(getContext(), "What happened...", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

            }

            @NonNull
            @Override
            public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.income_recycler_data,parent,false);
                DataViewHolder viewHolder = new DataViewHolder(view);
                return viewHolder;
            }
        };


        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();


    }


    public static class DataViewHolder extends  RecyclerView.ViewHolder{

        TextView date1, type1, note1, amount1;
        View mView;

        public DataViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;


            //Initializing...
            date1 = itemView.findViewById(R.id.date_txt_income);
            type1 = itemView.findViewById(R.id.type_txt_income);
            note1 = itemView.findViewById(R.id.note_txt_income);
            amount1=itemView.findViewById(R.id.amount_txt_income);




        }

        private void setType(String type){
            TextView mType = mView.findViewById(R.id.type_txt_income);
            mType.setText(type);
        }

        private void setNote(String note){
            TextView mNote = mView.findViewById(R.id.note_txt_income);
            mNote.setText(note);
        }

        private void setDate(String date){
            TextView mDate = mView.findViewById(R.id.date_txt_income);
            mDate.setText(date);
        }

        private void setAmount(int amount){
            TextView mAmount = mView.findViewById(R.id.amount_txt_income);
            String stamount = String.valueOf(amount);
            mAmount.setText(stamount);
        }

    }



    private void updateDataItem(){
        AlertDialog.Builder  myDialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View myView = inflater.inflate(R.layout.update_data_item,null);
        myDialog.setView(myView);

        editAmount = myView.findViewById(R.id.amount_edittxt);
        editNote   = myView.findViewById(R.id.note_edittxt);
        editType   = myView.findViewById(R.id.type_edittxt);

        btnUpdate  = myView.findViewById(R.id.btn_update);
        btnDelete  = myView.findViewById(R.id.btn_delete);

        editType.setText(uptype);
        editType.setSelection(uptype.length());
        editNote.setText(upnote);
        editNote.setSelection(upnote.length());
        editAmount.setText(String.valueOf(upamount));
        editAmount.setSelection(String.valueOf(upamount).length());

        final AlertDialog dialog = myDialog.create();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uptype = editType.getText().toString().trim();
                upnote = editNote.getText().toString().trim();

                String mdAmount = String.valueOf(upamount);
                mdAmount = editAmount.getText().toString().trim();

                int myAmount = Integer.parseInt(mdAmount);

                String mDate = DateFormat.getDateInstance().format(new Date());
                Data data = new Data(myAmount,uptype,upnote,post_key,mDate);

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
