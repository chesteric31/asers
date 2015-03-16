package be.asers.model;

import java.util.Date;

/**
 * Episode entity.
 * 
 * @author chesteric31
 */
public class Episode extends AbstractIdentity {

    /** The Constant TABLE_NAME. */
    public static final String TABLE_NAME = "EPISODE";

    /** The Constant COLUMN_NUMBER. */
    public static final String COLUMN_NUMBER = "NUMBER";
    
    /** The Constant COLUMN_EPISODE. */
    public static final String COLUMN_EPISODE = "EPISODE";
    
    /** The Constant COLUMN_PRODUCTION_CODE. */
    public static final String COLUMN_PRODUCTION_CODE = "PRODUCTION_CODE";
    
    /** The Constant COLUMN_AIR_DATE. */
    public static final String COLUMN_AIR_DATE = "AIR_DATE";
    
    /** The Constant COLUMN_TITLE. */
    public static final String COLUMN_TITLE = "TITLE";
    
    /** The Constant COLUMN_SPECIAL. */
    public static final String COLUMN_SPECIAL = "SPECIAL";
    
    /** The Constant COLUMN_TV_RAGE_LINK. */
    public static final String COLUMN_TV_RAGE_LINK = "TV_RAGE_LINK";
    
    /** The Constant COLUMN_TO_SEE. */
    public static final String COLUMN_TO_SEE = "TO_SEE";
    
    /** The Constant COLUMN_SEASON. */
    public static final String COLUMN_SEASON = "FK_SEASON";

    /** The Constant CREATE_TABLE. */
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + COLUMN_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NUMBER + " INTEGER, " + COLUMN_EPISODE + " INTEGER, "
            + COLUMN_PRODUCTION_CODE + " TEXT, " + COLUMN_AIR_DATE + " DATE, " + COLUMN_TITLE + " TEXT, "
            + COLUMN_SPECIAL + " BOOLEAN, " + COLUMN_TV_RAGE_LINK + " TEXT, " + COLUMN_SEASON + " INTEGER, "
            + COLUMN_TO_SEE + " BOOLEAN, FOREIGN KEY (" + COLUMN_SEASON + ") REFERENCES " + Season.TABLE_NAME + "("
            + Season.COLUMN_ID + "));";

    /** The Constant ALL_COLUMNS. */
    public static final String[] ALL_COLUMNS = {COLUMN_ID, COLUMN_NUMBER, COLUMN_EPISODE, COLUMN_PRODUCTION_CODE,
            COLUMN_AIR_DATE, COLUMN_TITLE, COLUMN_SPECIAL, COLUMN_TV_RAGE_LINK, COLUMN_TO_SEE, COLUMN_SEASON };

    /** The Constant DATE_PATTERN. */
    public static final String DATE_PATTERN = "dd/MMM/yy";
    
    /** The Constant DATE_OPTIONAL_PATTERN. */
    public static final String DATE_OPTIONAL_PATTERN = "MMM/yy";

    /** The number. */
    private Integer number;
    
    /** The episode. */
    private Integer episode;
    
    /** The production code. */
    private String productionCode;
    
    /** The air date. */
    private Date airDate;
    
    /** The title. */
    private String title;
    
    /** The special. */
    private Boolean special;
    
    /** The tv rage link. */
    private String tvRageLink;
    
    /** The to see. */
    private Boolean toSee;
    
    /** The season. */
    private Season season;

    /**
     * Gets the number.
     *
     * @return the number the sequence number (can be null for special episode)
     */
    public Integer getNumber() {
        return number;
    }

    /**
     * Sets the number.
     *
     * @param number the number to set
     */
    public void setNumber(Integer number) {
        this.number = number;
    }

    /**
     * Gets the season.
     *
     * @return the season the season number
     */
    public Season getSeason() {
        return season;
    }

    /**
     * Sets the season.
     *
     * @param season the season to set
     */
    public void setSeason(Season season) {
        this.season = season;
    }

    /**
     * Gets the episode.
     *
     * @return the episode the episode number
     */
    public Integer getEpisode() {
        return episode;
    }

    /**
     * Sets the episode.
     *
     * @param episode the episode to set
     */
    public void setEpisode(Integer episode) {
        this.episode = episode;
    }

    /**
     * Gets the production code.
     *
     * @return the productionCode the production code
     */
    public String getProductionCode() {
        return productionCode;
    }

    /**
     * Sets the production code.
     *
     * @param productionCode the productionCode to set
     */
    public void setProductionCode(String productionCode) {
        this.productionCode = productionCode;
    }

    /**
     * Gets the air date.
     *
     * @return the airDate the air date
     */
    public Date getAirDate() {
        return airDate;
    }

    /**
     * Sets the air date.
     *
     * @param airDate the airDate to set
     */
    public void setAirDate(Date airDate) {
        this.airDate = airDate;
    }

    /**
     * Gets the title.
     *
     * @return the title the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title.
     *
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the special.
     *
     * @return the special true if it's a special episode, otherwise false
     */
    public Boolean getSpecial() {
        return special;
    }

    /**
     * Sets the special.
     *
     * @param special the special to set
     */
    public void setSpecial(Boolean special) {
        this.special = special;
    }

    /**
     * Gets the tv rage link.
     *
     * @return the tvRageLink the web link to the TvRage episode
     */
    public String getTvRageLink() {
        return tvRageLink;
    }

    /**
     * Sets the tv rage link.
     *
     * @param tvRageLink the tvRageLink to set
     */
    public void setTvRageLink(String tvRageLink) {
        this.tvRageLink = tvRageLink;
    }

    /**
     * Gets the to see.
     *
     * @return the toSee
     */
    public Boolean getToSee() {
        return toSee;
    }

    /**
     * Sets the to see.
     *
     * @param toSee the toSee to set
     */
    public void setToSee(Boolean toSee) {
        this.toSee = toSee;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return title;
    }

}
