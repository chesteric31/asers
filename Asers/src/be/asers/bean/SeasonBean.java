package be.asers.bean;

import java.util.List;

/**
 * Season bean.
 *
 * @author chesteric31
 */
public class SeasonBean extends AbstractIdentityBean {
    
    private Integer number;
    private SeriesBean series;
    private List<EpisodeBean> episodes;
    
    /**
     * @return the number
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
     * @return the series
     */
    public SeriesBean getSeries() {
        return series;
    }

    /**
     * @param series the series to set
     */
    public void setSeries(SeriesBean series) {
        this.series = series;
    }

    /**
     * @return the episodes
     */
    public List<EpisodeBean> getEpisodes() {
        return episodes;
    }

    /**
     * @param episodes the episodes to set
     */
    public void setEpisodes(List<EpisodeBean> episodes) {
        this.episodes = episodes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.valueOf(number);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result;
        if (episodes == null) {
            result += 0;
        } else {
            result += episodes.hashCode();
        }
        result = prime * result + number;
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
        if (!(obj instanceof SeasonBean)) {
            return false;
        }
        SeasonBean other = (SeasonBean) obj;
        if (episodes == null) {
            if (other.episodes != null) {
                return false;
            }
        } else if (!episodes.equals(other.episodes)) {
            return false;
        }
        if (number != other.number) {
            return false;
        }
        return true;
    }

}
