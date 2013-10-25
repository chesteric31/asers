package be.asers.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import be.asers.R;

/**
 * Bug report activity to show all uncaught errors.
 * 
 * @author chesteric31
 */
public class BugReportActivity extends Activity {

    public static final String STACKTRACE = "asers.stacktrace";

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bug_report);
        final String stackTrace = getIntent().getStringExtra(STACKTRACE);
        final TextView report = (TextView) findViewById(R.id.report_text);
        report.setMovementMethod(ScrollingMovementMethod.getInstance());
        report.setClickable(false);
        report.setLongClickable(false);

        final String versionName = getVersionName();
        report.append("ASERS " + versionName + " has been crashed, sorry.\n");
        report.append(stackTrace);

        findViewById(R.id.ok_button).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent mainActivity = new Intent(BugReportActivity.this, MainActivity.class);
                startActivityIfNeeded(mainActivity, 0);
                finish();
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.bug_report, menu);
        return true;
    }

    /**
     * @return the version name of the application, empty string if an error occurred
     */
    private String getVersionName() {
        try {
            return getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (Exception e) {
            return "";
        }
    }
}
