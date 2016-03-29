package be.asers.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.os.AsyncTask;
import be.asers.bean.EpisodeBean;
import be.asers.bean.SeasonBean;
import be.asers.bean.SeriesBean;
import be.asers.model.Episode;

/**
 * {@link AsyncTask} to build {@link SeasonBean} from {@link BufferedReader}.
 * 
 * @author chesteric31
 */
public class ReaderSeasonTask extends AsyncTask<BufferedReader, Void, Void> {

    /** The Constant END_DATA_DELIMITER. */
    private static final String END_DATA_DELIMITER = "</pre>";
    
    /** The Constant HEADER_NUMBER_LINES. */
    private static final int HEADER_NUMBER_LINES = 7;
    
    /** The Constant FIRST_COLUMN_TITLE. */
    private static final String FIRST_COLUMN_TITLE = "number";
    
    /** The Constant NOT_SPECIAL_EPISODE. */
    private static final String NOT_SPECIAL_EPISODE = "n";
    
    /** The series. */
    private SeriesBean series;

    /**
     * Constructor.
     * 
     * @param series the {@link SeriesBean} to use
     */
    public ReaderSeasonTask(SeriesBean series) {
        this.series = series;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Void doInBackground(BufferedReader... params) {
        if (params == null) {
            throw new IllegalArgumentException("The reader cannot be empty!");
        }
        String line = null;
        boolean skipFurtherLines = false;
        int i = 0;
        SeasonBean lastSeason = new SeasonBean();
        lastSeason.setSeries(series);
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
                lastSeason = processEpisodes(series, line, skipFurtherLines, lastSeason);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * Process the episodes.
     * 
     * @param series the {@link SeriesBean} to use
     * @param line the line to process
     * @param skipNextLines if we must skip the next lines
     * @param lastSeason the last {@link SeasonBean} processed
     * @return the {@link SeasonBean} entity
     */
    private SeasonBean processEpisodes(SeriesBean series, String line, boolean skipNextLines, SeasonBean lastSeason) {
        if (!skipNextLines && !line.startsWith(FIRST_COLUMN_TITLE)) {
            String[] strings = line.split(",");
            EpisodeBean episode = buildEpisode(strings);
            Integer seasonNumber = Integer.valueOf(strings[1]);
            if (Boolean.TRUE.equals(episode.getSpecial())) {
                specialEpisodeProcess(series, episode, seasonNumber);
            } else {
                if (lastSeason.getNumber() != seasonNumber) {
                    lastSeason = buildSeason(series, seasonNumber);
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

    /**
     * Builds the {@link SeasonBean} from the {@link SeriesBean} and the
     * {@link SeasonBean} number.
     * 
     * @param series the {@link SeriesBean} to use
     * @param seasonNumber the {@link SeasonBean} number to use
     * @return the build {@link SeasonBean}
     */
    private SeasonBean buildSeason(SeriesBean series, Integer seasonNumber) {
        SeasonBean lastSeason;
        List<SeasonBean> seasons = series.getSeasons();
        if (seasons == null) {
            seasons = new ArrayList<SeasonBean>();
        }
        lastSeason = new SeasonBean();
        lastSeason.setNumber(seasonNumber);
        lastSeason.setSeries(series);
        seasons.add(lastSeason);
        series.setSeasons(seasons);
        return lastSeason;
    }

    /**
     * Special {@link EpisodeBean} process.
     * 
     * @param series the {@link SeriesBean} to use
     * @param episode the {@link EpisodeBean} to process
     * @param seasonNumber the {@link SeasonBean} number to use
     */
    private void specialEpisodeProcess(SeriesBean series, EpisodeBean episode, Integer seasonNumber) {
        List<SeasonBean> seasons = series.getSeasons();
        for (SeasonBean season : seasons) {
            if (seasonNumber.equals(season.getNumber())) {
                episode.setSeason(season);
                season.getEpisodes().add(episode);
            }
        }
    }

    /**
     * Builds the {@link EpisodeBean} from the String array.
     * 
     * @param strings the String array contained all the {@link EpisodeBean}
     *            informations
     * @return the built {@link EpisodeBean}
     */
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
