package be.asers.model;

import java.util.List;

/**
 * Season entity.
 *
 * @author chesteric31
 * @version $Revision$ $Date::                  $ $Author$
 */
public class Season {

    private int number;
    private Series series;
    private List<Episode> episodes;

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
    public Series getSeries() {
        return series;
    }

    /**
     * @param series the series to set
     */
    public void setSeries(Series series) {
        this.series = series;
    }

    /**
     * @return the episodes
     */
    public List<Episode> getEpisodes() {
        return episodes;
    }

    /**
     * @param episodes the episodes to set
     */
    public void setEpisodes(List<Episode> episodes) {
        this.episodes = episodes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Season [number=");
        builder.append(number);
        builder.append(", series=");
        builder.append(series.getTitle());
        builder.append(", episodes=");
        builder.append(episodes);
        builder.append("]");
        return builder.toString();
    }

}
