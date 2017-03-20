package com.pushbots.notificationshistory;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.pushbots.notificationshistory.storage.Database;
import com.pushbots.push.Pushbots;
import com.pushbots.push.utils.PBConstants;

/**
 * Created by Muhammad on 1/17/2017.
 */

public class CustomHandler extends BroadcastReceiver {
    private String TAG = "PB3";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d(TAG, "action=" + action);
        if (action.equals(PBConstants.EVENT_MSG_RECEIVE)) {
            Database database = new Database(context);
            database.insertNotificationData(intent.getBundleExtra(PBConstants.EVENT_MSG_RECEIVE));
            Log.e("image", intent.getBundleExtra(PBConstants.EVENT_MSG_RECEIVE).getString("image"));
        }
        // Handle Push Message when opened
        if (action.equals(PBConstants.EVENT_MSG_OPEN)) {


            //Bundle containing all fields of the opened notification
            Bundle bundle = intent.getExtras().getBundle(PBConstants.EVENT_MSG_OPEN);

            //Record opened notification
            Pushbots.PushNotificationOpened(context, bundle);

            Log.i(TAG, "User clicked notification with Message: " + bundle.get("message"));

            //Get Custom field key e.g. article_id
            if (bundle.get("article_id") != null)
                Log.i(TAG, "Article Id: " + bundle.get("article_id"));

            //Start Launch Activity
            String packageName = context.getPackageName();
            Intent resultIntent = new Intent(context.getPackageManager().getLaunchIntentForPackage(packageName));
            resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            // Check for next activity
            String next_activity = bundle.getString("nextActivity");
            if (null != next_activity) {
                try {
                    Log.i(TAG, "Opening Custom Activity " + next_activity);
                    resultIntent = new Intent(context, Class.forName(next_activity));
                    resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                } catch (ClassNotFoundException e) {
                    //ClassNotFound
                    e.printStackTrace();
                }
            }

            // Check for open URL
            String open_url = bundle.getString("openURL");
            if (null != open_url && (open_url.startsWith("http://") || open_url.startsWith("https://"))) {
                resultIntent = new Intent("android.intent.action.VIEW", Uri.parse(open_url));
                resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                Log.d(TAG, "Opening url: " + open_url);
            }

            resultIntent.putExtras(intent.getBundleExtra("pushData"));

            //Open activity or URL with pushData.
            if (null != resultIntent) {
                context.startActivity(resultIntent);
            }

        } else if (action.equals(PBConstants.EVENT_MSG_RECEIVE)) {

            //Bundle containing all fields of the notification
            Bundle bundle = intent.getExtras().getBundle(PBConstants.EVENT_MSG_RECEIVE);
            Log.i(TAG, "User received notification with Message: " + bundle.get("message"));

        }
    }
}
