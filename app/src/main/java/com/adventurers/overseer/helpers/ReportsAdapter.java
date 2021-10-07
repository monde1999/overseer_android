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
import com.adventurers.overseer.api.ReportReactData;

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
        int reportId = reports.get(position).getId();
        int userId = reports.get(position).getUser().getId();
        holder.tv_name.setText(name);
        holder.tv_time_address.setText(reports.get(position).getTimestamp());
        holder.tv_caption.setText((reports.get(position).getDescription()));
        LinearLayoutManager manager = new LinearLayoutManager(holder.rv_images.getContext(), LinearLayoutManager.HORIZONTAL, false);
        holder.rv_images.setLayoutManager(manager);
        ReportsHelper.fetchReactionForReport(reportId, userId, holder);
        ReportsHelper.fetchReactionsCountForReport(reportId, holder.tv_likes, holder.tv_dislikes);
        ReportsHelper.fetchImagesForReport(reportId, holder.rv_images);

        holder.btn_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.toggleLikeDislike(true);
                ReportReactData reportReact  = new ReportReactData(reports.get(holder.getAdapterPosition()).getId(),reports.get(holder.getAdapterPosition()).getUser().getId(),true);
                ReportsHelper.postReportReaction(reportReact);
            }
        });
        holder.btn_dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.toggleLikeDislike(false);
                ReportReactData reportReact  = new ReportReactData(reports.get(holder.getAdapterPosition()).getId(),reports.get(holder.getAdapterPosition()).getUser().getId(),false);
                ReportsHelper.postReportReaction(reportReact);
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

        public void toggleLikeDislike(boolean isLike) {
            switch (selected) {
                case 0:
                    if(isLike) {
                        switchLike(true);
                        selected = 1;
                    }
                    else {
                        switchDislike(true);
                        selected = -1;
                    }
                    break;
                case 1:
                    if(isLike) {
                        switchLike(false);
                        selected = 0;
                    }
                    else {
                        switchLike(false);
                        switchDislike(true);
                        selected = -1;
                    }
                    break;
                case -1:
                    if(isLike) {
                        switchDislike(false);
                        switchLike(true);
                        selected = 1;
                    }
                    else {
                        switchDislike(false);
                        selected = 0;
                    }
                    break;
            }
        }

        private void switchLike(boolean isOn) {
            if(isOn) {
                btn_like.setBackgroundResource(R.drawable.bg_btn_main_fill);
                btn_like.setImageResource(R.drawable.ic_thumbs_up_20dp_white);
            }
            else {
                btn_like.setBackgroundResource(R.drawable.bg_btn_grey_border);
                btn_like.setImageResource(R.drawable.ic_thumbs_up_20dp_grey);
            }
        }

        private void switchDislike(boolean isOn) {
            if(isOn) {
                btn_dislike.setBackgroundResource(R.drawable.bg_btn_main_fill);
                btn_dislike.setImageResource(R.drawable.ic_thumbs_down_20dp_white);
            }
            else {
                btn_dislike.setBackgroundResource(R.drawable.bg_btn_grey_border);
                btn_dislike.setImageResource(R.drawable.ic_thumbs_down_20dp_grey);
            }
        }
    }
}
