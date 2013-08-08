package be.asers.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

/**
 * Test class for {@link UrlRetrieverTask}. 
 *
 * @author chesteric31
 */
public class UrlRetrieverTaskTest extends AndroidTestCase {

    private UrlRetrieverTask task;
    
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
        task = new UrlRetrieverTask(otherContext);
    }
    
    /**
     * Test method for
     * {@link be.asers.service.FinderServiceImpl#retrieveBasicUrlContent(java.net.URL)}.
     * 
     * @throws IOException if an error occurred
     */
    public void testRetrieveBasicUrlContent() throws IOException {
        URL url = new URL("http://epguides.com/Friends/");
        String content = task.doInBackground(url);
        assertTrue(content != null);
    }

    /**
     * Test method for
     * {@link be.asers.service.UrlRetrieverTask#retrieveBasicUrlContent(java.net.URL)} with empty.
     * 
     * @throws IOException if an error occurred
     */
    public void testRetrieveBasicUrlContentEmpty() throws IOException {
        try {
            task.doInBackground(new URL(""));
            fail("That cannot  be there");
        } catch (MalformedURLException e) {
            System.out.println("That's ok!");
        }
    }

}
