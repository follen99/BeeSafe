package com.example.scrolltest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView simpleList;
    String countryList[] = {"India", "China", "australia", "Portugle", "America", "NewZealand"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        simpleList = (ListView) findViewById(R.id.SimpleListView);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.activity_listview, R.id.textView, countryList);
        simpleList.setAdapter(adapter);

    }
}