package be.asers.bean;

/**
 * Abstract class to keep id data.
 * 
 * @author chesteric31
 */
public abstract class AbstractIdentityBean {

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
