package be.asers.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import be.asers.model.Series;

/**
 * Data Access Object for {@link Series} entity.
 * 
 * @author chesteric31
 */
public class SeriesDao extends AbstractDao {

    /**
     * Constructor.
     * 
     * @param context the {@link Context} to use
     */
    public SeriesDao(Context context) {
        super(context);
    }

    /**
     * Creates a new {@link Series} from {@link ContentValues}.
     * 
     * @param values the {@link ContentValues} to persist
     * @return the created {@link Series} in database
     */
    public Series add(ContentValues values) {
        long insertId = getDatabase().insert(Series.TABLE_NAME, null, values);
        Cursor cursor = getDatabase().query(Series.TABLE_NAME, Series.ALL_COLUMNS, Series.COLUMN_ID + " = " + insertId,
                null, null, null, null);
        cursor.moveToFirst();
        Series newSeries = retrieveSeries(cursor);
        cursor.close();
        return newSeries;
    }
    
    /**
     * Creates an {@link Series} from the {@link Cursor}.
     * 
     * @param cursor the {@link Cursor} to use
     * @return the created {@link Series}
     */
    private Series retrieveSeries(Cursor cursor) {
        if (cursor.getCount() > 0) {
            Series series = new Series();
            series.setId(cursor.getLong(cursor.getColumnIndexOrThrow(Series.COLUMN_ID)));
            series.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(Series.COLUMN_TITLE)));
            series.setTvRageId(cursor.getInt(cursor.getColumnIndexOrThrow(Series.COLUMN_TV_RAGE_ID)));
            series.setNetwork(cursor.getString(cursor.getColumnIndexOrThrow(Series.COLUMN_NETWORK)));
            String startDateTime = cursor.getString(cursor.getColumnIndexOrThrow(Series.COLUMN_START_DATE));
            String endDateTime = cursor.getString(cursor.getColumnIndexOrThrow(Series.COLUMN_END_DATE));
            SimpleDateFormat dateFormat = new SimpleDateFormat(Series.DATE_PATTERN, Locale.US);
            Date startDate;
            try {
                startDate = dateFormat.parse(startDateTime);
                series.setStartDate(startDate);
                Date endDate = dateFormat.parse(endDateTime);
                series.setEndDate(endDate);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            series.setEpisodesNumber(cursor.getInt(cursor.getColumnIndexOrThrow(Series.COLUMN_EPISODES_NUMBER)));
            series.setRunTime(cursor.getInt(cursor.getColumnIndexOrThrow(Series.COLUMN_RUN_TIME)));
            series.setCountry(cursor.getString(cursor.getColumnIndexOrThrow(Series.COLUMN_COUNTRY)));
            series.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(Series.COLUMN_STATUS)));
            return series;
        } else {
            return null;
        }
    }

    /**
     * Finds by the title.
     * 
     * @param title the title of the series to find
     * @return the found series, null if none found
     */
    public Series findByTitle(String title) {
        Series series = null;
        StringBuilder sqlBuilder = new StringBuilder("SELECT ");
        sqlBuilder.append("S." + Series.COLUMN_ID + ", ");
        sqlBuilder.append("S." + Series.COLUMN_COUNTRY + ", ");
        sqlBuilder.append("S." + Series.COLUMN_END_DATE + ", ");
        sqlBuilder.append("S." + Series.COLUMN_EPISODES_NUMBER + ", ");
        sqlBuilder.append("S." + Series.COLUMN_NETWORK + ", ");
        sqlBuilder.append("S." + Series.COLUMN_RUN_TIME + ", ");
        sqlBuilder.append("S." + Series.COLUMN_START_DATE + ", ");
        sqlBuilder.append("S." + Series.COLUMN_TITLE + ", ");
        sqlBuilder.append("S." + Series.COLUMN_TV_RAGE_ID + ", ");
        sqlBuilder.append("S." + Series.COLUMN_STATUS + " ");
        sqlBuilder.append("FROM ");
        sqlBuilder.append(Series.TABLE_NAME + " S ");
        sqlBuilder.append("WHERE ");
        sqlBuilder.append("S." + Series.COLUMN_TITLE + " LIKE '%" + title + "%'");
        Cursor cursor = getDatabase().rawQuery(sqlBuilder.toString(), null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            series = retrieveSeries(cursor);
            cursor.close();
        }
        return series;
    }

    /**
     * Finds all active series.
     * 
     * @return the found active series
     */
    public List<Series> findActiveSeries() {
        List<Series> series = new ArrayList<Series>();
        Cursor cursor = getDatabase().query(Series.TABLE_NAME, Series.ALL_COLUMNS,
                Series.COLUMN_STATUS + " = '" + Series.STATUS_ACTIVE + "'", null, null, null, null);
        while (cursor.moveToNext()) {
            series.add(retrieveSeries(cursor));
        }
        cursor.close();
        return series;
    }
    
    /**
     * Deletes all rows of the table.
     * 
     * @return the number of deleted rows
     */
    public int deleteTable() {
        return getDatabase().delete(Series.TABLE_NAME, null, null);
    }

}
