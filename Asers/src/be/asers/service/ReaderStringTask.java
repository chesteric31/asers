package be.asers.service;

import java.io.BufferedReader;
import java.io.IOException;

import android.os.AsyncTask;

/**
 * {@link AsyncTask} to retrieve some content from {@link BufferedReader}.
 * 
 * @author chesteric31
 */
public class ReaderStringTask extends AsyncTask<BufferedReader, Void, String> {

    /**
     * {@inheritDoc}
     */
    @Override
    protected String doInBackground(BufferedReader... params) {
        if (params == null) {
            throw new IllegalArgumentException("The url cannot be empty!");
        }
        StringBuilder stringBuilder = new StringBuilder();
        try {
            String line = null;
            BufferedReader bufferedReader = params[0];
            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim();
                stringBuilder.append(line);
                stringBuilder.append("\n");
            }
            bufferedReader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return stringBuilder.toString();
    }

}
