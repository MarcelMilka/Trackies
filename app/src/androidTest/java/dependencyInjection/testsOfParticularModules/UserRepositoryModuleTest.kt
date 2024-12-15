package dependencyInjection.testsOfParticularModules

import com.example.trackies.isSignedIn.user.data.UserRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class UserRepositoryModuleTest {

    @get:Rule
    val hiltAndroidRule = HiltAndroidRule(this)

    @Inject
    lateinit var userRepository: UserRepository

    @Before
    fun injectDependencies() {

        hiltAndroidRule.inject()
    }

    @Test
    fun dependencyIsInjectedProperly() {

        assertNotNull(userRepository)
    }
}