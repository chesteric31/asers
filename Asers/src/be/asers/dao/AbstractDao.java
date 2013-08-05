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
public abstract class AbstractDao {

    private DatabaseManager databaseManager;
    private SQLiteDatabase database;

    /**
     * Constructor.
     * 
     * @param context the {@link Context} to use
     */
    public AbstractDao(Context context) {
        databaseManager = new DatabaseManager(context);
        try {
            open();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
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
    public void close() {
        databaseManager.close();
    }

    /**
     * @return the database
     */
    public SQLiteDatabase getDatabase() {
        return database;
    }

    /**
     * @param database the database to set
     */
    public void setDatabase(SQLiteDatabase database) {
        this.database = database;
    }

}
