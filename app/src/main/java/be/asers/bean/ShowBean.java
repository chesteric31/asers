package be.asers.bean;

import android.graphics.Bitmap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ShowBean extends AbstractIdentityBean {

    @JsonProperty("id")
    private int tvMazeId;
    private String name;
    private Date nextEpisodeAirDate;
    private Bitmap cast;

    public Date getNextEpisodeAirDate() {
        return nextEpisodeAirDate;
    }

    public void setNextEpisodeAirDate(Date nextEpisodeAirDate) {
        this.nextEpisodeAirDate = nextEpisodeAirDate;
    }

    public int getTvMazeId() {
        return tvMazeId;
    }

    public String getName() {
        return name;
    }

    private NetworkBean network;
    private ExternalsBean externals;
    private List<SeasonBean> seasons;
    private int runTime;
    private String status;
    private ImageBean image;
    public NetworkBean getNetwork() {
        return network;
    }

    public List<SeasonBean> getSeasons() {
        return seasons;
    }
    public void setSeasons(List<SeasonBean> seasons) {
        this.seasons = seasons;
    }

    public int getRunTime() {
        return runTime;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        if (name != null && network != null) {
            return name + " / " + network.getName();
        } else {
            return "";
        }
    }

    public void setNetwork(NetworkBean network) {
        this.network = network;
    }

    public void setRunTime(int runTime) {
        this.runTime = runTime;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTvMazeId(int tvMazeId) {
        this.tvMazeId = tvMazeId;
    }

    public void setExternals(ExternalsBean externals) {
        this.externals = externals;
    }

    public ExternalsBean getExternals() {
        return externals;
    }

    public ImageBean getImage() {
        return image;
    }

    public void setImage(ImageBean image) {
        this.image = image;
    }

    public void setCast(Bitmap cast) {
        this.cast = cast;
    }

    public Bitmap getCast() {
        return cast;
    }
}
