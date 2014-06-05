package be.asers.bean;

import java.util.Date;

/**
 * Episode bean.
 *
 * @author chesteric31
 */
public class EpisodeBean extends AbstractIdentityBean {

    private Integer number;
    private Integer episode;
    private String productionCode;
    private Date airDate;
    private String title;
    private Boolean special;
    private String tvRageLink;
    private SeasonBean season;
    
    /**
     * @return the number the sequence number (can be null for special episode)
     */
    public Integer getNumber() {
        return number;
    }

    /**
     * @param number the number to set
     */
    public void setNumber(Integer number) {
        this.number = number;
    }

    /**
     * @return the season the season number
     */
    public SeasonBean getSeason() {
        return season;
    }

    /**
     * @param season the season to set
     */
    public void setSeason(SeasonBean season) {
        this.season = season;
    }

    /**
     * @return the episode the episode number
     */
    public Integer getEpisode() {
        return episode;
    }

    /**
     * @param episode the episode to set
     */
    public void setEpisode(Integer episode) {
        this.episode = episode;
    }

    /**
     * @return the productionCode the production code
     */
    public String getProductionCode() {
        return productionCode;
    }

    /**
     * @param productionCode the productionCode to set
     */
    public void setProductionCode(String productionCode) {
        this.productionCode = productionCode;
    }

    /**
     * @return the airDate the air date
     */
    public Date getAirDate() {
        return airDate;
    }

    /**
     * @param airDate the airDate to set
     */
    public void setAirDate(Date airDate) {
        this.airDate = airDate;
    }

    /**
     * @return the title the title
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
     * @return the special true if it's a special episode, otherwise false
     */
    public Boolean getSpecial() {
        return special;
    }

    /**
     * @param special the special to set
     */
    public void setSpecial(Boolean special) {
        this.special = special;
    }

    /**
     * @return the tvRageLink the web link to the TvRage episode
     */
    public String getTvRageLink() {
        return tvRageLink;
    }

    /**
     * @param tvRageLink the tvRageLink to set
     */
    public void setTvRageLink(String tvRageLink) {
        this.tvRageLink = tvRageLink;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return title;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result;
        if (airDate == null) {
            result += 0;
        } else {
            result += airDate.hashCode();
        }
        result = prime * result;
        if (episode == null) {
            result += 0;
        } else {
            result += episode.hashCode();
        }
        result = prime * result;
        if (number == null) {
            result += 0;
        } else {
            result += number.hashCode();
        }
        result = prime * result;
        if (productionCode == null) {
            result += 0;
        } else {
            result += productionCode.hashCode();
        }
        result = prime * result;
        if (season == null) {
            result += 0;
        } else {
            result += season.hashCode();
        }
        result = prime * result;
        if (special == null) {
            result += 0;
        } else {
            result += special.hashCode();
        }
        result = prime * result;
        if (title == null) {
            result += 0;
        } else {
            result += title.hashCode();
        }
        result = prime * result;
        if (tvRageLink == null) {
            result += 0;
        } else {
            result += tvRageLink.hashCode();
        }
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
        if (!(obj instanceof EpisodeBean)) {
            return false;
        }
        EpisodeBean other = (EpisodeBean) obj;
        if (airDate == null) {
            if (other.airDate != null) {
                return false;
            }
        } else if (!airDate.equals(other.airDate)) {
            return false;
        }
        if (episode == null) {
            if (other.episode != null) {
                return false;
            }
        } else if (!episode.equals(other.episode)) {
            return false;
        }
        if (number == null) {
            if (other.number != null) {
                return false;
            }
        } else if (!number.equals(other.number)) {
            return false;
        }
        if (productionCode == null) {
            if (other.productionCode != null) {
                return false;
            }
        } else if (!productionCode.equals(other.productionCode)) {
            return false;
        }
        if (special == null) {
            if (other.special != null) {
                return false;
            }
        } else if (!special.equals(other.special)) {
            return false;
        }
        if (title == null) {
            if (other.title != null) {
                return false;
            }
        } else if (!title.equals(other.title)) {
            return false;
        }
        if (tvRageLink == null) {
            if (other.tvRageLink != null) {
                return false;
            }
        } else if (!tvRageLink.equals(other.tvRageLink)) {
            return false;
        }
        return true;
    }

}
