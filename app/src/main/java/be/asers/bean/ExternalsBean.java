package be.asers.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by eric on 4/1/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExternalsBean {

    @JsonProperty("tvrage")
    private int tvRage;

    public int getTvRage() {
        return tvRage;
    }

    public void setTvRage(int tvRage) {
        this.tvRage = tvRage;
    }
}
