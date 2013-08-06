package be.asers.model;

import java.util.Date;

/**
 * Episode entity.
 *
 * @author chesteric31
 */
public class Episode extends AbstractIdentity {

    public static final String TABLE_NAME = "EPISODE";

    public static final String COLUMN_NUMBER = "NUMBER";
    public static final String COLUMN_EPISODE = "EPISODE";
    public static final String COLUMN_PRODUCTION_CODE = "PRODUCTION_CODE";
    public static final String COLUMN_AIR_DATE = "AIR_DATE";
    public static final String COLUMN_TITLE = "TITLE";
    public static final String COLUMN_SPECIAL = "SPECIAL";
    public static final String COLUMN_TV_RAGE_LINK = "TV_RAGE_LINK";
    public static final String COLUMN_SEASON = "FK_SEASON";

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + COLUMN_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NUMBER + " INTEGER, " + COLUMN_EPISODE + " INTEGER, "
            + COLUMN_PRODUCTION_CODE + " TEXT, " + COLUMN_AIR_DATE + " DATE, " + COLUMN_TITLE + " TEXT, "
            + COLUMN_SPECIAL + " BOOLEAN, " + COLUMN_TV_RAGE_LINK + " TEXT, " + COLUMN_SEASON
            + " INTEGER, FOREIGN KEY (" + COLUMN_SEASON + ") REFERENCES " + Season.TABLE_NAME + "(" + Season.COLUMN_ID
            + "));";

    public static final String[] ALL_COLUMNS = { COLUMN_ID, COLUMN_NUMBER, COLUMN_EPISODE, COLUMN_PRODUCTION_CODE,
            COLUMN_AIR_DATE, COLUMN_TITLE, COLUMN_SPECIAL, COLUMN_TV_RAGE_LINK, COLUMN_SEASON };

    public static final String DATE_PATTERN = "dd/MMM/yy";
    
    private Integer number;
    private Integer episode;
    private String productionCode;
    private Date airDate;
    private String title;
    private Boolean special;
    private String tvRageLink;
    private Season season;
    
    /**
     * @return the number the sequence number (can be null for special episode)
     */
    public Integer getNumber() {
        return number;
    }

    /**
     * @param number the number to set
     */
    public void setNumber(Integer number) {
        this.number = number;
    }

    /**
     * @return the season the season number
     */
    public Season getSeason() {
        return season;
    }

    /**
     * @param season the season to set
     */
    public void setSeason(Season season) {
        this.season = season;
    }

    /**
     * @return the episode the episode number
     */
    public Integer getEpisode() {
        return episode;
    }

    /**
     * @param episode the episode to set
     */
    public void setEpisode(Integer episode) {
        this.episode = episode;
    }

    /**
     * @return the productionCode the production code
     */
    public String getProductionCode() {
        return productionCode;
    }

    /**
     * @param productionCode the productionCode to set
     */
    public void setProductionCode(String productionCode) {
        this.productionCode = productionCode;
    }

    /**
     * @return the airDate the air date
     */
    public Date getAirDate() {
        return airDate;
    }

    /**
     * @param airDate the airDate to set
     */
    public void setAirDate(Date airDate) {
        this.airDate = airDate;
    }

    /**
     * @return the title the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the special true if it's a special episode, otherwise false
     */
    public Boolean getSpecial() {
        return special;
    }

    /**
     * @param special the special to set
     */
    public void setSpecial(Boolean special) {
        this.special = special;
    }

    /**
     * @return the tvRageLink the web link to the TvRage episode
     */
    public String getTvRageLink() {
        return tvRageLink;
    }

    /**
     * @param tvRageLink the tvRageLink to set
     */
    public void setTvRageLink(String tvRageLink) {
        this.tvRageLink = tvRageLink;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return title;
    }

}
