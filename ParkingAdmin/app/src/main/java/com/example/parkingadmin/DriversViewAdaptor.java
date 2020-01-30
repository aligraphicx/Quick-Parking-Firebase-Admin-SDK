package com.example.parkingadmin;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.cloud.storage.Acl;
import com.google.firebase.auth.ListUsersPage;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class DriversViewAdaptor extends RecyclerView.Adapter<DriversViewAdaptor.MyViewHolder> {
    private Context context;
    private List<DriverType> driverList;

    public DriversViewAdaptor(Context context, List<DriverType> driverList) {
        this.context = context;
        this.driverList = driverList;
    }


    @NonNull
    @Override
    public DriversViewAdaptor.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.driver_list_view,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DriversViewAdaptor.MyViewHolder holder, final int position) {

        final DriverType r = driverList.get(position);


        holder.driverName.setText(r.getFirstName());
        holder.email.setText(r.getEmail());
        Glide.with(context).load(r.getProfileLink()).override(512,512).circleCrop().placeholder(R.drawable.profile).into(holder.profile);

        holder.viewList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DriverType driver=driverList.get(position);
                Intent intent=new Intent(context,DriverView.class);
                intent.putExtra("recorde",driver);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return driverList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{


        public ImageView profile;
        public TextView driverName;
        public TextView email;
        public CardView viewList;

        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            profile=itemView.findViewById(R.id.driverProfile);
            driverName=itemView.findViewById(R.id.driverName);
            email=itemView.findViewById(R.id.driverEmail);
            viewList=itemView.findViewById(R.id.driverListView);

        }
    }


}
