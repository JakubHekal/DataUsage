package com.jakubhekal.datausage.views;

import android.content.Context;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.jakubhekal.datausage.R;
import com.jakubhekal.datausage.utils.Utils;
import com.jakubhekal.datausage.model.Package;

import java.util.List;

public class PackageAdapter extends RecyclerView.Adapter<PackageAdapter.PackageViewHolder> {
    List<Package> mPackageList;

    public PackageAdapter(List<Package> packageList) {
        mPackageList = packageList;
    }

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
            name = (TextView) itemView.findViewById(R.id.text_app_name);
            dataUsage = (TextView) itemView.findViewById(R.id.text_data_usage);
            icon = (ImageView) itemView.findViewById(R.id.app_icon);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }
}
