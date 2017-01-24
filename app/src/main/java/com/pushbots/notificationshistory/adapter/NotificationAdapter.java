package com.pushbots.notificationshistory.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pushbots.notificationshistory.R;
import com.pushbots.notificationshistory.model.PushBotsModel;

import java.util.ArrayList;

/**
 * Created by Muhammad on 1/17/2017.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationsHistoryHolder> {
    ArrayList<PushBotsModel> pushBotsModels;
    Context context;

    public NotificationAdapter(Context context, ArrayList<PushBotsModel> pushBotsModels) {
        this.pushBotsModels = pushBotsModels;
        this.context = context;
    }

    @Override
    public NotificationAdapter.NotificationsHistoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_notification_item, parent, false);
        NotificationsHistoryHolder notificationsHistoryHolder = new NotificationsHistoryHolder(v);
        return notificationsHistoryHolder;
    }

    @Override
    public void onBindViewHolder(NotificationsHistoryHolder holder, int position) {
        try {
            holder.itemName.setText(pushBotsModels.get(position).getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return pushBotsModels.size();
    }


    public static class NotificationsHistoryHolder extends RecyclerView.ViewHolder {
        TextView itemName;
        ImageView imageView;
        CardView cardView;

        public NotificationsHistoryHolder(View itemView) {
            super(itemView);
            itemName = (TextView) itemView.findViewById(R.id.itemName);
            imageView = (ImageView) itemView.findViewById(R.id.notificationImage);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
        }
    }
}
