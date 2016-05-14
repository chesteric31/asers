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

    /**
     * The network.
     */
    private NetworkBean network;

    private ExternalsBean externals;

    /**
     * The seasons.
     */
    private List<SeasonBean> seasons;

    /**
     * The run time.
     */
    private int runTime;

    /**
     * The status.
     */
    private String status;

    /**
     * The cast.
     */
    private ImageBean image;

    /**
     * Gets the network.
     *
     * @return the network
     */
    public NetworkBean getNetwork() {
        return network;
    }

    /**
     * Gets the seasons.
     *
     * @return the seasons
     */
    public List<SeasonBean> getSeasons() {
        return seasons;
    }

    /**
     * Sets the seasons.
     *
     * @param seasons the seasons to set
     */
    public void setSeasons(List<SeasonBean> seasons) {
        this.seasons = seasons;
    }

    /**
     * Gets the run time.
     *
     * @return the runTime the run time duration in minute
     */
    public int getRunTime() {
        return runTime;
    }

    /**
     * Gets the status.
     *
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the status.
     *
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * {@inheritDoc}
     */
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
