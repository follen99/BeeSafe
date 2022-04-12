package com.example.emergencyui_v1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.emergencyui_v1.adapters.ReportAdapter;
import com.example.emergencyui_v1.viewmodels.ReportViewModel;

import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    ImageView addReportButton;
    ReportViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Objects.requireNonNull(getSupportActionBar()).hide();


        RecyclerView recyclerView = findViewById(R.id.main_recycler_view);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        ReportAdapter adapter = new ReportAdapter();
        recyclerView.setAdapter(adapter);


        viewModel = new ViewModelProvider(this).get(ReportViewModel.class);
        viewModel.getAllReports().observe(this, new Observer<List<Report>>() {
            @Override
            public void onChanged(List<Report> reports) {
                Toast.makeText(MainActivity.this, "Activity updated", Toast.LENGTH_SHORT).show();

                adapter.setReportList(reports);
            }
        });

        addReportButton = findViewById(R.id.add_report_button);
        addReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddReportActivity.class));
            }
        });
    }
}