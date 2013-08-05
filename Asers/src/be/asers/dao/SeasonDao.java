package be.asers.dao;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import be.asers.db.DatabaseManager;
import be.asers.model.Episode;
import be.asers.model.Season;
import be.asers.model.Series;

/**
 * Data Access Object for Episode entity.
 *
 * @author chesteric31
 */
public class SeasonDao {

    private SQLiteDatabase database;
    private DatabaseManager databaseManager;

    /**
     * Constructor.
     * 
     * @param context the {@link Context} to use
     */
    public SeasonDao(Context context) {
        databaseManager = new DatabaseManager(context);
    }

    /**
     * Opens the database.
     * 
     * @throws SQLException a {@link SQLException} if an error occurred
     */
    public void open() throws SQLException {
        database = databaseManager.getWritableDatabase();
    }

    /**
     * Closes the database.
     */
    public void close() {
        databaseManager.close();
    }

//    public Episode createEpisode(String comment) {
//      ContentValues values = new ContentValues();
//      values.put(MySQLiteHelper.COLUMN_COMMENT, comment);
//      long insertId = database.insert(MySQLiteHelper.TABLE_COMMENTS, null,
//          values);
//      Cursor cursor = database.query(MySQLiteHelper.TABLE_COMMENTS,
//          allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
//          null, null, null);
//      cursor.moveToFirst();
//      Comment newComment = cursorToEpisode(cursor);
//      cursor.close();
//      return newComment;
//    }

//    public void deleteComment(Comment comment) {
//      long id = comment.getId();
//      System.out.println("Comment deleted with id: " + id);
//      database.delete(MySQLiteHelper.TABLE_COMMENTS, MySQLiteHelper.COLUMN_ID
//          + " = " + id, null);
//    }

    /**
     * Finds all the episodes for a {@link Series} and a {@link Season}.
     * 
     * @param series the {@link Series} to use
     * @param season the {@link Season} to use
     * @return all the {@link Episode} for a {@link Series} and a {@link Season}
     */
    public List<Episode> findAllForSeriesSeason(Series series, Season season) {
      List<Episode> episodes = new ArrayList<Episode>();

      StringBuilder sqlBuilder = new StringBuilder("SELECT ");
      sqlBuilder.append("E." + Episode.COLUMN_ID + ", ");
      sqlBuilder.append("E." + Episode.COLUMN_NUMBER + ", ");
      sqlBuilder.append("E." + Episode.COLUMN_EPISODE + ", ");
      sqlBuilder.append("E." + Episode.COLUMN_PRODUCTION_CODE + ", ");
      sqlBuilder.append("E." + Episode.COLUMN_AIR_DATE + ", ");
      sqlBuilder.append("E." + Episode.COLUMN_TITLE + ", ");
      sqlBuilder.append("E." + Episode.COLUMN_SPECIAL + ", ");
      sqlBuilder.append("E." + Episode.COLUMN_TV_RAGE_LINK + ", ");
      sqlBuilder.append("E." + Episode.COLUMN_SEASON);
      sqlBuilder.append("FROM ");
      sqlBuilder.append(Episode.TABLE_NAME + " E, ");
      sqlBuilder.append(Series.TABLE_NAME + " S, ");
      sqlBuilder.append(Season.TABLE_NAME + "SE ");
      sqlBuilder.append("WHERE ");
      sqlBuilder.append("E." + Episode.COLUMN_SEASON + " = S." + Season.COLUMN_ID);
      sqlBuilder.append("S." + Season.COLUMN_SERIES + " = SE." + Series.COLUMN_ID);
      Cursor cursor = database.rawQuery(sqlBuilder.toString(), null);

      cursor.moveToFirst();
      while (!cursor.isAfterLast()) {
        Episode episode = retrieveEpisode(cursor);
        episodes.add(episode);
        cursor.moveToNext();
      }
      // Make sure to close the cursor
      cursor.close();
      return episodes;
    }

    /**
     * Create an {@link Episode} from the {@link Cursor}.
     *  
     * @param cursor the {@link Cursor} to use
     * @return the created {@link Episode}
     */
    private Episode retrieveEpisode(Cursor cursor) {
      Episode episode = new Episode();
      episode.setId(cursor.getLong(0));
      episode.setNumber(cursor.getInt(1));
      episode.setEpisode(cursor.getInt(2));
      episode.setProductionCode(cursor.getString(3));
      String dateTime = cursor.getString(cursor.getColumnIndexOrThrow(Episode.COLUMN_AIR_DATE));
      DateFormat format = SimpleDateFormat.getDateInstance();
      try {
          episode.setAirDate(format.parse(dateTime));
      } catch (ParseException e) {
          Log.e(SeasonDao.class.getSimpleName(), "Parsing datetime failed", e);
      }
      episode.setTitle(cursor.getString(5));
      episode.setSpecial(Boolean.parseBoolean(cursor.getString(6)));
      episode.setTvRageLink(cursor.getString(7));
//      episode.setSeason(season)
      return episode;
    }
}
