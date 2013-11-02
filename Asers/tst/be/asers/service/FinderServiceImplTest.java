package be.asers.service;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import be.asers.bean.EpisodeBean;
import be.asers.bean.SeasonBean;
import be.asers.bean.SeriesBean;
import be.asers.model.Series;

/**
 * Test class for {@link FinderService}.
 * 
 * @author chesteric31
 */
public class FinderServiceImplTest extends AndroidTestCase {

    private FinderService finder;

    /**
     * {@inheritDoc}
     * 
     * @throws Exception an {@link Exception}
     */
    @Override
    public void setUp() throws Exception {
        super.setUp();
        Context context = getContext();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isProxy", true);
        editor.putString("proxyAddress", "10.16.0.25");
        editor.putInt("proxyPort", 8080);
        editor.putBoolean("isProxyAuthentication", true);
        editor.putString("proxyUser", "bprtester");
        editor.putString("proxyPassword", "bprtester");
        editor.commit();
        RenamingDelegatingContext otherContext = new RenamingDelegatingContext(context, "test_");
        finder = new FinderServiceImpl(otherContext);
        finder.getSeriesDao().deleteTable();
    }

    /**
     * Test method for
     * {@link be.asers.service.FinderServiceImpl#findSeries(java.lang.String)}.
     */
    public void testFindSeries() {
        String title = "Friends (1994)";
        SeriesBean series = finder.findSeries(title);
        // assertTrue(title.equalsIgnoreCase(series.getTitle()));
        assertTrue(series.getTitle().contains(title));
        // "Friends (1994)",Friends,3616,Sep 1994,May
        // 2004,"239 eps","30 min","NBC",US
        assertTrue(series.getTvRageId() == 3616);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 1994);
        calendar.set(Calendar.MONTH, 8);
        clearDate(calendar);
        assertTrue(series.getStartDate().equals(calendar.getTime()));
        calendar.set(Calendar.YEAR, 2004);
        calendar.set(Calendar.MONTH, 4);
        clearDate(calendar);
        assertTrue(series.getEndDate().equals(calendar.getTime()));
        assertTrue(series.getEpisodesNumber() == 239);
        assertTrue(series.getRunTime() == 30);
        assertTrue("NBC".equals(series.getNetwork()));
        assertTrue("US".equals(series.getCountry()));
    }

    /**
     * Test method for {@link be.asers.service.FinderServiceImpl#findMySeries()}
     * .
     */
    public void testFindMySeries() {
        List<SeriesBean> series = finder.findMySeries();
        assertTrue(series.isEmpty());
    }

    /**
     * Test method for
     * {@link be.asers.service.FinderServiceImpl#addSeries(SeriesBean)}.
     */
    public void testAddSeries() {
        SeriesBean series = new SeriesBean();
        series.setCountry("BE");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2013);
        calendar.set(Calendar.MONTH, 1);
        clearDate(calendar);
        Date time = calendar.getTime();
        series.setEndDate(time);
        series.setEpisodesNumber(1);
        series.setNetwork("RTBF");
        series.setRunTime(30);
        series.setStartDate(time);
        series.setStatus(Series.STATUS_ACTIVE);
        String title = "My Serie";
        series.setTitle(title);
        series.setTvRageId(0);
        finder.addSeries(series);
        SeriesBean addedSeries = finder.findSeries(title);
        assertTrue(addedSeries.getId() == 1L);
    }

    /**
     * Test method for
     * {@link be.asers.service.FinderServiceImpl#findSeries(java.lang.String)}
     * with null.
     */
    public void testFindSeriesNull() {
        try {
            finder.findSeries(null);
            fail("That cannot be there");
        } catch (IllegalArgumentException e) {
            System.out.println("That's ok!");
        }
    }

    /**
     * Test method for
     * {@link be.asers.service.FinderServiceImpl#findSeries(java.lang.String)}
     * with empty.
     */
    public void testFindSeriesEmpty() {
        try {
            finder.findSeries("");
            fail("That cannot be there");
        } catch (IllegalArgumentException e) {
            System.out.println("That's ok!");
        }
    }

    /**
     * Test method for
     * {@link be.asers.service.FinderServiceImpl#findSeries(java.lang.String)}
     * with unknown series.
     */
    public void testFindSeriesNotFoundSeries() {
        assertNull(finder.findSeries("xyzxyz"));
    }

    /**
     * Clears a calendar: set to minimum the date, 0 for hour, minute, second
     * and millisecond.
     * 
     * @param calendar the calendar to clear
     */
    private void clearDate(Calendar calendar) {
        calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DATE));
        calendar.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the
                                               // hour of day !
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);
    }

    /**
     * Test method for
     * {@link be.asers.service.FinderServiceImpl#findSeries(java.lang.String)}
     * .
     * 
     * @throws IOException if an error occurred
     */
    public void testFindSeriesDetails() throws IOException {
        SeriesBean series = finder.findSeries("Friends (1994)");
        assertTrue(series != null);
        int episodesNumber = 0;
        List<SeasonBean> seasons = series.getSeasons();
        assertTrue(seasons.size() == 10);
        for (SeasonBean season : seasons) {
            episodesNumber += season.getEpisodes().size();
        }
        assertTrue(episodesNumber == series.getEpisodesNumber());
        SeasonBean firstSeason = seasons.get(0);
        assertTrue(series.equals(firstSeason.getSeries()));
        EpisodeBean firstEpisode = firstSeason.getEpisodes().get(0);
        assertTrue(series.equals(firstEpisode.getSeason().getSeries()));
        assertTrue(firstSeason.equals(firstEpisode.getSeason()));
        assertTrue(firstEpisode.getAirDate() != null);
        assertTrue(firstEpisode.getProductionCode() != null);
        assertTrue(firstEpisode.getNumber() != null);
        assertTrue(firstEpisode.getEpisode() == 1);
        assertTrue(firstEpisode.getTitle() != null);
        assertTrue(firstEpisode.getTvRageLink() != null);
    }

}
