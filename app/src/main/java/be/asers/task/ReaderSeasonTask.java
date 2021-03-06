package be.asers.task;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import be.asers.bean.EpisodeBean;
import be.asers.bean.SeasonBean;
import be.asers.bean.ShowBean;
import be.asers.model.Episode;

/**
 * {@link AsyncTask} to build {@link SeasonBean} from {@link BufferedReader}.
 * 
 * @author chesteric31
 */
public class ReaderSeasonTask extends AsyncTask<BufferedReader, Void, Void> {

    private static final String END_DATA_DELIMITER = "</pre>";
    private static final int HEADER_NUMBER_LINES = 7;
    private static final String FIRST_COLUMN_TITLE = "number";
    private static final String NOT_SPECIAL_EPISODE = "n";
    private ShowBean show;

    public ReaderSeasonTask(ShowBean show) {
        this.show = show;
    }

    @Override
    protected Void doInBackground(BufferedReader... params) {
        if (params == null) {
            throw new IllegalArgumentException("The reader cannot be empty!");
        }
        String line = null;
        boolean skipFurtherLines = false;
        int i = 0;
        SeasonBean lastSeason = new SeasonBean();
        lastSeason.setShow(show);
        lastSeason.setNumber(0);
        try {
            while ((line = params[0].readLine()) != null) {
                line = line.trim();
                if (i < HEADER_NUMBER_LINES) {
                    i++;
                    continue;
                }
                if (line.startsWith(END_DATA_DELIMITER)) {
                    skipFurtherLines = true;
                }
                lastSeason = processEpisodes(show, line, skipFurtherLines, lastSeason);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private SeasonBean processEpisodes(ShowBean show, String line, boolean skipNextLines, SeasonBean lastSeason) {
        if (!skipNextLines && !line.startsWith(FIRST_COLUMN_TITLE)) {
            String[] strings = line.split(",");
            EpisodeBean episode = buildEpisode(strings);
            Integer seasonNumber = Integer.valueOf(strings[1]);
            if (Boolean.TRUE.equals(episode.getSpecial())) {
                specialEpisodeProcess(show, episode, seasonNumber);
            } else {
                if (lastSeason.getNumber() != seasonNumber) {
                    lastSeason = buildSeason(show, seasonNumber);
                }
                List<EpisodeBean> episodes = lastSeason.getEpisodes();
                if (episodes == null) {
                    episodes = new ArrayList<EpisodeBean>();
                }
                episodes.add(episode);
                lastSeason.setEpisodes(episodes);
                episode.setSeason(lastSeason);
            }
        }
        return lastSeason;
    }

    private SeasonBean buildSeason(ShowBean show, Integer seasonNumber) {
        SeasonBean lastSeason;
        List<SeasonBean> seasons = show.getSeasons();
        if (seasons == null) {
            seasons = new ArrayList<SeasonBean>();
        }
        lastSeason = new SeasonBean();
        lastSeason.setNumber(seasonNumber);
        lastSeason.setShow(show);
        seasons.add(lastSeason);
        show.setSeasons(seasons);
        return lastSeason;
    }

    private void specialEpisodeProcess(ShowBean show, EpisodeBean episode, Integer seasonNumber) {
        List<SeasonBean> seasons = show.getSeasons();
        for (SeasonBean season : seasons) {
            if (seasonNumber.equals(season.getNumber())) {
                episode.setSeason(season);
                season.getEpisodes().add(episode);
            }
        }
    }

    private EpisodeBean buildEpisode(String[] strings) {
        EpisodeBean episode = new EpisodeBean();
        if (!strings[0].isEmpty()) {
            episode.setNumber(Integer.valueOf(strings[0]));
        }
        if (!strings[2].isEmpty()) {
            episode.setEpisode(Integer.valueOf(strings[2]));
        }
        if (!strings[3].isEmpty()) {
            episode.setProductionCode(strings[3]);
        }
        String date = strings[4];
        if (!"UNAIRED".equals(date)) {
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Episode.DATE_PATTERN, Locale.US);
                date = date.replaceAll("\"", "");
                Date airDate = simpleDateFormat.parse(date);
                episode.setAirDate(airDate);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        episode.setTitle(strings[5]);
        boolean notSpecial = NOT_SPECIAL_EPISODE.equals(strings[6]);
        if (notSpecial) {
            episode.setSpecial(Boolean.FALSE);
        } else {
            episode.setSpecial(Boolean.TRUE);
        }
        episode.setTvRageLink(strings[7]);
        return episode;
    }

}
