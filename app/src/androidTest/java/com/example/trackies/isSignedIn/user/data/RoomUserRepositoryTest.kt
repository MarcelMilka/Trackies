package com.example.trackies.isSignedIn.user.data

//@OptIn(ExperimentalCoroutinesApi::class)
//@HiltAndroidTest
//@UninstallModules(UserRepositoryModule::class, RoomDatabaseProvider::class)
//class RoomUserRepositoryTest {
//
//    @get:Rule
//    var hiltRule = HiltAndroidRule(this)
//
//
//    @Before
//    fun beforeTest() {
//
//        hiltRule.inject()
//
//        AuthenticationServiceOperator.setRoomAuthenticationService()
//    }
//
//    @After
//    fun afterTest() {}
//
//    @Module
//    @InstallIn(SingletonComponent::class)
//    class RoomDatabaseProvider {
//
//        @Provides
//        @Singleton
//        fun provideRoomDatabase(@ApplicationContext appContext: Lazy<Context>): RoomDatabase =
//            Room.databaseBuilder(
//                appContext.get(),
//                RoomDatabase::class.java, com.example.globalConstants.Room.databaseName,
//            ).build()
//    }
//}