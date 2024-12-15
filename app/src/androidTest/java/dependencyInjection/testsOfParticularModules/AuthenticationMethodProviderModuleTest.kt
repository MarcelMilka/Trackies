package dependencyInjection.testsOfParticularModules

import com.example.trackies.auth.authenticationMethodProvider.AuthenticationMethodProvider
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class AuthenticationMethodProviderModuleTest {

    @get:Rule
    val hiltAndroidRule = HiltAndroidRule(this)

    @Inject
    lateinit var authenticationMethodProvider: AuthenticationMethodProvider

    @Before
    fun injectDependencies() {

        hiltAndroidRule.inject()
    }

    @Test
    fun dependencyIsInjectedProperly() {

        assertNotNull(authenticationMethodProvider)
    }
}