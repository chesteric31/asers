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
import be.asers.model.Episode;
import be.asers.model.Season;

/**
 * Data Access Object for {@link Episode} entity.
 * 
 * @author chesteric31
 */
public class EpisodeDao extends AbstractDao {

    /**
     * Constructor.
     * 
     * @param context the {@link Context} to use
     */
    public EpisodeDao(Context context) {
        super(context);
    }

    /**
     * Creates a new {@link Episode} from {@link ContentValues}.
     * 
     * @param values the {@link ContentValues} to persist
     * @return the created {@link Episode} in database
     */
    public Episode add(ContentValues values) {
        long insertId = getDatabase().insert(Episode.TABLE_NAME, null, values);
        Cursor cursor = getDatabase().query(Episode.TABLE_NAME, Episode.ALL_COLUMNS,
                Episode.COLUMN_ID + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        Episode newEpisode = retrieveEpisode(cursor);
        cursor.close();
        return newEpisode;
    }

    /**
     * Finds all the episodes for a {@link Season}.
     * 
     * @param season the {@link Season} to use
     * @return all the {@link Episode} for a {@link Season}
     */
    public List<Episode> findAllForSeason(Season season) {
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
        sqlBuilder.append("E." + Episode.COLUMN_SEASON + " ");
        sqlBuilder.append("FROM ");
        sqlBuilder.append(Episode.TABLE_NAME + " E ");
        sqlBuilder.append("WHERE ");
        sqlBuilder.append("E." + Episode.COLUMN_SEASON + " = " + season.getId());
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
        Date airDate = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(Episode.DATE_PATTERN, Locale.US);
        try {
            if (!"".equals(dateTime)) {
                airDate = dateFormat.parse(dateTime);
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        episode.setAirDate(airDate);
        episode.setTitle(cursor.getString(5));
        episode.setSpecial(Boolean.parseBoolean(cursor.getString(6)));
        episode.setTvRageLink(cursor.getString(7));
        return episode;
    }

    /**
     * Update.
     *
     * @param contentValues the content values
     * @param id the id
     */
    public void update(ContentValues contentValues, Long id) {
        getDatabase().update(Episode.TABLE_NAME, contentValues, "_id=" + id, null);
    }
}
