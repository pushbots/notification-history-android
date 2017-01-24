package com.pushbots.notificationshistory.storage;

import android.provider.BaseColumns;

/**
 * Created by Muhammad on 9/27/2016.
 */

public final class DatabaseContract {
        // To prevent someone from accidentally instantiating the contract class,
        // make the constructor private.
        private DatabaseContract() {}

        /* Inner class that defines the table contents */
        public static class DataEntry implements BaseColumns {
            public static final String TABLE_NAME = "notifications";
            public static final String COLUMN_INDEX = "index";
            public static final String COLUMN_NOTIFICATION_ID = "nID";
            public static final String COLUMN_MESSAGE = "message";
            public static final String COLUMN_DATE= "sent_time";



        }
}
