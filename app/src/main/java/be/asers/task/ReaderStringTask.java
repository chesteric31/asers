package be.asers.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;

/**
 * {@link AsyncTask} to retrieve some content from {@link BufferedReader}.
 * 
 * @author chesteric31
 */
public class ReaderStringTask extends AsyncTask<BufferedReader, Void, List<String>> {

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<String> doInBackground(BufferedReader... params) {
        if (params == null) {
            throw new IllegalArgumentException("The url cannot be empty!");
        }
        List<String> strings = new ArrayList<String>();
        try {
            String line = null;
            BufferedReader bufferedReader = params[0];
            boolean firstLineToSkip = true;
            while ((line = bufferedReader.readLine()) != null) {
                if (firstLineToSkip) {
                    firstLineToSkip = false;
                } else {
                    line = line.trim();
                    strings.add(line);
                }
            }
            bufferedReader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return strings;
    }

}
