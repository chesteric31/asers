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
    public static final String COLUMN_TO_SEE = "TO_SEE";
    public static final String COLUMN_SEASON = "FK_SEASON";

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + COLUMN_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NUMBER + " INTEGER, " + COLUMN_EPISODE + " INTEGER, "
            + COLUMN_PRODUCTION_CODE + " TEXT, " + COLUMN_AIR_DATE + " DATE, " + COLUMN_TITLE + " TEXT, "
            + COLUMN_SPECIAL + " BOOLEAN, " + COLUMN_TV_RAGE_LINK + " TEXT, " + COLUMN_SEASON + " INTEGER, "
            + COLUMN_TO_SEE + " BOOLEAN, FOREIGN KEY (" + COLUMN_SEASON + ") REFERENCES " + Season.TABLE_NAME + "("
            + Season.COLUMN_ID + "));";

    public static final String[] ALL_COLUMNS = {COLUMN_ID, COLUMN_NUMBER, COLUMN_EPISODE, COLUMN_PRODUCTION_CODE,
            COLUMN_AIR_DATE, COLUMN_TITLE, COLUMN_SPECIAL, COLUMN_TV_RAGE_LINK, COLUMN_TO_SEE, COLUMN_SEASON };

    public static final String DATE_PATTERN = "dd/MMM/yy";
    
    private Integer number;
    private Integer episode;
    private String productionCode;
    private Date airDate;
    private String title;
    private Boolean special;
    private String tvRageLink;
    private Boolean toSee;
    private Season season;

    public Integer getNumber() {
        return number;
    }
    public void setNumber(Integer number) {
        this.number = number;
    }

    public Season getSeason() {
        return season;
    }
    public void setSeason(Season season) {
        this.season = season;
    }

    public Integer getEpisode() {
        return episode;
    }
    public void setEpisode(Integer episode) {
        this.episode = episode;
    }

    public String getProductionCode() {
        return productionCode;
    }
    public void setProductionCode(String productionCode) {
        this.productionCode = productionCode;
    }

    public Date getAirDate() {
        return airDate;
    }
    public void setAirDate(Date airDate) {
        this.airDate = airDate;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getSpecial() {
        return special;
    }
    public void setSpecial(Boolean special) {
        this.special = special;
    }

    public String getTvRageLink() {
        return tvRageLink;
    }
    public void setTvRageLink(String tvRageLink) {
        this.tvRageLink = tvRageLink;
    }

    @Override
    public String toString() {
        return title;
    }

}
