package com.adventurers.overseer.helpers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adventurers.overseer.R;
import com.adventurers.overseer.api.ReportData;

import java.util.List;

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
        String name = reports.get(position).getUser().getFirst_name() + " " + reports.get(position).getUser().getLast_name();
        holder.tv_name.setText(name);
        holder.tv_time_address.setText(reports.get(position).getTimestamp());
        holder.tv_caption.setText((reports.get(position).getDescription()));
        LinearLayoutManager manager = new LinearLayoutManager(holder.rv_images.getContext(), LinearLayoutManager.HORIZONTAL, false);
        holder.rv_images.setLayoutManager(manager);
        ReportsHelper.fetchReactionsCountForReport(reports.get(position).getId());
        ReportsHelper.fetchImagesForReport(reports.get(position).getId(), holder.rv_images);

        holder.btn_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (holder.selected) {
                    case 0:
                        // Activate like
                        holder.btn_like.setBackgroundResource(R.drawable.bg_btn_main_fill);
                        holder.btn_like.setImageResource(R.drawable.ic_thumbs_up_20dp_white);
                        holder.selected = 1;
                        break;
                    case 1:
                        // Deactivate like
                        holder.btn_like.setBackgroundResource(R.drawable.bg_btn_grey_border);
                        holder.btn_like.setImageResource(R.drawable.ic_thumbs_up_20dp_grey);
                        holder.selected = 0;
                        break;
                    case -1:
                        // Deactivate dislike
                        holder.btn_dislike.setBackgroundResource(R.drawable.bg_btn_grey_border);
                        holder.btn_dislike.setImageResource(R.drawable.ic_thumbs_down_20dp_grey);

                        // Activate like
                        holder.btn_like.setBackgroundResource(R.drawable.bg_btn_main_fill);
                        holder.btn_like.setImageResource(R.drawable.ic_thumbs_up_20dp_white);
                        holder.selected = 1;
                        break;
                }
            }
        });
        holder.btn_dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (holder.selected) {
                    case 0:
                        // Activate dislike
                        holder.btn_dislike.setBackgroundResource(R.drawable.bg_btn_main_fill);
                        holder.btn_dislike.setImageResource(R.drawable.ic_thumbs_down_20dp_white);
                        holder.selected = -1;
                        break;
                    case 1:
                        // Deactivate like
                        holder.btn_like.setBackgroundResource(R.drawable.bg_btn_grey_border);
                        holder.btn_like.setImageResource(R.drawable.ic_thumbs_up_20dp_grey);

                        // Activate dislike
                        holder.btn_dislike.setBackgroundResource(R.drawable.bg_btn_main_fill);
                        holder.btn_dislike.setImageResource(R.drawable.ic_thumbs_down_20dp_white);
                        holder.selected = -1;
                        break;
                    case -1:
                        // Deactivate dislike
                        holder.btn_dislike.setBackgroundResource(R.drawable.bg_btn_grey_border);
                        holder.btn_dislike.setImageResource(R.drawable.ic_thumbs_down_20dp_grey);
                        holder.selected = 0;
                        break;
                }
            }
        });
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
        TextView tv_likes;
        TextView tv_dislikes;
        ImageButton btn_like;
        ImageButton btn_dislike;
        int selected;   // 0 = neutral, 1 = like, -1 = dislike

        public ReportsViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.reports_post_tv_name);
            tv_time_address = itemView.findViewById(R.id.reports_post_tv_time_address);
            tv_caption = itemView.findViewById(R.id.reports_post_tv_caption);
            rv_images = itemView.findViewById(R.id.reports_post_rv_images);
            tv_likes = itemView.findViewById(R.id.reports_post_tv_likes);
            tv_dislikes = itemView.findViewById(R.id.reports_post_tv_dislikes);
            btn_like = itemView.findViewById(R.id.reports_post_btn_like);
            btn_dislike = itemView.findViewById(R.id.reports_post_btn_dislike);
            selected = 0;
        }
    }
}
