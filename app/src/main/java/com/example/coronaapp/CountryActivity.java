package com.example.coronaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import com.example.coronaapp.api.ApiUtils;
import com.example.coronaapp.api.CountryData;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CountryActivity extends AppCompatActivity {

    private List<CountryData> list;
    private RecyclerView recyclerView;
    private ProgressDialog dialog;
    private CountryAdapter adapter;

    private EditText searchBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country);

        recyclerView = findViewById(R.id.country_name_recycleView);
        list = new ArrayList<>();

        searchBar = findViewById(R.id.search_bar);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter = new CountryAdapter(this,list);
        recyclerView.setAdapter(adapter);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        dialog.show();

        ApiUtils.apiInterface().getCountryData().enqueue(new Callback<List<CountryData>>() {
            @Override
            public void onResponse(Call<List<CountryData>> call, Response<List<CountryData>> response) {
                list.addAll(response.body());
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<CountryData>> call, Throwable t) {
                Toast.makeText(CountryActivity.this, "Error:" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
              filter(s.toString());
            }
        });


    }

    private void filter(String text) {

        List<CountryData> filterList = new ArrayList<>();

        for (CountryData items : list){
            if (items.getCountry().toLowerCase().contains(text.toLowerCase())){
                filterList.add(items);
            }
        }
        adapter.filterList(filterList);
    }
}