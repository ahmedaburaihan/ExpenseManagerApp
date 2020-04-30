package com.example.expmanager2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.example.expmanager2.reporting.ReportDataItems;
import com.example.expmanager2.reporting.ReportItemListAdapter;

import java.util.ArrayList;
import java.util.List;

public class Reporting extends AppCompatActivity {

    private RecyclerView report_rec;
    private ReportItemListAdapter reportAdapter;
    private RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporting);

        Toolbar toolbar = findViewById(R.id.toolbar_report);
        setSupportActionBar(toolbar);

        report_rec = findViewById(R.id.report_rv);
        report_rec.setHasFixedSize(true);
        List<ReportDataItems> myItems = getAllItems();
        layoutManager = new LinearLayoutManager(getApplicationContext());
        report_rec.setLayoutManager(layoutManager);
        reportAdapter = new ReportItemListAdapter(myItems,getApplicationContext());
        report_rec.setAdapter(reportAdapter);





    }

    private List<ReportDataItems> getAllItems() {
            List<ReportDataItems> itemsList = new ArrayList<ReportDataItems>();
        itemsList.add(new ReportDataItems("Food","Food for life","12/10/2020",1000));
        itemsList.add(new ReportDataItems("Rent","Food for life","12/10/2020",2000));
        itemsList.add(new ReportDataItems("Books","Food for life","12/10/2020",3000));
        itemsList.add(new ReportDataItems("Shopping","Food for life","12/10/2020",4000));
        itemsList.add(new ReportDataItems("Car","Food for life","12/10/2020",5000));
        itemsList.add(new ReportDataItems("Food","Food for life","12/10/2020",1000));
        itemsList.add(new ReportDataItems("Rent","Food for life","12/10/2020",2000));
        itemsList.add(new ReportDataItems("Books","Food for life","12/10/2020",3000));
        itemsList.add(new ReportDataItems("Shopping","Food for life","12/10/2020",4000));
        itemsList.add(new ReportDataItems("Car","Food for life","12/10/2020",5000));
        itemsList.add(new ReportDataItems("Food","Food for life","12/10/2020",1000));
        itemsList.add(new ReportDataItems("Rent","Food for life","12/10/2020",2000));
        itemsList.add(new ReportDataItems("Books","Food for life","12/10/2020",3000));
        itemsList.add(new ReportDataItems("Shopping","Food for life","12/10/2020",4000));
        itemsList.add(new ReportDataItems("Car","Food for life","12/10/2020",5000));

        return itemsList;
    }

    public void itemClick(View view, int position) {
        Toast.makeText(getApplicationContext(), "test_test1", Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.reporting_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
