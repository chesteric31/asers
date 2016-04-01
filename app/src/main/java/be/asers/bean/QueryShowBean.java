package be.asers.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class QueryShowBean {

    private String score;
    private ShowBean show;

    public String getScore() {
        return score;
    }

    public ShowBean getShow() {
        return show;
    }
}
