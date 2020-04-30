package com.example.expmanager2;


import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.expmanager2.Model.Data;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;

import static androidx.constraintlayout.widget.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashBoardFragment extends Fragment {

    //DASHBOARD INCOME and EXPENSE results....
    private TextView totalIncomeResult;
    private TextView totalExpenseResult;

    //RECYCLER VIEWS for INCOME and EXPENSE
    private RecyclerView incomeDashboardRecycler;
    private RecyclerView expenseDashboardRecycler;
    //////////////////////////////////////////////


    //floating button
    private FloatingActionButton fab_main_btn;
    private FloatingActionButton fab_income_btn;
    private FloatingActionButton fab_expense_btn;

    //floating button text view
    private TextView fab_income_txt;
    private TextView fab_expense_txt;


    //boolean
    private boolean isOpen = false;


    //Animation class object
    private Animation FadeOpen, FadeClose;


    ///FireBase Database...
    private FirebaseAuth mAuth;
    private DatabaseReference mIncomeDatabase;
    private DatabaseReference mExpenseDatabase;



    public DashBoardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_dash_board, container, false);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        String uid = mUser.getUid();




        mIncomeDatabase = FirebaseDatabase.getInstance().getReference().child("IncomeData").child(uid);



        mExpenseDatabase = FirebaseDatabase.getInstance().getReference().child("ExpenseData").child(uid);


        //connecting floating to layout
        fab_main_btn = myView.findViewById(R.id.fb_main_plus_btn);
        fab_income_btn = myView.findViewById(R.id.income_ft_btn);
        fab_expense_btn= myView.findViewById(R.id.expense_ft_btn);

        //connect floating text
        fab_income_txt = myView.findViewById(R.id.income_ft_txt);
        fab_expense_txt = myView.findViewById(R.id.expense_ft_text);

        //Connect animation
        FadeOpen = AnimationUtils.loadAnimation(getActivity(),R.anim.fade_open);
        FadeClose = AnimationUtils.loadAnimation(getActivity(),R.anim.fade_close);

        //CONNECTING RECYCLER VIEWS.........................
        incomeDashboardRecycler = myView.findViewById(R.id.recycler_income_dashboard);
        expenseDashboardRecycler= myView.findViewById(R.id.recycler_expense_dashboard);



        fab_main_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    //save data function (calling)
                    addData();

                if(isOpen){

                    fab_income_btn.startAnimation(FadeClose);
                    fab_expense_btn.startAnimation(FadeClose);
                    fab_income_btn.setClickable(false);
                    fab_expense_btn.setClickable(false);

                    fab_income_txt.startAnimation(FadeClose);
                    fab_expense_txt.startAnimation(FadeClose);
                    fab_income_txt.setClickable(false);
                    fab_expense_txt.setClickable(false);

                    isOpen = false;

                } else{
                    fab_income_btn.startAnimation(FadeOpen);
                    fab_expense_btn.startAnimation(FadeOpen);
                    fab_income_btn.setClickable(true);
                    fab_expense_btn.setClickable(true);

                    fab_income_txt.startAnimation(FadeOpen);
                    fab_expense_txt.startAnimation(FadeOpen);
                    fab_income_txt.setClickable(true);
                    fab_expense_txt.setClickable(true);

                    isOpen = true;
                }

            }
        });


        //CODE FOR TOTAL INCOME AND EXPENSE
        totalIncomeResult = myView.findViewById(R.id.income_set_result);
        totalExpenseResult= myView.findViewById(R.id.expense_set_result);
                //Calculate totalIncome Data
        mIncomeDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int totalSum = 0;
                for(DataSnapshot mysnapshot: dataSnapshot.getChildren()){

                    Data data = mysnapshot.getValue(Data.class);
                    totalSum += data.getAmount();
                    String stResult = String.valueOf(totalSum);
                    totalIncomeResult.setText(stResult+".00");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

                //calculate totalExpense Data
        mExpenseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int totalSum = 0;
                for(DataSnapshot mysnapshot: dataSnapshot.getChildren()){
                    Data data = mysnapshot.getValue(Data.class);
                    totalSum += data.getAmount();
                    String stResult = String.valueOf(totalSum);
                    totalExpenseResult.setText(stResult+".00");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ///////////////////////////////////
        //RECYCLER VIEWS FOR DASHBOARD_ LINEARLAYOUT CONNECTION
        LinearLayoutManager layoutManagerIncome = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false);
        layoutManagerIncome.setStackFromEnd(true);
        layoutManagerIncome.setReverseLayout(true);
        incomeDashboardRecycler.setHasFixedSize(true);
        incomeDashboardRecycler.setLayoutManager(layoutManagerIncome);

        LinearLayoutManager layoutManagerExpense= new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false);
        layoutManagerExpense.setStackFromEnd(true);
        layoutManagerExpense.setReverseLayout(true);
        expenseDashboardRecycler.setHasFixedSize(true);
        expenseDashboardRecycler.setLayoutManager(layoutManagerExpense);

        return myView;
    }


    //Floating Button Animation...
    private void ftAnimation(){

        if(isOpen){

            fab_income_btn.startAnimation(FadeClose);
            fab_expense_btn.startAnimation(FadeClose);
            fab_income_btn.setClickable(false);
            fab_expense_btn.setClickable(false);

            fab_income_txt.startAnimation(FadeClose);
            fab_expense_txt.startAnimation(FadeClose);
            fab_income_txt.setClickable(false);
            fab_expense_txt.setClickable(false);

            isOpen = false;

        } else{
            fab_income_btn.startAnimation(FadeOpen);
            fab_expense_btn.startAnimation(FadeOpen);
            fab_income_btn.setClickable(true);
            fab_expense_btn.setClickable(true);

            fab_income_txt.startAnimation(FadeOpen);
            fab_expense_txt.startAnimation(FadeOpen);
            fab_income_txt.setClickable(true);
            fab_expense_txt.setClickable(true);

            isOpen = true;
        }

    }

    private void addData(){

        //Fab Button income
        fab_income_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                incomeDataInsert();


            }
        });

        //Fab expense button
        fab_expense_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expenseDataInsert();
            }
        });
    }

    public void incomeDataInsert(){
        AlertDialog.Builder myDialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View myView  = inflater.inflate(R.layout.custom_layout_for_insertdata,null);

        myDialog.setView(myView);
        final AlertDialog dialog = myDialog.create();
        dialog.setCancelable(false);

        final EditText editAmount = myView.findViewById(R.id.amount_edittxt);
        final EditText editType = myView.findViewById(R.id.type_edittxt);
        final EditText editNote = myView.findViewById(R.id.note_edittxt);

        Button btnSave = myView.findViewById(R.id.btn_save);
        Button btnCancel = myView.findViewById(R.id.btn_cancel);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type = editType.getText().toString().trim();
                String amount = editAmount.getText().toString().trim();
                String note = editNote.getText().toString().trim();


                if(TextUtils.isEmpty(type)){
                    editType.setError("Required Field...");
                    return;
                }

                if(TextUtils.isEmpty(amount)){
                    editAmount.setError("Required Field...");
                    return;
                }

                int ourAmountInt = Integer.parseInt(amount);
                if(TextUtils.isEmpty(note)){
                    editNote.setError("Required Field...");
                }


                String id = mIncomeDatabase.push().getKey();

                String mDate = DateFormat.getDateInstance().format(new Date());

                Data data = new Data(ourAmountInt,type,note,id,mDate);

                mIncomeDatabase.child(id).setValue(data);
                Toast.makeText(getActivity(), "Data Added", Toast.LENGTH_SHORT).show();
                ftAnimation();
                dialog.dismiss();

            }
        });


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ftAnimation();
        dialog.show();
    }


    public void expenseDataInsert() {

        AlertDialog.Builder mydialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View myView = inflater.inflate(R.layout.custom_layout_for_insertdata,null);
        mydialog.setView(myView);

        final AlertDialog dialog = mydialog.create();
        dialog.setCancelable(false);

        final EditText amount = myView.findViewById(R.id.amount_edittxt);
        final EditText type = myView.findViewById(R.id.type_edittxt);
        final EditText note = myView.findViewById(R.id.note_edittxt);

        Button btnSave = myView.findViewById(R.id.btn_save);
        Button btnCanel = myView.findViewById(R.id.btn_cancel);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String tmAmount = amount.getText().toString().trim();
                String tmType = type.getText().toString().trim();
                String tmNote = note.getText().toString().trim();

                if(TextUtils.isEmpty(tmAmount)){
                    amount.setError("Field Required...");
                    return;
                }

                int inAmount = Integer.parseInt(tmAmount);

                if(TextUtils.isEmpty(tmType)){
                    type.setError("Required Field...");
                    return;
                }

                if(TextUtils.isEmpty(tmNote)){
                    note.setError("Required Field...");
                    return;
                }

                String id = mExpenseDatabase.push().getKey();
                String mDate = DateFormat.getDateInstance().format(new Date());
                Data data = new Data(inAmount,tmType,tmNote,id,mDate);
                mExpenseDatabase.child(id).setValue(data);
                Toast.makeText(getActivity(),   "Data Added", Toast.LENGTH_SHORT).show();

                ftAnimation();
                dialog.dismiss();
            }
        });

        btnCanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ftAnimation();
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Data>()
                .setQuery(mIncomeDatabase,Data.class)
                .build();

        FirebaseRecyclerAdapter<Data, IncomeViewHolder> adapterIncome = new FirebaseRecyclerAdapter<Data, IncomeViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull IncomeViewHolder holder, int position, @NonNull Data model) {
                String userIDs = getRef(position).getKey();
                holder.setIncomeAmount(model.getAmount());
                holder.setIncomeDate(model.getDate());
                holder.setIncomeType(model.getType());

            }

            @NonNull
            @Override
            public IncomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_income,parent,false);
                IncomeViewHolder holder = new IncomeViewHolder(view);
                return holder;
            }
        };

        //EXPENSE DATA ADAPTER
        FirebaseRecyclerOptions options2 = new FirebaseRecyclerOptions.Builder<Data>()
                .setQuery(mExpenseDatabase,Data.class)
                .build();

        FirebaseRecyclerAdapter<Data, ExpenseViewHolder> adapterExpense = new FirebaseRecyclerAdapter<Data, ExpenseViewHolder>(options2) {
            @Override
            protected void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position, @NonNull Data model) {
                String userIDs = getRef(position).getKey();
                holder.setExpenseAmount(model.getAmount());
                holder.setExpenseDate(model.getDate());
                holder.setExpenseType(model.getType());

            }

            @NonNull
            @Override
            public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_expense,parent,false);
                ExpenseViewHolder holder2 = new ExpenseViewHolder(view);
                return holder2;
            }
        };


        incomeDashboardRecycler.setAdapter(adapterIncome);
        adapterIncome.startListening();

        //Expense recycler
        expenseDashboardRecycler.setAdapter(adapterExpense);
        adapterExpense.startListening();
    }

    public static class IncomeViewHolder extends RecyclerView.ViewHolder {
        View mIncomeView;
        public IncomeViewHolder(@NonNull View itemView) {
            super(itemView);
            mIncomeView = itemView;
        }

        public void setIncomeType(String type){
            TextView mType = mIncomeView.findViewById(R.id.type_income_dashboard);
            mType.setText(type);
        }

        public void setIncomeAmount(int amount){
            TextView mAmount = mIncomeView.findViewById(R.id.amount_income_dashboard);
            mAmount.setText(String.valueOf(amount));
        }

        public void setIncomeDate(String date){
            TextView mDate = mIncomeView.findViewById(R.id.date_income_dashboard);
            mDate.setText(date);
        }
    }

    //Expense Viewholder
    public static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        View mExpenseView;
        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            mExpenseView = itemView;
        }

        public void setExpenseType(String type){
            TextView mType = mExpenseView.findViewById(R.id.type_expense_dashboard);
            mType.setText(type);
        }

        public void setExpenseAmount(int amount){
            TextView mAmount = mExpenseView.findViewById(R.id.amount_expense_dashboard);
            mAmount.setText(String.valueOf(amount));
        }

        public void setExpenseDate(String date){
            TextView mDate = mExpenseView.findViewById(R.id.date_expense_dashboard);
            mDate.setText(date);
        }
    }








}
