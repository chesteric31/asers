package be.asers.bean;

/**
 * Abstract class to keep id data.
 * 
 * @author chesteric31
 */
public abstract class AbstractIdentityBean {

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
