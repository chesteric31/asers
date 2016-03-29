package be.asers.bean;

import java.util.Date;
import java.util.List;

import android.graphics.Bitmap;

/**
 * Series bean.
 * 
 * @author chesteric31
 */
public class SeriesBean extends AbstractIdentityBean {

    /** The title. */
    private String title;
    
    /** The tv rage id. */
    private int tvRageId;
    
    /** The network. */
    private String network;
    
    /** The seasons. */
    private List<SeasonBean> seasons;
    
    /** The start date. */
    private Date startDate;
    
    /** The end date. */
    private Date endDate;
    
    /** The episodes number. */
    private int episodesNumber;
    
    /** The run time. */
    private int runTime;
    
    /** The country. */
    private String country;
    
    /** The status. */
    private String status;
    
    /** The directory. */
    private String directory;
    
    /** The cast. */
    private Bitmap cast;

    /**
     * Gets the title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title.
     *
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the tv rage id.
     *
     * @return the tvRageId
     */
    public int getTvRageId() {
        return tvRageId;
    }

    /**
     * Sets the tv rage id.
     *
     * @param tvRageId the tvRageId to set
     */
    public void setTvRageId(int tvRageId) {
        this.tvRageId = tvRageId;
    }

    /**
     * Gets the network.
     *
     * @return the network
     */
    public String getNetwork() {
        return network;
    }

    /**
     * Sets the network.
     *
     * @param network the network to set
     */
    public void setNetwork(String network) {
        this.network = network;
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
     * Gets the start date.
     *
     * @return the startDate
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * Sets the start date.
     *
     * @param startDate the startDate to set
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * Gets the end date.
     *
     * @return the endDate
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * Sets the end date.
     *
     * @param endDate the endDate to set
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * Gets the episodes number.
     *
     * @return the episodesNumber
     */
    public int getEpisodesNumber() {
        return episodesNumber;
    }

    /**
     * Sets the episodes number.
     *
     * @param episodesNumber the episodesNumber to set
     */
    public void setEpisodesNumber(int episodesNumber) {
        this.episodesNumber = episodesNumber;
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
     * Sets the run time.
     *
     * @param runTime the runTime to set
     */
    public void setRunTime(int runTime) {
        this.runTime = runTime;
    }

    /**
     * Gets the country.
     *
     * @return the country the country (like US, UK, ...)
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets the country.
     *
     * @param country the country to set
     */
    public void setCountry(String country) {
        this.country = country;
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
     * Gets the directory.
     *
     * @return the directory
     */
    public String getDirectory() {
        return directory;
    }

    /**
     * Sets the directory.
     *
     * @param directory the directory to set
     */
    public void setDirectory(String directory) {
        this.directory = directory;
    }
    
    /**
     * Gets the cast.
     *
     * @return the cast
     */
    public Bitmap getCast() {
        return cast;
    }

    /**
     * Sets the cast.
     *
     * @param cast the cast to set
     */
    public void setCast(Bitmap cast) {
        this.cast = cast;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        if (title != null) {
            return title + " / " + network;
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
        if (directory == null) {
            result += 0;
        } else {
            result += directory.hashCode();
        }
        result = prime * result;
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
        if (directory == null) {
            if (other.directory != null) {
                return false;
            }
        } else if (!directory.equals(other.directory)) {
            return false;
        }
        return true;
    }

}
