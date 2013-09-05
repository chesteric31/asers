package be.asers.bean;

import java.util.Date;
import java.util.List;

/**
 * Series bean.
 *
 * @author chesteric31
 */
public class SeriesBean extends AbstractIdentityBean {

    private String title;
    private int tvRageId;
    private String network;
    private List<SeasonBean> seasons;
    private Date startDate;
    private Date endDate;
    private int episodesNumber;
    private int runTime;
    private String country;
    private String status;

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
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
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
        if (title != null) {
            return title;
        } else {
            return "";
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result;
        if (country == null) {
            result += 0;
        } else {
            result += country.hashCode();
        }
        result = prime * result;
        if (endDate == null) {
            result += 0;
        } else {
            result += endDate.hashCode();
        }
        result = prime * result + episodesNumber;
        result = prime * result;
        if (network == null) {
            result += 0;
        } else {
            result += network.hashCode();
        }
        result = prime * result + runTime;
        result = prime * result;
        if (seasons == null) {
            result += 0;
        } else {
            result += seasons.hashCode();
        }
        result = prime * result;
        if (startDate == null) {
            result += 0;
        } else {
            result += startDate.hashCode();
        }
        result = prime * result;
        if (status == null) {
            result += 0;
        } else {
            result += status.hashCode();
        }
        result = prime * result;
        if (title == null) {
            result += 0;
        } else {
            result += title.hashCode();
        }
        result = prime * result + tvRageId;
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof SeriesBean)) {
            return false;
        }
        SeriesBean other = (SeriesBean) obj;
        if (country == null) {
            if (other.country != null) {
                return false;
            }
        } else if (!country.equals(other.country)) {
            return false;
        }
        if (endDate == null) {
            if (other.endDate != null) {
                return false;
            }
        } else if (!endDate.equals(other.endDate)) {
            return false;
        }
        if (episodesNumber != other.episodesNumber) {
            return false;
        }
        if (network == null) {
            if (other.network != null) {
                return false;
            }
        } else if (!network.equals(other.network)) {
            return false;
        }
        if (runTime != other.runTime) {
            return false;
        }
        if (seasons == null) {
            if (other.seasons != null) {
                return false;
            }
        } else if (!seasons.equals(other.seasons)) {
            return false;
        }
        if (startDate == null) {
            if (other.startDate != null) {
                return false;
            }
        } else if (!startDate.equals(other.startDate)) {
            return false;
        }
        if (status == null) {
            if (other.status != null) {
                return false;
            }
        } else if (!status.equals(other.status)) {
            return false;
        }
        if (title == null) {
            if (other.title != null) {
                return false;
            }
        } else if (!title.equals(other.title)) {
            return false;
        }
        if (tvRageId != other.tvRageId) {
            return false;
        }
        return true;
    }
    
}
