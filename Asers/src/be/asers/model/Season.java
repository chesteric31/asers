package be.asers.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Season entity.
 * 
 * @author chesteric31
 */
public class Season extends AbstractIdentity {

    public static final String TABLE_NAME = "SEASON";

    public static final String COLUMN_NUMBER = "NUMBER";
    public static final String COLUMN_SERIES = "FK_SERIES";

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + COLUMN_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NUMBER + " INTEGER, " + COLUMN_SERIES
            + " INTEGER, FOREIGN KEY (" + COLUMN_SERIES + ") REFERENCES " + Series.TABLE_NAME + "("
            + AbstractIdentity.COLUMN_ID + "));";

    public static final String[] ALL_COLUMNS = {COLUMN_ID, COLUMN_NUMBER, COLUMN_SERIES };

    private int number;
    private Series series;
    private List<Episode> episodes;

    /**
     * @return the number
     */
    public int getNumber() {
        return number;
    }

    /**
     * @param number the number to set
     */
    public void setNumber(int number) {
        this.number = number;
    }

    /**
     * @return the series
     */
    public AbstractIdentity getSeries() {
        return series;
    }

    /**
     * @param series the series to set
     */
    public void setSeries(Series series) {
        this.series = series;
    }

    /**
     * @return the episodes
     */
    public List<Episode> getEpisodes() {
        return episodes;
    }

    /**
     * @param episodes the episodes to set
     */
    public void setEpisodes(List<Episode> episodes) {
        this.episodes = episodes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.valueOf(number);
    }
    
    public void addEpisode(Episode episode) {
        if (episodes == null) {
            episodes = new ArrayList<Episode>();
        }
        episodes.add(episode);
    }

}
