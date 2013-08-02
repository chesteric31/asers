package be.asers.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.test.AndroidTestCase;
import be.asers.model.Episode;
import be.asers.model.Season;
import be.asers.model.Series;

/**
 * Test class for Finder. 
 *
 * @author chesteric31
 * @version $Revision$ $Date::                  $ $Author$
 */
public class FinderTest extends AndroidTestCase {

    private Finder finder;
    
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
        finder = new Finder(context);
    }
    
    /**
     * Test method for {@link be.asers.service.Finder#findSeries(java.lang.String)}.
     */
    public void testFindSeries() {
        String title = "friends";
        Series series = finder.findSeries(title);
        assertTrue(title.equalsIgnoreCase(series.getTitle()));
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
     * Test method for {@link be.asers.service.Finder#findSeries(java.lang.String)} with null.
     */
    public void testFindSeriesNull() {
        try {
            finder.findSeries(null);
            fail("That cannot  be there");
        } catch (IllegalArgumentException e) {
            System.out.println("That's ok!");
        }
    }

    /**
     * Test method for {@link be.asers.service.Finder#findSeries(java.lang.String)} with empty.
     */
    public void testFindSeriesEmpty() {
        try {
            finder.findSeries("");
            fail("That cannot  be there");
        } catch (IllegalArgumentException e) {
            System.out.println("That's ok!");
        }
    }

    /**
     * Test method for {@link be.asers.service.Finder#findSeries(java.lang.String)} with unknown series.
     */
    public void testFindSeriesNotFoundSeries() {
        assertNull(finder.findSeries("xyzxyz"));
    }

    /**
     * Clears a calendar: set to minimum the date, 0 for hour, minute, second and
     * millisecond.
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
     * {@link be.asers.service.Finder#retrieveBasicUrlContent(java.net.URL)}.
     * 
     * @throws IOException if an error occurred
     */
    public void testRetrieveBasicUrlContent() throws IOException {
        URL url = new URL("http://epguides.com/Friends/");
        String content = finder.retrieveBasicUrlContent(url);
        assertTrue(content != null);
    }

    /**
     * Test method for
     * {@link be.asers.service.Finder#retrieveBasicUrlContent(java.net.URL)} with null.
     * 
     * @throws IOException if an error occurred
     */
    public void testRetrieveBasicUrlContentNull() throws IOException {
        try {
            finder.retrieveBasicUrlContent(null);
            fail("That cannot  be there");
        } catch (IllegalArgumentException e) {
            System.out.println("That's ok!");
        }
    }

    /**
     * Test method for
     * {@link be.asers.service.Finder#retrieveBasicUrlContent(java.net.URL)} with empty.
     * 
     * @throws IOException if an error occurred
     */
    public void testRetrieveBasicUrlContentEmpty() throws IOException {
        try {
            finder.retrieveBasicUrlContent(new URL(""));
            fail("That cannot  be there");
        } catch (MalformedURLException e) {
            System.out.println("That's ok!");
        }
    }

    /**
     * Test method for {@link be.asers.service.Finder#findSeriesDetails(java.lang.String)}.
     * 
     * @throws IOException if an error occurred
     */
    public void testFindSeriesDetails() throws IOException {
        Series series = finder.findSeriesDetails("Friends");
        assertTrue(series != null);
        int episodesNumber = 0;
        List<Season> seasons = series.getSeasons();
        assertTrue(seasons.size() == 10);
        for (Season season : seasons) {
            episodesNumber += season.getEpisodes().size();
        }
        assertTrue(episodesNumber == series.getEpisodesNumber());
        Season firstSeason = series.getSeasons().get(0);
        assertTrue(series.equals(firstSeason.getSeries()));
        Episode firstEpisode = firstSeason.getEpisodes().get(0);
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
     * Test method for {@link be.asers.service.Finder#findSeriesDetails(java.lang.String)} with null.
     * 
     * @throws IOException if an error occurred
     */
    public void testFindSeriesDetailsNull() throws IOException {
        try {
            finder.findSeriesDetails(null);
            fail("That cannot  be there");
        } catch (IllegalArgumentException e) {
            System.out.println("That's ok!");
        }
    }

    /**
     * Test method for {@link be.asers.service.Finder#findSeriesDetails(java.lang.String)} with empty.
     * 
     * @throws IOException if an error occurred
     */
    public void testFindSeriesDetailsEmpty() throws IOException {
        try {
            finder.findSeriesDetails("");
            fail("That cannot be there");
        } catch (IllegalArgumentException e) {
            System.out.println("That's ok!");
        }
    }
}
