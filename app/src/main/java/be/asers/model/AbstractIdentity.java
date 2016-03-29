package be.asers.model;

/**
 * Abstract class for all identified classes.
 *
 * @author chesteric31
 */
abstract class AbstractIdentity {

    /** The Constant COLUMN_ID. */
    public static final String COLUMN_ID = "_ID";
    
    /** The id. */
    private Long id;

    /**
     * Gets the id.
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

}
