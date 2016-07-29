package be.asers.task;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.test.AndroidTestCase;
import android.util.Base64;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * Test class for {@link ReaderStringTask}.
 * 
 * @author chesteric31
 */
public class ReaderStringTaskTest extends AndroidTestCase {

    private ReaderStringTask task;

    /**
     * {@inheritDoc}
     * 
     * @throws Exception an {@link Exception}
     */
    @Override
    public void setUp() throws Exception {
        super.setUp();
        task = new ReaderStringTask();
    }

    /**
     * Test method for doInBackground.
     * 
     * @throws IOException an {@link IOException} if an error occurred
     */
    public void testDoInBackground() throws IOException {
        URL url = new URL("http://epguides.com/Friends/");
        BufferedReader bufferedReader = createReaderFromUrl(url);
        List<String> strings = task.doInBackground(bufferedReader);
        assertFalse(strings.isEmpty());
    }

    public void testDoInBackgroundEmpty() throws IOException {
        try {
            URL url = new URL("");
            BufferedReader bufferedReader = createReaderFromUrl(url);
            task.doInBackground(bufferedReader);
            fail("That cannot  be there");
        } catch (MalformedURLException e) {
            System.out.println("That's ok!");
        }
    }
    
    /**
     * Creates a {@link BufferedReader} from an {@link URL}
     * 
     * @param url the {@link URL} to use
     * @return the created {@link BufferedReader}
     */
    private BufferedReader createReaderFromUrl(URL url) {
        URLConnection connection;
        BufferedReader reader;
        try {
            connection = url.openConnection();
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            boolean isProxy = preferences.getBoolean("isProxy", false);
            if (isProxy) {
                boolean isProxyAuthentication = preferences.getBoolean("isProxyAuthentication", false);
                if (isProxyAuthentication) {
                    String user = preferences.getString("proxyUser", "");
                    String password = preferences.getString("proxyPassword", "");
                    String userPassword = user + ":" + password;
                    String encoded = Base64.encodeToString(userPassword.getBytes(), 0);
                    connection.setRequestProperty("Proxy-Authorization", "Basic " + encoded);
                }
            }
            InputStream inputStream = connection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            reader = new BufferedReader(inputStreamReader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return reader;
    }

}
