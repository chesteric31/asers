package be.asers.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import be.asers.model.Season;

/**
 * Data Access Object for Episode entity.
 * 
 * @author chesteric31
 */
public class SeasonDao extends AbstractDao {

    /**
     * Constructor.
     * 
     * @param context the {@link Context} to use
     */
    public SeasonDao(Context context) {
        super(context);
    }

    /**
     * Creates a new {@link Season} from {@link ContentValues}.
     * 
     * @param values the {@link ContentValues} to persist
     * @return the created {@link Season} in database
     */
    public Season add(ContentValues values) {
        long insertId = getDatabase().insert(Season.TABLE_NAME, null, values);
        Cursor cursor = getDatabase().query(Season.TABLE_NAME, Season.ALL_COLUMNS, Season.COLUMN_ID + " = " + insertId,
                null, null, null, null);
        cursor.moveToFirst();
        Season newSeason = retrieveSeason(cursor);
        cursor.close();
        return newSeason;
    }

    /**
     * Creates an {@link Season} from the {@link Cursor}.
     * 
     * @param cursor the {@link Cursor} to use
     * @return the created {@link Season}
     */
    private Season retrieveSeason(Cursor cursor) {
        if (cursor.getCount() > 0) {
            Season season = new Season();
            season.setId(cursor.getLong(cursor.getColumnIndexOrThrow(Season.COLUMN_ID)));
            season.setNumber(cursor.getInt(cursor.getColumnIndexOrThrow(Season.COLUMN_NUMBER)));
            return season;
        } else {
            return null;
        }
    }

    /**
     * Finds season by Serie id.
     * 
     * @param id the Serie id
     * @return the found seasons, empty list if none found
     */
    public List<Season> findBySerieId(Long id) {
        List<Season> seasons = new ArrayList<Season>();
        StringBuilder builder = new StringBuilder("SELECT ");
        builder.append("S." + Season.COLUMN_ID + ", ");
        builder.append("S." + Season.COLUMN_NUMBER + ", ");
        builder.append("S." + Season.COLUMN_SERIES + " ");
        builder.append("FROM ");
        builder.append(Season.TABLE_NAME + " S ");
        builder.append("WHERE ");
        builder.append("S." + Season.COLUMN_SERIES + " = " + id);
        Cursor cursor = getDatabase().rawQuery(builder.toString(), null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                seasons.add(retrieveSeason(cursor));
                cursor.moveToNext();
            }
            // Make sure to close the cursor
            cursor.close();
        }
        return seasons;
    }

    /**
     * Find by id.
     *
     * @param id the id
     * @return the season
     */
    public Season findById(Long id) {
        Season season = null;
        StringBuilder builder = new StringBuilder("SELECT ");
        builder.append("S." + Season.COLUMN_ID + ", ");
        builder.append("S." + Season.COLUMN_NUMBER + ", ");
        builder.append("S." + Season.COLUMN_SERIES + " ");
        builder.append("FROM ");
        builder.append(Season.TABLE_NAME + " S ");
        builder.append("WHERE ");
        builder.append("S." + Season.COLUMN_ID + " = " + id);
        Cursor cursor = getDatabase().rawQuery(builder.toString(), null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                season = retrieveSeason(cursor);
                cursor.moveToNext();
            }
            // Make sure to close the cursor
            cursor.close();
        }
        return season;
    }

    /**
     * Update.
     *
     * @param contentValues the content values
     * @param id the id
     */
    public void update(ContentValues contentValues, Long id) {
        getDatabase().update(Season.TABLE_NAME, contentValues, "_id=" + id, null);
    }

}
