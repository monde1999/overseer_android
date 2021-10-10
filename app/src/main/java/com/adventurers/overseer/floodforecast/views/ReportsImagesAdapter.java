package com.adventurers.overseer.floodforecast.views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adventurers.overseer.R;
import com.adventurers.overseer.api.ReportImage;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class ReportsImagesAdapter extends RecyclerView.Adapter<ReportsImagesAdapter.ReportsViewHolder> {
    private final List<ReportImage> images;
    private View view;

    public ReportsImagesAdapter(List<ReportImage> images) {
        this.images = images;
    }

    @NonNull
    @Override
    public ReportsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.fragment_reports_post_image, parent, false);
        return new ReportsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportsViewHolder holder, int position) {
        Transformation transformation = new RoundedCornersTransformation(100,0);
        Picasso.get().load(images.get(position).getImage()).transform(transformation).resize(400,400).centerCrop().into(holder.iv_image);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public static class ReportsViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_image;

        public ReportsViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_image = itemView.findViewById(R.id.reports_post_iv_image);
        }
    }
}
