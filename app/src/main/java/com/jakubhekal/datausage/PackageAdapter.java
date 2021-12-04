package com.jakubhekal.datausage;

import android.content.Context;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jakubhekal.datausage.model.Package;

import java.util.List;

public class PackageAdapter extends RecyclerView.Adapter<PackageAdapter.PackageViewHolder> {
    List<Package> mPackageList;

    public PackageAdapter(List<Package> packageList) {
        mPackageList = packageList;
    }

    @NonNull
    @Override
    public PackageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_app, parent, false);
        return new PackageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PackageViewHolder holder, int position) {
        Package packageItem = mPackageList.get(position);
        holder.name.setText(packageItem.getName());
        holder.dataUsage.setText(Utils.convertFromBytes(packageItem.getDataUsage()));
        try {
            holder.icon.setImageDrawable(holder.context.getPackageManager().getApplicationIcon(packageItem.getPackageName()));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mPackageList.size();
    }

    public class PackageViewHolder extends RecyclerView.ViewHolder {
        Context context;
        TextView name;
        TextView dataUsage;
        ImageView icon;

        public PackageViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            name = itemView.findViewById(R.id.text_app_name);
            dataUsage = itemView.findViewById(R.id.text_data_usage);
            icon = itemView.findViewById(R.id.app_icon);
            itemView.setOnClickListener(view -> {

            });
        }
    }
}
