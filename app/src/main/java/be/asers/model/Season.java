package be.asers.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Season entity.
 * 
 * @author chesteric31
 */
public class Season extends AbstractIdentity {

    /** The Constant TABLE_NAME. */
    public static final String TABLE_NAME = "SEASON";

    /** The Constant COLUMN_NUMBER. */
    public static final String COLUMN_NUMBER = "NUMBER";
    
    /** The Constant COLUMN_SERIES. */
    public static final String COLUMN_SERIES = "FK_SERIES";

    /** The Constant CREATE_TABLE. */
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + COLUMN_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NUMBER + " INTEGER, " + COLUMN_SERIES
            + " INTEGER, FOREIGN KEY (" + COLUMN_SERIES + ") REFERENCES " + Series.TABLE_NAME + "("
            + AbstractIdentity.COLUMN_ID + "));";

    /** The Constant ALL_COLUMNS. */
    public static final String[] ALL_COLUMNS = {COLUMN_ID, COLUMN_NUMBER, COLUMN_SERIES };

    /** The number. */
    private int number;
    
    private Show show;
    
    /** The episodes. */
    private List<Episode> episodes;

    /**
     * Gets the number.
     *
     * @return the number
     */
    public int getNumber() {
        return number;
    }

    /**
     * Sets the number.
     *
     * @param number the number to set
     */
    public void setNumber(int number) {
        this.number = number;
    }


    public Show getShow() {
        return show;
    }

    public void setShow(Show show) {
        this.show = show;
    }

    /**
     * Gets the episodes.
     *
     * @return the episodes
     */
    public List<Episode> getEpisodes() {
        return episodes;
    }

    /**
     * Sets the episodes.
     *
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
    
    /**
     * Adds episode to the current season.
     * 
     * @param episode the episode to add
     */
    public void addEpisode(Episode episode) {
        if (episodes == null) {
            episodes = new ArrayList<Episode>();
        }
        episodes.add(episode);
    }

}
