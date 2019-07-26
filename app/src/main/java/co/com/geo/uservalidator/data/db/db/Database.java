package co.com.geo.uservalidator.data.db.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import co.com.geo.uservalidator.data.db.dao.schema.IIntentEntitySchema;
import co.com.geo.uservalidator.data.db.dao.schema.IUserEntitySchema;

public class Database {
    private static final String TAG = "MyDatabase";
    private static final String DATABASE_NAME = "users.db";
    private DatabaseHelper mDbHelper;
    // Increment DB Version on any schema change
    private static final int DATABASE_VERSION = 6;
    private final Context mContext;
    private SQLiteDatabase mDb;



    public Database open() throws SQLException {
        mDbHelper = new DatabaseHelper(mContext);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public SQLiteDatabase getWritableDatabase() {
        return mDb;
    }

    public void close() {
        mDbHelper.close();
    }

    public Database(Context context) {
        this.mContext = context;
    }


    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(IUserEntitySchema.USER_TABLE_CREATE);
            db.execSQL(IIntentEntitySchema.INTENT_TABLE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion,
                              int newVersion) {
            Log.w(TAG, "Upgrading database from version "
                    + oldVersion + " to "
                    + newVersion + " which destroys all old data");

            db.execSQL("DROP TABLE IF EXISTS "
                    + IUserEntitySchema.USER_TABLE);
            db.execSQL("DROP TABLE IF EXISTS "
                    + IIntentEntitySchema.INTENT_TABLE);
            onCreate(db);

        }
    }

}
