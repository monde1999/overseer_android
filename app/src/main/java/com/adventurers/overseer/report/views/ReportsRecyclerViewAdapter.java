package com.adventurers.overseer.report.views;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adventurers.overseer.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class ReportsRecyclerViewAdapter extends RecyclerView.Adapter<ReportsRecyclerViewAdapter.ReportsViewHolder> {
    private final List<Uri> imagePath;

    public ReportsRecyclerViewAdapter(List<Uri> imagePath) {
        this.imagePath = imagePath;
    }

    @NonNull
    @Override
    public ReportsRecyclerViewAdapter.ReportsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.fragment_report_image_item, parent, false);
        return new ReportsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportsRecyclerViewAdapter.ReportsViewHolder holder, int position) {
        Transformation transformation = new RoundedCornersTransformation(100,0);
        Picasso.get().load(imagePath.get(position)).transform(transformation).resize(400,400).centerCrop().into(holder.image);
    }

    @Override
    public int getItemCount() {
        return imagePath.size();
    }

    public static class ReportsViewHolder extends RecyclerView.ViewHolder {
        ImageView image;

        public ReportsViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.report_iv_image);
        }
    }
}
