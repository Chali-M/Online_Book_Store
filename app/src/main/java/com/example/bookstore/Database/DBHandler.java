package com.example.bookstore.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.service.autofill.AutofillService;

import java.util.ArrayList;
import java.util.List;


public class DBHandler  extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "book.db";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + UserProfile.Users.TABLE_NAME + " (" +
                    UserProfile.Users._ID + " INTEGER PRIMARY KEY," +
                    UserProfile.Users.COLUMN_1 + " TEXT," +
                    UserProfile.Users.COLUMN_2 + " TEXT," +
                    UserProfile.Users.COLUMN_3 + " TEXT," +
                    UserProfile.Users.COLUMN_4 + " TEXT," +
                    UserProfile.Users.COLUMN_5 + " TEXT," +
                    UserProfile.Users.COLUMN_6 + " TEXT," +
                    UserProfile.Users.COLUMN_7 + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + UserProfile.Users.TABLE_NAME;


    public long addInfo(String username, String birth, String address, String phone, String pw, String email, String type) {

        // Gets the data repository in write mode
        SQLiteDatabase db = getWritableDatabase();
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(UserProfile.Users.COLUMN_1, username);
        values.put(UserProfile.Users.COLUMN_2, birth);
        values.put(UserProfile.Users.COLUMN_3, address);
        values.put(UserProfile.Users.COLUMN_4, phone);
        values.put(UserProfile.Users.COLUMN_5, pw);
        values.put(UserProfile.Users.COLUMN_6, email);
        values.put(UserProfile.Users.COLUMN_7, type);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(UserProfile.Users.TABLE_NAME, null, values);
        return newRowId;
    }


    public Boolean updateInfo(String username, String birth, String address, String phone, String pw, String email, String type) {

        SQLiteDatabase db = getWritableDatabase();

        // New value for one column

        ContentValues values = new ContentValues();
        values.put(UserProfile.Users.COLUMN_2, birth);
        values.put(UserProfile.Users.COLUMN_3, address);
        values.put(UserProfile.Users.COLUMN_4, phone);
        values.put(UserProfile.Users.COLUMN_5, pw);
        values.put(UserProfile.Users.COLUMN_6, email);
        values.put(UserProfile.Users.COLUMN_6, type);

        // Which row to update, based on the title
        String selection = UserProfile.Users.COLUMN_1 + " LIKE ?";
        String[] selectionArgs = {username};

        int count = db.update(
                UserProfile.Users.TABLE_NAME,
                values,
                selection,
                selectionArgs);

        if (count >= 1) {
            return true;
        } else {
            return false;
        }
    }

       public void deleteInfo (String username){

           SQLiteDatabase db = getWritableDatabase();

           // Define 'where' part of query.
           String selection = UserProfile.Users.COLUMN_1 + " LIKE ?";
          // Specify arguments in placeholder order.
           String[] selectionArgs = { username };
          // Issue SQL statement.
           int deletedRows = db.delete(UserProfile.Users.TABLE_NAME, selection, selectionArgs);


       }


           public List readAllInfo(){
                String username = "avinash";
               SQLiteDatabase db = getReadableDatabase();

            // Define a projection that specifies which columns from the database
            // you will actually use after this query.
               String[] projection = {
                       BaseColumns._ID,
                       UserProfile.Users.COLUMN_1,
                       UserProfile.Users.COLUMN_2,
                       UserProfile.Users.COLUMN_3,
                       UserProfile.Users.COLUMN_4,
                       UserProfile.Users.COLUMN_5,
                       UserProfile.Users.COLUMN_6,
                       UserProfile.Users.COLUMN_7
               };

              // Filter results WHERE "title" = 'My Title'
               String selection = UserProfile.Users.COLUMN_1 + " = ?";
               String[] selectionArgs = { username };

                // How you want the results sorted in the resulting Cursor
               String sortOrder =
                       UserProfile.Users.COLUMN_1 + " ASC";

               Cursor cursor = db.query(
                       UserProfile.Users.TABLE_NAME,   // The table to query
                       projection,             // The array of columns to return (pass null to get all)
                       null,              // The columns for the WHERE clause
                       null,          // The values for the WHERE clause
                       null,                   // don't group the rows
                       null,                   // don't filter by row groups
                       sortOrder               // The sort order
               );

               List usernames = new ArrayList<>();
               while(cursor.moveToNext()) {
                   String user = cursor.getString(cursor.getColumnIndexOrThrow(UserProfile.Users.COLUMN_1));
                   usernames.add(user);
               }
               cursor.close();
                return usernames;

           }

    public List readAllInfo( String username){

        SQLiteDatabase db = getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                BaseColumns._ID,
                UserProfile.Users.COLUMN_1,
                UserProfile.Users.COLUMN_2,
                UserProfile.Users.COLUMN_3,
                UserProfile.Users.COLUMN_4,
                UserProfile.Users.COLUMN_5,
                UserProfile.Users.COLUMN_6,
                UserProfile.Users.COLUMN_7
        };

        // Filter results WHERE "title" = 'My Title'
        String selection = UserProfile.Users.COLUMN_1 + " LIKE ?";
        String[] selectionArgs = { username };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                UserProfile.Users.COLUMN_1 + " ASC";

        Cursor cursor = db.query(
                UserProfile.Users.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        List userInfo = new ArrayList<>();
        while(cursor.moveToNext()) {
            String user = cursor.getString(cursor.getColumnIndexOrThrow(UserProfile.Users.COLUMN_1));
            String birthday = cursor.getString(cursor.getColumnIndexOrThrow(UserProfile.Users.COLUMN_2));
            String address= cursor.getString(cursor.getColumnIndexOrThrow(UserProfile.Users.COLUMN_3));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow(UserProfile.Users.COLUMN_4));
            String password= cursor.getString(cursor.getColumnIndexOrThrow(UserProfile.Users.COLUMN_5));
            String email = cursor.getString(cursor.getColumnIndexOrThrow(UserProfile.Users.COLUMN_6));
            String type = cursor.getString(cursor.getColumnIndexOrThrow(UserProfile.Users.COLUMN_7));

            userInfo.add(user);
            userInfo.add(birthday);
            userInfo.add(address);
            userInfo.add(phone);
            userInfo.add(password);
            userInfo.add(email);
            userInfo.add(type);

        }
        cursor.close();
        return userInfo;

    }

             public Boolean loginUser (String username,String password){


                 SQLiteDatabase db = getReadableDatabase();

                     // Define a projection that specifies which columns from the database
                   // you will actually use after this query.
                 String[] projection = {
                         BaseColumns._ID,
                         UserProfile.Users.COLUMN_1,
                         UserProfile.Users.COLUMN_5
                 };

                    // Filter results WHERE "title" = 'My Title'
                 String selection = UserProfile.Users.COLUMN_1 + " = ? AND "+ UserProfile.Users.COLUMN_5 + "= ?";
                 String[] selectionArgs = { username,password };

                    // How you want the results sorted in the resulting Cursor
                 String sortOrder =
                         UserProfile.Users.COLUMN_1 + " ASC";

                 Cursor cursor = db.query(
                         UserProfile.Users.TABLE_NAME,   // The table to query
                         projection,             // The array of columns to return (pass null to get all)
                         selection,              // The columns for the WHERE clause
                         selectionArgs,          // The values for the WHERE clause
                         null,                   // don't group the rows
                         null,                   // don't filter by row groups
                         sortOrder               // The sort order
                 );

                 List validUser = new ArrayList();
                 while(cursor.moveToNext()) {
                     String User = cursor.getString(cursor.getColumnIndexOrThrow(UserProfile.Users.COLUMN_1));
                     validUser.add(User);
                 }
                 cursor.close();

                 if(validUser.isEmpty()){
                     return false;
                 }
                 else {
                     return true;
                 }
             }



}




