package com.example.yallasyara.activity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.yallasyara.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class carsAdapter extends RecyclerView.Adapter<carsAdapter.ProductViewHolder> {

    private Context mCtx;
    private List<Car> carList;
    private String user_id;
    Car car;
    public double cLat;
    public double cLng;


    public carsAdapter(Context mCtx,List<Car> carList,String user_id,double cLat,double cLng) {
        this.mCtx = mCtx;
        this.carList = carList;
        this.user_id=user_id;
        this.cLat=cLat;
        this.cLng=cLng;

    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.car_list, null);

        return new ProductViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ProductViewHolder holder, final int position) {
        car = carList.get(position);

        holder.textViewModelName.setText(car.getModel_name());
        holder.textViewYear.setText(String.valueOf(car.getProduction_year()));
        //loading the image

        String url="http://172.20.10.2/"+car.getImage_path();
        Picasso.with(mCtx).load(url).into(holder.imageView);
        holder.textViewDistance.setText(String.valueOf(car.getDistance()));
        holder.textViewFuelLevel.setText(String.valueOf(car.getFuel_level()));

       holder.relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(mCtx,DetailActivity.class);
                intent.putExtra("car_id",carList.get(position).getId());
                intent.putExtra("Model_Name",carList.get(position).getModel_name());
                intent.putExtra("Year",carList.get(position).getProduction_year());
                intent.putExtra("Distance",carList.get(position).getDistance());
                intent.putExtra("Fuel_level",carList.get(position).getFuel_level());
                intent.putExtra("Latitude",carList.get(position).getLatitude());
                intent.putExtra("Longitude",carList.get(position).getLongitude());
                intent.putExtra("user_id",user_id);
                intent.putExtra("clat",cLat);
                intent.putExtra("clng",cLng);

                mCtx.startActivity(intent);

            }
        });
    }


    @Override
    public int getItemCount() {
        return carList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView textViewModelName, textViewYear,textViewFuelLevel,textViewDistance;
        ImageView imageView;
        RelativeLayout relative;

        public ProductViewHolder(View itemView) {
            super(itemView);

            textViewModelName = itemView.findViewById(R.id.textViewModelName);
            textViewYear = itemView.findViewById(R.id.textViewYear);
            imageView = itemView.findViewById(R.id.imageView);
            textViewFuelLevel = itemView.findViewById(R.id.textViewFuelLevel);
            textViewDistance=itemView.findViewById(R.id.textViewDistance);
            relative=itemView.findViewById(R.id.relative);
        }
    }
}


