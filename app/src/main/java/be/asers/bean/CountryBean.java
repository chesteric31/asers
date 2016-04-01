package be.asers.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by eric on 4/1/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CountryBean {

    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
