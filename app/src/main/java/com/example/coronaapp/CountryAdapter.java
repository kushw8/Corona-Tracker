package com.example.coronaapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.coronaapp.api.CountryData;

import java.text.NumberFormat;
import java.util.List;
import java.util.Map;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.CounrtyViewHolder> {

    Context context;
    List<CountryData> list;

    public CountryAdapter(Context context, List<CountryData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CounrtyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.country_items,parent,false);
        return new CounrtyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CounrtyViewHolder holder, int position) {

        CountryData data = list.get(position);

        holder.countryCase.setText(NumberFormat.getInstance().format(Integer.parseInt(data.getCases())));
        holder.countryName.setText(data.getCountry());
        holder.serialNumber.setText(String.valueOf(position+1));

        Map<String,String> img = data.getCountryInfo();

        Glide.with(context).load(img.get("flag")).into(holder.flagImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,MainActivity.class);
                intent.putExtra("country",data.getCountry());
                context.startActivity(intent);
            }
        });

    }

    public void filterList(List<CountryData> filterList){
      list = filterList;
      notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    public class CounrtyViewHolder extends RecyclerView.ViewHolder {

        private TextView serialNumber,countryName,countryCase;
        private ImageView flagImage;

        public CounrtyViewHolder(@NonNull View itemView) {
            super(itemView);

            serialNumber =itemView.findViewById(R.id.serial_number);
            countryName =itemView.findViewById(R.id.counry_name_item);
            countryCase =itemView.findViewById(R.id.counry_case_item);
            flagImage =itemView.findViewById(R.id.flag_image);
        }
    }
}
