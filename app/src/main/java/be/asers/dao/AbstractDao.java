package be.asers.dao;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import be.asers.db.DatabaseManager;

/**
 * Abstract class for Data Access Objects.
 * 
 * @author chesteric31
 */
abstract class AbstractDao {

    /** The database manager. */
    private DatabaseManager databaseManager;
    
    /** The database. */
    private SQLiteDatabase database;

    /**
     * Constructor.
     * 
     * @param context the {@link Context} to use
     */
    AbstractDao(Context context) {
        databaseManager = new DatabaseManager(context);
        try {
            open();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Opens the database.
     * 
     * @throws SQLException a {@link SQLException} if an error occurred
     */
    private void open() throws SQLException {
        database = databaseManager.getWritableDatabase();
    }

    /**
     * Closes the database.
     */
    void close() {
        databaseManager.close();
    }

    /**
     * Gets the database.
     *
     * @return the database
     */
    public SQLiteDatabase getDatabase() {
        return database;
    }

    /**
     * Sets the database.
     *
     * @param database the database to set
     */
    public void setDatabase(SQLiteDatabase database) {
        this.database = database;
    }

}
