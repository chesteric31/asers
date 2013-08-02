package be.asers.model;

/**
 * Abstract class for all identified classes.
 *
 * @author chesteric31
 */
public abstract class AbstractIdentity {

    public static final String COLUMN_ID = "_ID";
    private Long id;

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

}
