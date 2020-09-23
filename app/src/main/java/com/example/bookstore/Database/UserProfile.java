package com.example.bookstore.Database;

import android.provider.BaseColumns;


    public final class UserProfile {
        // To prevent someone from accidentally instantiating the contract class,
        // make the constructor private.
        private UserProfile() {}

        /* Inner class that defines the table contents */
        public static class Users implements BaseColumns {
            public static final String TABLE_NAME = "UserInfo";
            public static final String COLUMN_1 = "username";
            public static final String COLUMN_2 = "birthday";
            public static final String COLUMN_3 = "address";
            public static final String COLUMN_4 = "phone";
            public static final String COLUMN_5 = "password";
            public static final String COLUMN_6 = "email";
            public static final String COLUMN_7 = "type";
        }
    }

