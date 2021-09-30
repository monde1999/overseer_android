package com.adventurers.overseer.helpers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adventurers.overseer.R;
import com.adventurers.overseer.api.ReportData;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class ReportsAdapter extends RecyclerView.Adapter<ReportsAdapter.ReportsViewHolder> {
    private final List<ReportData> reports;

    public ReportsAdapter(List<ReportData> reports) {
        this.reports = reports;
    }

    @NonNull
    @Override
    public ReportsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.fragment_reports_post, parent, false);
        return new ReportsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportsViewHolder holder, int position) {
        String name = "Juan Dela Cruz " + reports.get(position).getId();
        holder.tv_name.setText(name);
        holder.tv_time_address.setText(reports.get(position).getTimestamp());
        holder.tv_caption.setText((reports.get(position).getDescription()));
        LinearLayoutManager manager = new LinearLayoutManager(holder.rv_images.getContext(), LinearLayoutManager.HORIZONTAL, false);
        holder.rv_images.setLayoutManager(manager);
        ReportsHelper.fetchImagesForReport(reports.get(position), holder.rv_images);
    }

    @Override
    public int getItemCount() {
        return reports.size();
    }

    public static class ReportsViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name;
        TextView tv_time_address;
        TextView tv_caption;
        RecyclerView rv_images;

        public ReportsViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.reports_post_tv_name);
            tv_time_address = itemView.findViewById(R.id.reports_post_tv_time_address);
            tv_caption = itemView.findViewById(R.id.reports_post_tv_caption);
            rv_images = itemView.findViewById(R.id.reports_post_rv_images);
        }
    }
}
