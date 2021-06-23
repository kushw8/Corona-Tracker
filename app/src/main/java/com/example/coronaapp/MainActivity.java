package com.example.coronaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coronaapp.api.ApiInterface;
import com.example.coronaapp.api.ApiUtils;
import com.example.coronaapp.api.CountryData;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private TextView totalActive,totalDeath,totalRecovered,totalTested,totalConfirm,date;
    private TextView todayRecovered,todayDeath,todayConfirm,counrtyNameMain;
    PieChart mPieChart;

    private List<CountryData> list;
    String country = "India";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intilize();

        if (getIntent().getStringExtra("country") != null){
            country = getIntent().getStringExtra("country");
        }

        counrtyNameMain.setText(country);

        counrtyNameMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,CountryActivity.class));
            }
        });

        ApiUtils.apiInterface().getCountryData().enqueue(new Callback<List<CountryData>>() {
            @Override
            public void onResponse(Call<List<CountryData>> call, Response<List<CountryData>> response) {

                list.addAll(response.body());

                for (int i=0;i<list.size();i++){
                    if (list.get(i).getCountry().equals(country)){
                        int confirm = Integer.parseInt(list.get(i).getCases());
                        int active = Integer.parseInt(list.get(i).getActive());
                        int recovered = Integer.parseInt(list.get(i).getRecovered());
                        int death = Integer.parseInt(list.get(i).getDeaths());

                        totalActive.setText(NumberFormat.getInstance().format(active));
                        totalConfirm.setText(NumberFormat.getInstance().format(confirm));
                        totalDeath.setText(NumberFormat.getInstance().format(death));
                        totalRecovered.setText(NumberFormat.getInstance().format(recovered));

                        todayDeath.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayDeaths())));
                        todayConfirm.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayCases())));
                        todayRecovered.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayRecovered())));

                        totalTested.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTests())));

                        setText(list.get(i).getUpdated());


                        mPieChart.addPieSlice(new PieModel("Confirm", confirm,getResources().getColor(R.color.yellow)));
                        mPieChart.addPieSlice(new PieModel("Active", active, getResources().getColor(R.color.blue)));
                        mPieChart.addPieSlice(new PieModel("Death", death, getResources().getColor(R.color.red)));
                        mPieChart.addPieSlice(new PieModel("Recovered", recovered, getResources().getColor(R.color.green)));

                        mPieChart.startAnimation();


                    }
                }
            }

            @Override
            public void onFailure(Call<List<CountryData>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "error :" +t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setText(String updated) {

        DateFormat format = new SimpleDateFormat("mm dd,yyyy");

        long miliSecond = Long.parseLong(updated);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(miliSecond);

        date.setText("Last Updated: " +format.format(calendar.getTime()));

    }

    private void intilize() {

        totalActive = findViewById(R.id.totalActive);
        totalDeath = findViewById(R.id.totalDeath);
        totalRecovered = findViewById(R.id.totalRecovered);
        totalTested = findViewById(R.id.totalTest);
        totalConfirm = findViewById(R.id.totalConfirm);
        date = findViewById(R.id.date);

        todayRecovered = findViewById(R.id.todayRecovered);
        todayDeath = findViewById(R.id.todayDeath);
        todayConfirm = findViewById(R.id.todayConfirm);
        list = new ArrayList<>();

        mPieChart = (PieChart) findViewById(R.id.piechart);
        counrtyNameMain = findViewById(R.id.counry_name_main);


    }
}