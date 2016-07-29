package be.asers.bean;

/**
 * Abstract class to keep id data.
 * 
 * @author chesteric31
 */
public abstract class AbstractIdentityBean {

    private Long id;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

}
