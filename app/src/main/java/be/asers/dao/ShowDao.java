package be.asers.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import be.asers.model.Show;

/**
 * Data Access Object for {@link Show} entity.
 * 
 * @author chesteric31
 */
public class ShowDao extends AbstractDao {
    
    /**
     * Constructor.
     * 
     * @param context the {@link Context} to use
     */
    public ShowDao(Context context) {
        super(context);
    }

    /**
     * Creates a new {@link Show} from {@link ContentValues}.
     * 
     * @param values the {@link ContentValues} to persist
     * @return the created {@link Show} in database
     */
    public Show add(ContentValues values) {
        long insertId = getDatabase().insert(Show.TABLE_NAME, null, values);
        Cursor cursor = getDatabase().query(Show.TABLE_NAME, Show.ALL_COLUMNS, Show.COLUMN_ID + " = " + insertId,
                null, null, null, null);
        cursor.moveToFirst();
        Show newShow = retrieveShow(cursor);
        cursor.close();
        return newShow;
    }

    /**
     * Creates an {@link Show} from the {@link Cursor}.
     * 
     * @param cursor the {@link Cursor} to use
     * @return the created {@link Show}
     */
    private Show retrieveShow(Cursor cursor) {
        if (cursor.getCount() > 0) {
            Show show = new Show();
            show.setId(cursor.getLong(cursor.getColumnIndexOrThrow(Show.COLUMN_ID)));
            show.setName(cursor.getString(cursor.getColumnIndexOrThrow(Show.COLUMN_NAME)));
            show.setTvRageId(cursor.getInt(cursor.getColumnIndexOrThrow(Show.COLUMN_TV_RAGE_ID)));
            show.setTvMazeId(cursor.getInt(cursor.getColumnIndexOrThrow(Show.COLUMN_TV_MAZE_ID)));
            show.setNetwork(cursor.getString(cursor.getColumnIndexOrThrow(Show.COLUMN_NETWORK)));
            show.setRunTime(cursor.getInt(cursor.getColumnIndexOrThrow(Show.COLUMN_RUN_TIME)));
            show.setCountry(cursor.getString(cursor.getColumnIndexOrThrow(Show.COLUMN_COUNTRY)));
            show.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(Show.COLUMN_STATUS)));
            show.setCast(cursor.getString(cursor.getColumnIndexOrThrow(Show.COLUMN_IMAGE)));
            return show;
        } else {
            return null;
        }
    }

    /**
     * Finds by the title.
     * 
     * @param name the title of the series to find
     * @return the found series, null if none found
     */
    public Show findByName(String name) {
        Show show = null;
        name = name.replaceAll("'", "''");
        StringBuilder builder = new StringBuilder("SELECT ");
        builder.append("S." + Show.COLUMN_ID + ", ");
        builder.append("S." + Show.COLUMN_COUNTRY + ", ");
        builder.append("S." + Show.COLUMN_NETWORK + ", ");
        builder.append("S." + Show.COLUMN_RUN_TIME + ", ");
        builder.append("S." + Show.COLUMN_NAME + ", ");
        builder.append("S." + Show.COLUMN_TV_RAGE_ID + ", ");
        builder.append("S." + Show.COLUMN_TV_MAZE_ID + ", ");
        builder.append("S." + Show.COLUMN_STATUS + ", ");
        builder.append("S." + Show.COLUMN_IMAGE + " ");
        builder.append("FROM ");
        builder.append(Show.TABLE_NAME + " S ");
        builder.append("WHERE ");
        builder.append("S." + Show.COLUMN_NAME + " LIKE '%" + name + "%'");
        Cursor cursor = getDatabase().rawQuery(builder.toString(), null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            show = retrieveShow(cursor);
            cursor.close();
        }
        return show;
    }

    /**
     * Finds all active shows.
     * 
     * @return the found active shows
     */
    public List<Show> findActiveShows() {
        List<Show> shows = new ArrayList<Show>();
        Cursor cursor = getDatabase().query(Show.TABLE_NAME, Show.ALL_COLUMNS,
                Show.COLUMN_STATUS + " = '" + Show.STATUS_ACTIVE + "'", null, null, null, null);
        while (cursor.moveToNext()) {
            shows.add(retrieveShow(cursor));
        }
        cursor.close();
        return shows;
    }

    /**
     * Deletes all rows of the table.
     * 
     * @return the number of deleted rows
     */
    public int deleteTable() {
        return getDatabase().delete(Show.TABLE_NAME, null, null);
    }

    /**
     * Updates the {@link Show}.
     * 
     * @param contentValues the {@link ContentValues} to use
     * @param id the id of the series to update
     */
    public void update(ContentValues contentValues, Long id) {
        getDatabase().update(Show.TABLE_NAME, contentValues, "_id=" + id, null);
    }

}
