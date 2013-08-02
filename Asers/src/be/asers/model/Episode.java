package be.asers.model;

import java.util.Date;

/**
 * Episode entity.
 *
 * @author chesteric31
 * @version $Revision$ $Date::                  $ $Author$
 */
public class Episode {

    private Integer number;
    private Integer episode;
    private String productionCode;
    private Date airDate;
    private String title;
    private Boolean special;
    private String tvRageLink;
    private Season season;

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
    public Season getSeason() {
        return season;
    }

    /**
     * @param season the season to set
     */
    public void setSeason(Season season) {
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
        StringBuilder builder = new StringBuilder();
        builder.append("Episode [number=");
        if (number != null) {
            builder.append(number);
        }
        builder.append(", episode=");
        builder.append(episode);
        builder.append(", productionCode=");
        if (productionCode != null) {
            builder.append(productionCode);
        }
        builder.append(", airDate=");
        builder.append(airDate);
        builder.append(", title=");
        builder.append(title);
        builder.append(", special=");
        builder.append(special);
        builder.append(", tvRageLink=");
        builder.append(tvRageLink);
        builder.append(", season=");
        if (season == null) {
            System.out.println();
        }
        builder.append(season.getNumber());
        builder.append("]");
        return builder.toString();
    }

}
