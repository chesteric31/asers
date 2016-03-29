package be.asers.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Series entity.
 * 
 * @author chesteric31
 */
public class Series extends AbstractIdentity {

    /** The Constant TABLE_NAME. */
    public static final String TABLE_NAME = "SERIES";

    /** The Constant COLUMN_TITLE. */
    public static final String COLUMN_TITLE = "TITLE";
    
    /** The Constant COLUMN_TV_RAGE_ID. */
    public static final String COLUMN_TV_RAGE_ID = "TV_RAGE_ID";
    
    /** The Constant COLUMN_NETWORK. */
    public static final String COLUMN_NETWORK = "NETWORK";
    
    /** The Constant COLUMN_START_DATE. */
    public static final String COLUMN_START_DATE = "START_DATE";
    
    /** The Constant COLUMN_END_DATE. */
    public static final String COLUMN_END_DATE = "END_DATE";
    
    /** The Constant COLUMN_EPISODES_NUMBER. */
    public static final String COLUMN_EPISODES_NUMBER = "EPISODES_NUMBER";
    
    /** The Constant COLUMN_RUN_TIME. */
    public static final String COLUMN_RUN_TIME = "RUN_TIME";
    
    /** The Constant COLUMN_COUNTRY. */
    public static final String COLUMN_COUNTRY = "COUNTRY";
    
    /** The Constant COLUMN_STATUS. */
    public static final String COLUMN_STATUS = "STATUS";
    
    /** The Constant COLUMN_DIRECTORY. */
    public static final String COLUMN_DIRECTORY = "DIRECTORY";

    /** The Constant CREATE_TABLE. */
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + COLUMN_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_TITLE + " TEXT, " + COLUMN_TV_RAGE_ID + " INTEGER,"
            + COLUMN_NETWORK + " TEXT, " + COLUMN_START_DATE + " DATE, " + COLUMN_END_DATE + " DATE, "
            + COLUMN_EPISODES_NUMBER + " INTEGER, " + COLUMN_RUN_TIME + " INTEGER, " + COLUMN_COUNTRY + " TEXT, "
            + COLUMN_STATUS + " , " + COLUMN_DIRECTORY + " );";

    /** The Constant ALL_COLUMNS. */
    public static final String[] ALL_COLUMNS = {COLUMN_ID, COLUMN_COUNTRY, COLUMN_END_DATE, COLUMN_EPISODES_NUMBER,
            COLUMN_NETWORK, COLUMN_RUN_TIME, COLUMN_START_DATE, COLUMN_TITLE, COLUMN_TV_RAGE_ID, COLUMN_STATUS,
            COLUMN_DIRECTORY };

    /** The Constant DATE_PATTERN. */
    public static final String DATE_PATTERN = "MMM yyyy";
    
    /** The Constant DATE_PATTERN_OPTIONAL. */
    public static final String DATE_PATTERN_OPTIONAL = "yyyy";
    
    /** The Constant NO_KNOWN_END_DATE. */
    public static final String NO_KNOWN_END_DATE = "___ ____";

    /** The Constant STATUS_ACTIVE. */
    public static final String STATUS_ACTIVE = "ACTIVE";
    
    /** The Constant STATUS_INACTIVE. */
    public static final String STATUS_INACTIVE = "INACTIVE";

    /** The title. */
    private String title;
    
    /** The tv rage id. */
    private int tvRageId;
    
    /** The network. */
    private String network;
    
    /** The seasons. */
    private List<Season> seasons;
    
    /** The start date. */
    private Date startDate;
    
    /** The end date. */
    private Date endDate;
    
    /** The episodes number. */
    private int episodesNumber;
    
    /** The run time. */
    private int runTime;
    
    /** The country. */
    private String country;
    
    /** The status. */
    private String status;
    
    /** The directory. */
    private String directory;

    /**
     * Gets the title.
     *
     * @return the title
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
     * Gets the tv rage id.
     *
     * @return the tvRageId
     */
    public int getTvRageId() {
        return tvRageId;
    }

    /**
     * Sets the tv rage id.
     *
     * @param tvRageId the tvRageId to set
     */
    public void setTvRageId(int tvRageId) {
        this.tvRageId = tvRageId;
    }

    /**
     * Gets the network.
     *
     * @return the network
     */
    public String getNetwork() {
        return network;
    }

    /**
     * Sets the network.
     *
     * @param network the network to set
     */
    public void setNetwork(String network) {
        this.network = network;
    }

    /**
     * Gets the seasons.
     *
     * @return the seasons
     */
    public List<Season> getSeasons() {
        return seasons;
    }

    /**
     * Sets the seasons.
     *
     * @param seasons the seasons to set
     */
    public void setSeasons(List<Season> seasons) {
        this.seasons = seasons;
    }

    /**
     * Gets the start date.
     *
     * @return the startDate
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * Sets the start date.
     *
     * @param startDate the startDate to set
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * Gets the end date.
     *
     * @return the endDate
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * Sets the end date.
     *
     * @param endDate the endDate to set
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * Gets the episodes number.
     *
     * @return the episodesNumber
     */
    public int getEpisodesNumber() {
        return episodesNumber;
    }

    /**
     * Sets the episodes number.
     *
     * @param episodesNumber the episodesNumber to set
     */
    public void setEpisodesNumber(int episodesNumber) {
        this.episodesNumber = episodesNumber;
    }

    /**
     * Gets the run time.
     *
     * @return the runTime the run time duration in minute
     */
    public int getRunTime() {
        return runTime;
    }

    /**
     * Sets the run time.
     *
     * @param runTime the runTime to set
     */
    public void setRunTime(int runTime) {
        this.runTime = runTime;
    }

    /**
     * Gets the country.
     *
     * @return the country the country (like US, UK, ...)
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets the country.
     *
     * @param country the country to set
     */
    public void setCountry(String country) {
        this.country = country;
    }
    
    /**
     * Gets the status.
     *
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the status.
     *
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }
    

    /**
     * Gets the directory.
     *
     * @return the directory
     */
    public String getDirectory() {
        return directory;
    }

    /**
     * Sets the directory.
     *
     * @param directory the directory to set
     */
    public void setDirectory(String directory) {
        this.directory = directory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return title;
    }
    
    /**
     * Adds the season to the current series.
     * 
     * @param season the season to add
     */
    public void addSeason(Season season) {
        if (seasons == null) {
            seasons = new ArrayList<Season>();
        }
        seasons.add(season);
    }

}
