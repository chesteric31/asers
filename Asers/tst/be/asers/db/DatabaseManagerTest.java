package be.asers.db;

import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

/**
 * Test class for {@link DatabaseManager}.
 * 
 * @author chesteric31
 */
public class DatabaseManagerTest extends AndroidTestCase {

    private DatabaseManager databaseManager;

    /**
     * {@inheritDoc}
     * 
     * @throws Exception an {@link Exception}
     */
    @Override
    public void setUp() throws Exception {
        super.setUp();
        RenamingDelegatingContext context = new RenamingDelegatingContext(getContext(), "test_");
        databaseManager = new DatabaseManager(context);
    }

    /**
     * {@inheritDoc}
     * 
     * @throws Exception an {@link Exception}
     */
    public void tearDown() throws Exception {
        databaseManager.close();
        super.tearDown();
    }
    
    /**
     * Test method for {@link be.asers.db.DatabaseManager#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)}.
     */
    public void testOnUpgrade() {
        SQLiteDatabase database = databaseManager.getWritableDatabase();
        databaseManager.onUpgrade(database, 0, 1);
    }
}
