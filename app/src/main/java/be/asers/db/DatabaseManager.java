package be.asers.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import be.asers.model.Episode;
import be.asers.model.Season;
import be.asers.model.Show;

/**
 * Database manager for create/upgrade database.
 * 
 * @author chesteric31
 */
public class DatabaseManager extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "asers.db";
    private static final int DATABASE_VERSION = 5;

    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(Show.CREATE_TABLE);
        database.execSQL(Season.CREATE_TABLE);
        database.execSQL(Episode.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        String message = "Upgrading database from version %d to %d, which will destroy all old data";
        Log.w(DatabaseManager.class.getName(), String.format(message, oldVersion, newVersion));
        String dropTableIfExists = "DROP TABLE IF EXISTS %s";
        database.execSQL(String.format(dropTableIfExists, Episode.TABLE_NAME));
        database.execSQL(String.format(dropTableIfExists, Season.TABLE_NAME));
        database.execSQL(String.format(dropTableIfExists, Show.TABLE_NAME));
        onCreate(database);
    }

}
