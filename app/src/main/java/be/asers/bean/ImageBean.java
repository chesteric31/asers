package be.asers.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by eric on 4/1/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ImageBean {

    private String medium;
    private String original;

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }
}
