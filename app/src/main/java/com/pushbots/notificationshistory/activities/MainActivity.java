package com.pushbots.notificationshistory.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.pushbots.notificationshistory.CustomHandler;
import com.pushbots.notificationshistory.R;
import com.pushbots.notificationshistory.adapter.NotificationAdapter;
import com.pushbots.notificationshistory.storage.Database;
import com.pushbots.notificationshistory.utils.Constants;
import com.pushbots.push.Pushbots;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    RecyclerView notificationsRecycler;
    Database database;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        notificationsRecycler = (RecyclerView) findViewById(R.id.notificationsRecycler);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        notificationsRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        notificationsRecycler.setHasFixedSize(true);
        Pushbots.sharedInstance().setCustomHandler(CustomHandler.class);
        Pushbots.sharedInstance().registerForRemoteNotifications();
    }

    @Override
    protected void onStart() {
        super.onStart();
        database = new Database(this);
        swipeRefreshLayout.setRefreshing(true);
        fillNotificationsData();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        }, Constants.DELAY_TIME);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.delete:
                database.deleteNotifications();
                fillNotificationsData();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRefresh() {
        fillNotificationsData();
    }

    private void fillNotificationsData() {
        NotificationAdapter notificationAdapter = new NotificationAdapter(this, database.readNotificationData());
        notificationAdapter.notifyDataSetChanged();
        notificationsRecycler.setAdapter(notificationAdapter);
        swipeRefreshLayout.setRefreshing(false);
    }
}
