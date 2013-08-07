package be.asers.model;

import java.util.Date;
import java.util.List;

/**
 * Series entity.
 * 
 * @author chesteric31
 */
public class Series extends AbstractIdentity {

    public static final String TABLE_NAME = "SERIES";

    public static final String COLUMN_TITLE = "TITLE";
    public static final String COLUMN_TV_RAGE_ID = "TV_RAGE_ID";
    public static final String COLUMN_NETWORK = "NETWORK";
    public static final String COLUMN_START_DATE = "START_DATE";
    public static final String COLUMN_END_DATE = "END_DATE";
    public static final String COLUMN_EPISODES_NUMBER = "EPISODES_NUMBER";
    public static final String COLUMN_RUN_TIME = "RUN_TIME";
    public static final String COLUMN_COUNTRY = "COUNTRY";
    public static final String COLUMN_STATUS = "STATUS";

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + COLUMN_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_TITLE + " TEXT, " + COLUMN_TV_RAGE_ID + " INTEGER,"
            + COLUMN_NETWORK + " TEXT, " + COLUMN_START_DATE + " DATE, " + COLUMN_END_DATE + " DATE, "
            + COLUMN_EPISODES_NUMBER + " INTEGER, " + COLUMN_RUN_TIME + " INTEGER, " + COLUMN_COUNTRY + " TEXT, "
            + COLUMN_STATUS + ");";

    public static final String[] ALL_COLUMNS = {COLUMN_ID, COLUMN_TITLE, COLUMN_TV_RAGE_ID, COLUMN_NETWORK,
            COLUMN_START_DATE, COLUMN_END_DATE, COLUMN_EPISODES_NUMBER, 
            COLUMN_RUN_TIME, COLUMN_COUNTRY, COLUMN_STATUS };

    public static final String DATE_PATTERN = "MMM yyyy";

    public static final String STATUS_ACTIVE = "ACTIVE";
    public static final String STATUS_INACTIVE = "INACTIVE";

    private String title;
    private int tvRageId;
    private String network;
    private List<Season> seasons;
    private Date startDate;
    private Date endDate;
    private int episodesNumber;
    private int runTime;
    private String country;
    private String status;

    /**
     * @return the title
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
     * @return the tvRageId
     */
    public int getTvRageId() {
        return tvRageId;
    }

    /**
     * @param tvRageId the tvRageId to set
     */
    public void setTvRageId(int tvRageId) {
        this.tvRageId = tvRageId;
    }

    /**
     * @return the network
     */
    public String getNetwork() {
        return network;
    }

    /**
     * @param network the network to set
     */
    public void setNetwork(String network) {
        this.network = network;
    }

    /**
     * @return the seasons
     */
    public List<Season> getSeasons() {
        return seasons;
    }

    /**
     * @param seasons the seasons to set
     */
    public void setSeasons(List<Season> seasons) {
        this.seasons = seasons;
    }

    /**
     * @return the startDate
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the endDate
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * @return the episodesNumber
     */
    public int getEpisodesNumber() {
        return episodesNumber;
    }

    /**
     * @param episodesNumber the episodesNumber to set
     */
    public void setEpisodesNumber(int episodesNumber) {
        this.episodesNumber = episodesNumber;
    }

    /**
     * @return the runTime the run time duration in minute
     */
    public int getRunTime() {
        return runTime;
    }

    /**
     * @param runTime the runTime to set
     */
    public void setRunTime(int runTime) {
        this.runTime = runTime;
    }

    /**
     * @return the country the country (like US, UK, ...)
     */
    public String getCountry() {
        return country;
    }

    /**
     * @param country the country to set
     */
    public void setCountry(String country) {
        this.country = country;
    }
    
    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return title;
    }

}
