package be.asers.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Show entity.
 *
 * @author chesteric31
 */
public class Show extends AbstractIdentity {

    /** The Constant TABLE_NAME. */
    public static final String TABLE_NAME = "SHOW";

    /** The Constant COLUMN_NAME. */
    public static final String COLUMN_NAME = "NAME";

    /** The Constant COLUMN_TV_RAGE_ID. */
    public static final String COLUMN_TV_RAGE_ID = "TV_RAGE_ID";
    public static final String COLUMN_TV_MAZE_ID = "TV_MAZE_ID";

    /** The Constant COLUMN_NETWORK. */
    public static final String COLUMN_NETWORK = "NETWORK";

    /** The Constant COLUMN_RUN_TIME. */
    public static final String COLUMN_RUN_TIME = "RUN_TIME";

    /** The Constant COLUMN_COUNTRY. */
    public static final String COLUMN_COUNTRY = "COUNTRY";

    /** The Constant COLUMN_STATUS. */
    public static final String COLUMN_STATUS = "STATUS";

    public static final String COLUMN_IMAGE = "IMAGE";

    /** The Constant CREATE_TABLE. */
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + COLUMN_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NAME + " TEXT, " + COLUMN_TV_RAGE_ID + " INTEGER,"
            + COLUMN_TV_MAZE_ID + " INTEGER," + COLUMN_NETWORK + " TEXT, " + COLUMN_RUN_TIME + " INTEGER, "
            + COLUMN_COUNTRY + " TEXT, " + COLUMN_IMAGE + " TEXT, " + COLUMN_STATUS + " );";

    /** The Constant ALL_COLUMNS. */
    public static final String[] ALL_COLUMNS = {COLUMN_ID, COLUMN_COUNTRY,
            COLUMN_NETWORK, COLUMN_RUN_TIME, COLUMN_NAME, COLUMN_TV_RAGE_ID, COLUMN_TV_MAZE_ID,
            COLUMN_STATUS, COLUMN_IMAGE};

    /** The Constant STATUS_ACTIVE. */
    public static final String STATUS_ACTIVE = "ACTIVE";

    /** The Constant STATUS_INACTIVE. */
    public static final String STATUS_INACTIVE = "INACTIVE";

    /** The title. */
    private String name;

    /** The tv rage id. */
    private int tvRageId;

    private int tvMazeId;

    /** The network. */
    private String network;

    /** The seasons. */
    private List<Season> seasons;

    /** The run time. */
    private int runTime;

    /** The country. */
    private String country;

    /** The status. */
    private String status;

    private String cast;

    /**
     * Gets the title.
     *
     * @return the title
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the title.
     *
     * @param name the title to set
     */
    public void setName(String name) {
        this.name = name;
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

    public int getTvMazeId() {
        return tvMazeId;
    }

    public void setTvMazeId(int tvMazeId) {
        this.tvMazeId = tvMazeId;
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
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return name;
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

    public String getCast() {
        return cast;
    }

    public void setCast(String cast) {
        this.cast = cast;
    }
}
