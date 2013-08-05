package be.asers.bean;

import java.util.Date;
import java.util.List;

/**
 * Series bean.
 *
 * @author chesteric31
 */
public class SeriesBean {

    private String title;
    private int tvRageId;
    private String network;
    private List<SeasonBean> seasons;
    private Date startDate;
    private Date endDate;
    private int episodesNumber;
    private int runTime;
    private String country;

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the tvRageId
     */
    public int getTvRageId() {
        return tvRageId;
    }

    /**
     * @param tvRageId the tvRageId to set
     */
    public void setTvRageId(int tvRageId) {
        this.tvRageId = tvRageId;
    }

    /**
     * @return the network
     */
    public String getNetwork() {
        return network;
    }

    /**
     * @param network the network to set
     */
    public void setNetwork(String network) {
        this.network = network;
    }

    /**
     * @return the seasons
     */
    public List<SeasonBean> getSeasons() {
        return seasons;
    }

    /**
     * @param seasons the seasons to set
     */
    public void setSeasons(List<SeasonBean> seasons) {
        this.seasons = seasons;
    }

    /**
     * @return the startDate
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the endDate
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * @return the episodesNumber
     */
    public int getEpisodesNumber() {
        return episodesNumber;
    }

    /**
     * @param episodesNumber the episodesNumber to set
     */
    public void setEpisodesNumber(int episodesNumber) {
        this.episodesNumber = episodesNumber;
    }

    /**
     * @return the runTime the run time duration in minute
     */
    public int getRunTime() {
        return runTime;
    }

    /**
     * @param runTime the runTime to set
     */
    public void setRunTime(int runTime) {
        this.runTime = runTime;
    }

    /**
     * @return the country the country (like US, UK, ...)
     */
    public String getCountry() {
        return country;
    }

    /**
     * @param country the country to set
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return title;
    }

}