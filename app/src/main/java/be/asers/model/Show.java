package be.asers.model;

/**
 * Show entity.
 *
 * @author chesteric31
 */
public class Show extends AbstractIdentity {

    public static final String TABLE_NAME = "SHOW";
    public static final String COLUMN_NAME = "NAME";
    public static final String COLUMN_TV_RAGE_ID = "TV_RAGE_ID";
    public static final String COLUMN_TV_MAZE_ID = "TV_MAZE_ID";
    public static final String COLUMN_NETWORK = "NETWORK";
    public static final String COLUMN_RUN_TIME = "RUN_TIME";
    public static final String COLUMN_COUNTRY = "COUNTRY";
    public static final String COLUMN_STATUS = "STATUS";
    public static final String COLUMN_IMAGE = "IMAGE";

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + COLUMN_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NAME + " TEXT, " + COLUMN_TV_RAGE_ID + " INTEGER,"
            + COLUMN_TV_MAZE_ID + " INTEGER," + COLUMN_NETWORK + " TEXT, " + COLUMN_RUN_TIME + " INTEGER, "
            + COLUMN_COUNTRY + " TEXT, " + COLUMN_IMAGE + " TEXT, " + COLUMN_STATUS + " );";

    public static final String[] ALL_COLUMNS = {COLUMN_ID, COLUMN_COUNTRY,
            COLUMN_NETWORK, COLUMN_RUN_TIME, COLUMN_NAME, COLUMN_TV_RAGE_ID, COLUMN_TV_MAZE_ID,
            COLUMN_STATUS, COLUMN_IMAGE};

    public static final String STATUS_ACTIVE = "ACTIVE";
    public static final String STATUS_INACTIVE = "INACTIVE";
    private String name;
    private int tvRageId;
    private int tvMazeId;
    private String network;
    private int runTime;
    private String country;
    private String status;
    private String cast;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getTvRageId() {
        return tvRageId;
    }
    public void setTvRageId(int tvRageId) {
        this.tvRageId = tvRageId;
    }

    public int getTvMazeId() {
        return tvMazeId;
    }
    public void setTvMazeId(int tvMazeId) {
        this.tvMazeId = tvMazeId;
    }

    public String getNetwork() {
        return network;
    }
    public void setNetwork(String network) {
        this.network = network;
    }

    public int getRunTime() {
        return runTime;
    }
    public void setRunTime(int runTime) {
        this.runTime = runTime;
    }

    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getCast() {
        return cast;
    }
    public void setCast(String cast) {
        this.cast = cast;
    }
}
