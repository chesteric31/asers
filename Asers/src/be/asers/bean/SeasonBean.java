package be.asers.bean;

import java.util.List;

/**
 * Season bean.
 *
 * @author chesteric31
 */
public class SeasonBean {
    
    private int number;
    private SeriesBean series;
    private List<EpisodeBean> episodes;
    
    /**
     * @return the number
     */
    public int getNumber() {
        return number;
    }

    /**
     * @param number the number to set
     */
    public void setNumber(int number) {
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

}
