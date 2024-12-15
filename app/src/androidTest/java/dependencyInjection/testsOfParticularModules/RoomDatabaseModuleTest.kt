package dependencyInjection.testsOfParticularModules

import com.example.trackies.aRoom.db.RoomDatabase
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertNotNull
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class RoomDatabaseModuleTest {

    @get:Rule
    val hiltAndroidRule = HiltAndroidRule(this)

    @Inject
    lateinit var roomDatabase: RoomDatabase

    @Before
    fun injectDependencies() {

        hiltAndroidRule.inject()
    }

    @After
    fun closeDatabase() {

        roomDatabase.close()
    }

    @Test
    fun dependencyIsInjectedProperly() {

        assertNotNull(roomDatabase)
    }
}