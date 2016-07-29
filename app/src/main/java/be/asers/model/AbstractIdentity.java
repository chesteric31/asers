package be.asers.model;

/**
 * Abstract class for all identified classes.
 *
 * @author chesteric31
 */
abstract class AbstractIdentity {

    public static final String COLUMN_ID = "_ID";
    
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
