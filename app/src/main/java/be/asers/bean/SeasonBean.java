package be.asers.bean;

import java.util.List;

/**
 * Season bean.
 *
 * @author chesteric31
 */
public class SeasonBean extends AbstractIdentityBean {
    
    private Integer number;
    
    private ShowBean show;
    
    private List<EpisodeBean> episodes;

    public Integer getNumber() {
        return number;
    }
    public void setNumber(Integer number) {
        this.number = number;
    }

    public ShowBean getShow() {
        return show;
    }
    public void setShow(ShowBean show) {
        this.show = show;
    }

    public List<EpisodeBean> getEpisodes() {
        return episodes;
    }
    public void setEpisodes(List<EpisodeBean> episodes) {
        this.episodes = episodes;
    }

    @Override
    public String toString() {
        return String.valueOf(number);
    }

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
        return number == other.number;
    }

}
