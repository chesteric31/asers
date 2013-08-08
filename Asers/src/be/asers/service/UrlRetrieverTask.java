package be.asers.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Base64;

/**
 * {@link AsyncTask} to retrieve some content from URLs.
 * 
 * @author chesteric31
 */
public class UrlRetrieverTask extends AsyncTask<URL, Void, String> {

    private Context context;

    /**
     * Constructor.
     * 
     * @param context the {@link Context}
     */
    public UrlRetrieverTask(Context context) {
        this.context = context;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String doInBackground(URL... params) {
        if (params == null) {
            throw new IllegalArgumentException("The url cannot be empty!");
        }
        BufferedReader reader = null;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            reader = retrieveReaderFromUrl(params[0]);
            String line = null;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                stringBuilder.append(line);
                stringBuilder.append("\n");
            }
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return stringBuilder.toString();
    }

    /**
     * Retrieves a {@link BufferedReader} from an URL object.
     * 
     * @param url the URL to use
     * @return the {@link BufferedReader} URL content
     * @throws IOException if an error occurred
     */
    private BufferedReader retrieveReaderFromUrl(URL url) throws IOException {
        URLConnection connection = url.openConnection();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.context);
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
        BufferedReader reader = new BufferedReader(inputStreamReader);
        return reader;
    }

}
