package com.pushbots.notificationshistory.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Muhammad on 1/17/2017.
 */

public class PushBotsModel implements Parcelable {
    private String date;

    public PushBotsModel() {

    }

    protected PushBotsModel(Parcel in) {
        date = in.readString();
        message = in.readString();
        notificationID = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(date);
        dest.writeString(message);
        dest.writeString(notificationID);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PushBotsModel> CREATOR = new Creator<PushBotsModel>() {
        @Override
        public PushBotsModel createFromParcel(Parcel in) {
            return new PushBotsModel(in);
        }

        @Override
        public PushBotsModel[] newArray(int size) {
            return new PushBotsModel[size];
        }
    };

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNotificationID() {
        return notificationID;
    }

    public void setNotificationID(String notificationID) {
        this.notificationID = notificationID;
    }

    private String message;
    private String notificationID;


}
