package be.asers.service.impl;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.test.AndroidTestCase;
import be.asers.bean.EpisodeBean;
import be.asers.bean.SeasonBean;
import be.asers.bean.SeriesBean;
import be.asers.service.FinderRemoteService;

/**
 * Test class for {@link FinderRemoteService}.
 * 
 * @author chesteric31
 */
public class FinderRemoteServiceImplTest extends AndroidTestCase {

    private FinderRemoteService finder;

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
        finder = new FinderRemoteServiceImpl(context);
    }

    /**
     * Test method for
     * {@link be.asers.service.impl.FinderRemoteServiceImpl#findSeries(java.lang.String)}.
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
     * Test method for
     * {@link be.asers.service.impl.FinderServiceImpl#findSeries(java.lang.String)}
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
     * {@link be.asers.service.impl.FinderServiceImpl#findSeries(java.lang.String)}
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
     * {@link be.asers.service.impl.FinderServiceImpl#findSeries(java.lang.String)}
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
     * {@link be.asers.service.impl.FinderServiceImpl#findSeries(java.lang.String)}
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
    
    /**
     * Test method for
     * {@link be.asers.service.impl.FinderServiceImpl#buildSeries(String[])}
     * .
     */
    public void testBuildSeries() {
        String[] tokens = {"The Mentalist", "Mentalist", "18967", "Sep 2008", "___ ____", "126+ eps", "60 min", "CBS",
                "US" };
        SeriesBean series = finder.buildSeries(tokens);
        assertTrue("Mentalist".equals(series.getDirectory()));
        assertTrue("The Mentalist".equals(series.getTitle()));
        assertTrue(60 == series.getRunTime());
        assertTrue("US".equals(series.getCountry()));
        assertTrue("CBS".equals(series.getNetwork()));
        assertTrue(18967 == series.getTvRageId());
        Calendar calendar = Calendar.getInstance();
        calendar.set(2008, Calendar.SEPTEMBER, 1, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date startDate = calendar.getTime();
        assertTrue(startDate.equals(series.getStartDate()));
    }

    /**
     * Test method for
     * {@link be.asers.service.impl.FinderServiceImpl#buildSkinnySeries(String[])}
     * .
     */
    public void testBuildSkinnySeries() {
        String[] tokens = {"The Mentalist", "Mentalist", "18967", "Sep 2008", "___ ____", "126+ eps", "60 min", "CBS",
                "US" };
        SeriesBean skinnySeries = finder.buildSkinnySeries(tokens);
        assertTrue("The Mentalist".equals(skinnySeries.getTitle()));
        assertTrue("CBS".equals(skinnySeries.getNetwork()));
    }
    
    /**
     * Test method for {@link be.asers.service.impl.FinderServiceImpl#createBitmap(SeriesBean)}
     * .
     */
    public void testCreateBitmap() {
        SeriesBean series = new SeriesBean();
        series.setDirectory("AbbottandCostelloShow");
        assertNull(finder.createBitmap(series));
        series.setDirectory("Mentalist");
        assertNotNull(finder.createBitmap(series));
    }

}
