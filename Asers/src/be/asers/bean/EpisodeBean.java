package be.asers.bean;

import java.util.Date;

/**
 * Episode bean.
 *
 * @author chesteric31
 */
public class EpisodeBean extends AbstractIdentityBean {

    /** The number. */
    private Integer number;
    
    /** The episode. */
    private Integer episode;
    
    /** The production code. */
    private String productionCode;
    
    /** The air date. */
    private Date airDate;
    
    /** The title. */
    private String title;
    
    /** The special. */
    private Boolean special;
    
    /** The tv rage link. */
    private String tvRageLink;
    
    /** The season. */
    private SeasonBean season;
    
    /**
     * Gets the number.
     *
     * @return the number the sequence number (can be null for special episode)
     */
    public Integer getNumber() {
        return number;
    }

    /**
     * Sets the number.
     *
     * @param number the number to set
     */
    public void setNumber(Integer number) {
        this.number = number;
    }

    /**
     * Gets the season.
     *
     * @return the season the season number
     */
    public SeasonBean getSeason() {
        return season;
    }

    /**
     * Sets the season.
     *
     * @param season the season to set
     */
    public void setSeason(SeasonBean season) {
        this.season = season;
    }

    /**
     * Gets the episode.
     *
     * @return the episode the episode number
     */
    public Integer getEpisode() {
        return episode;
    }

    /**
     * Sets the episode.
     *
     * @param episode the episode to set
     */
    public void setEpisode(Integer episode) {
        this.episode = episode;
    }

    /**
     * Gets the production code.
     *
     * @return the productionCode the production code
     */
    public String getProductionCode() {
        return productionCode;
    }

    /**
     * Sets the production code.
     *
     * @param productionCode the productionCode to set
     */
    public void setProductionCode(String productionCode) {
        this.productionCode = productionCode;
    }

    /**
     * Gets the air date.
     *
     * @return the airDate the air date
     */
    public Date getAirDate() {
        return airDate;
    }

    /**
     * Sets the air date.
     *
     * @param airDate the airDate to set
     */
    public void setAirDate(Date airDate) {
        this.airDate = airDate;
    }

    /**
     * Gets the title.
     *
     * @return the title the title
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
     * Gets the special.
     *
     * @return the special true if it's a special episode, otherwise false
     */
    public Boolean getSpecial() {
        return special;
    }

    /**
     * Sets the special.
     *
     * @param special the special to set
     */
    public void setSpecial(Boolean special) {
        this.special = special;
    }

    /**
     * Gets the tv rage link.
     *
     * @return the tvRageLink the web link to the TvRage episode
     */
    public String getTvRageLink() {
        return tvRageLink;
    }

    /**
     * Sets the tv rage link.
     *
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
