package be.asers.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.database.Cursor;
import be.asers.model.Episode;
import be.asers.model.Season;
import be.asers.model.Series;

/**
 * Data Access Object for {@link Episode} entity.
 * 
 * @author chesteric31
 */
public class EpisodeDao extends AbstractDao {

    // public Episode createEpisode(String comment) {
    // ContentValues values = new ContentValues();
    // values.put(MySQLiteHelper.COLUMN_COMMENT, comment);
    // long insertId = database.insert(MySQLiteHelper.TABLE_COMMENTS, null,
    // values);
    // Cursor cursor = database.query(MySQLiteHelper.TABLE_COMMENTS,
    // allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
    // null, null, null);
    // cursor.moveToFirst();
    // Comment newComment = cursorToEpisode(cursor);
    // cursor.close();
    // return newComment;
    // }

    // public void deleteComment(Comment comment) {
    // long id = comment.getId();
    // System.out.println("Comment deleted with id: " + id);
    // database.delete(MySQLiteHelper.TABLE_COMMENTS, MySQLiteHelper.COLUMN_ID
    // + " = " + id, null);
    // }

    /**
     * Constructor.
     * 
     * @param context the {@link Context} to use
     */
    public EpisodeDao(Context context) {
        super(context);
    }

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
        Cursor cursor = getDatabase().rawQuery(sqlBuilder.toString(), null);

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
     * Creates an {@link Episode} from the {@link Cursor}.
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
        Date airDate;
        SimpleDateFormat dateFormat = new SimpleDateFormat(Episode.DATE_PATTERN, Locale.US);
        try {
            airDate = dateFormat.parse(dateTime);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        episode.setAirDate(airDate);
        episode.setTitle(cursor.getString(5));
        episode.setSpecial(Boolean.parseBoolean(cursor.getString(6)));
        episode.setTvRageLink(cursor.getString(7));
        // episode.setSeason(season)
        return episode;
    }
}
