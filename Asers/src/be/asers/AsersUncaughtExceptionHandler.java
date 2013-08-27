package be.asers;

import java.lang.Thread.UncaughtExceptionHandler;

import be.asers.activity.BugReportActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Process;

/**
 * Exception Handler for {@link UncaughtExceptionHandler}.
 * 
 * @author chesteric31
 */
public class AsersUncaughtExceptionHandler implements UncaughtExceptionHandler {

    private Thread.UncaughtExceptionHandler defaultHandler;
    private Context context;

    /**
     * Constructor.
     * 
     * @param context the {@link Context}
     */
    public AsersUncaughtExceptionHandler(Context context) {
        this.defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        this.context = context;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void uncaughtException(Thread thread, Throwable exception) {
        Intent intent = new Intent(context, BugReportActivity.class);
        intent.putExtra(BugReportActivity.STACKTRACE, exception.getCause().getLocalizedMessage());
        context.startActivity(intent);

        Process.killProcess(Process.myPid());
//        System.exit(10);
        defaultHandler.uncaughtException(thread, exception);
    }

}
