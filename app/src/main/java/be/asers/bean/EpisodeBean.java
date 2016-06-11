package be.asers.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * Episode bean.
 *
 * @author chesteric31
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EpisodeBean extends AbstractIdentityBean {

    private Integer number;
    private Integer episode;
    private String productionCode;
    @JsonProperty("airdate")
    private Date airDate;
    private String title;
    private Boolean special;
    private String tvRageLink;
    private SeasonBean season;
    
    public Integer getNumber() {
        return number;
    }
    public void setNumber(Integer number) {
        this.number = number;
    }

    public SeasonBean getSeason() {
        return season;
    }
    public void setSeason(SeasonBean season) {
        this.season = season;
    }

    public Integer getEpisode() {
        return episode;
    }
    public void setEpisode(Integer episode) {
        this.episode = episode;
    }

    public String getProductionCode() {
        return productionCode;
    }
    public void setProductionCode(String productionCode) {
        this.productionCode = productionCode;
    }

    public Date getAirDate() {
        return airDate;
    }
    public void setAirDate(Date airDate) {
        this.airDate = airDate;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getSpecial() {
        return special;
    }
    public void setSpecial(Boolean special) {
        this.special = special;
    }

    public String getTvRageLink() {
        return tvRageLink;
    }
    public void setTvRageLink(String tvRageLink) {
        this.tvRageLink = tvRageLink;
    }

    @Override
    public String toString() {
        return title;
    }

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
