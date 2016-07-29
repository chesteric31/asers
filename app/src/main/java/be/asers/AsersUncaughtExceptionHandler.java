package be.asers;

import java.lang.Thread.UncaughtExceptionHandler;

import android.content.Context;
import android.content.Intent;
import be.asers.activity.BugReportActivity;

/**
 * Exception Handler for {@link UncaughtExceptionHandler}.
 * 
 * @author chesteric31
 */
public class AsersUncaughtExceptionHandler implements UncaughtExceptionHandler {

    /** The context. */
    private Context context;

    /**
     * Constructor.
     * 
     * @param context the {@link Context}
     */
    public AsersUncaughtExceptionHandler(Context context) {
        this.context = context;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void uncaughtException(Thread thread, Throwable exception) {
        exception.printStackTrace();
        Intent intent = new Intent(context, BugReportActivity.class);
        Throwable cause = exception.getCause();
        StackTraceElement stackTraceElement = cause.getStackTrace()[0];
        intent.putExtra(BugReportActivity.STACKTRACE, cause.toString() + "\n" + stackTraceElement.toString());
        context.startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);
    }

}
