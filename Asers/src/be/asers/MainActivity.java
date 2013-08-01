package be.asers;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

/**
 * First activity of the project.
 *
 * @author chesteric31
 * @version $Revision$ $Date::                  $ $Author$
 */
public class MainActivity extends Activity {

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
