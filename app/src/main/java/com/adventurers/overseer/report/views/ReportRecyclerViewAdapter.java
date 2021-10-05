package com.adventurers.overseer.report.views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adventurers.overseer.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.File;
import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class ReportRecyclerViewAdapter extends RecyclerView.Adapter<ReportRecyclerViewAdapter.ReportsViewHolder> {
    private final List<File> mImageFiles;

    public ReportRecyclerViewAdapter(List<File> imageFiles) {
        mImageFiles = imageFiles;
    }

    @NonNull
    @Override
    public ReportRecyclerViewAdapter.ReportsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.fragment_report_image_item, parent, false);
        return new ReportsViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ReportRecyclerViewAdapter.ReportsViewHolder holder, int position) {
        Transformation transformation = new RoundedCornersTransformation(100,0);
        Picasso.get().load(mImageFiles.get(position)).transform(transformation).resize(400,400).centerCrop().into(holder.image);
    }
    @Override
    public int getItemCount() {
        return mImageFiles.size();
    }

    public static class ReportsViewHolder extends RecyclerView.ViewHolder {
        ImageView image;

        public ReportsViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.report_iv_image);
        }
    }

    public List<File> getImageFiles() {
        return mImageFiles;
    }
}
