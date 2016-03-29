package be.asers.task;

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
 * {@link AsyncTask} to retrieve some content from {@link URL}.
 * 
 * @author chesteric31
 */
public class UrlReaderTask extends AsyncTask<URL, Void, BufferedReader> {

    /** The context. */
    private Context context;

    /**
     * Constructor.
     * 
     * @param context the {@link Context}
     */
    public UrlReaderTask(Context context) {
        this.context = context;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BufferedReader doInBackground(URL... params) {
        if (params == null) {
            throw new IllegalArgumentException("The url cannot be empty!");
        }
        URLConnection connection;
        BufferedReader reader;
        try {
            connection = params[0].openConnection();
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
            reader = new BufferedReader(inputStreamReader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return reader;
    }

}
