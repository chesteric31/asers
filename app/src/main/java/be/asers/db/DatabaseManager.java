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

    /** The Constant DATABASE_NAME. */
    private static final String DATABASE_NAME = "asers.db";
    
    /** The Constant DATABASE_VERSION. */
    private static final int DATABASE_VERSION = 4;

    /**
     * Constructor.
     * 
     * @param context the {@link Context} to use
     */
    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(Show.CREATE_TABLE);
        database.execSQL(Season.CREATE_TABLE);
        database.execSQL(Episode.CREATE_TABLE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        String message = "Upgrading database from version " + oldVersion + " to " + newVersion
                + ", which will destroy all old data";
        Log.w(DatabaseManager.class.getName(), message);
        String dropTable = "DROP TABLE IF EXISTS ";
        database.execSQL(dropTable + Episode.TABLE_NAME);
        database.execSQL(dropTable + Season.TABLE_NAME);
        database.execSQL(dropTable + Show.TABLE_NAME);
        onCreate(database);
    }

}