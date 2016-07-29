package be.asers.model;

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
            + " INTEGER, FOREIGN KEY (" + COLUMN_SERIES + ") REFERENCES " + Show.TABLE_NAME + "("
            + AbstractIdentity.COLUMN_ID + "));";

    public static final String[] ALL_COLUMNS = {COLUMN_ID, COLUMN_NUMBER, COLUMN_SERIES };
    private int number;
    private Show show;
    private List<Episode> episodes;

    public int getNumber() {
        return number;
    }
    public void setNumber(int number) {
        this.number = number;
    }

    public Show getShow() {
        return show;
    }
    public void setShow(Show show) {
        this.show = show;
    }

    @Override
    public String toString() {
        return String.valueOf(number);
    }

}
