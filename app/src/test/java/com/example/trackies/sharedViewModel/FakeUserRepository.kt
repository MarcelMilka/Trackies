package com.example.trackies.sharedViewModel

import com.example.trackies.isSignedIn.constantValues.DaysOfWeek
import com.example.trackies.isSignedIn.trackie.TrackieViewState
import com.example.trackies.isSignedIn.user.buisness.licenseViewState.LicenseViewState
import com.example.trackies.isSignedIn.user.data.UserRepository

class FakeUserRepository: UserRepository {

    override fun firstTimeInTheApp(anErrorOccurred: () -> Unit) {}

    override fun addNewUser() {}

    override suspend fun fetchUsersLicense(): LicenseViewState? {

        val error = null

        val inactiveLicenseViewState = LicenseViewState(
            active = false,
            validUntil = null,
            totalAmountOfTrackies = 0
        )

        val activeLicenseViewState = LicenseViewState(
            active = true,
            validUntil = "01.01.2025",
            totalAmountOfTrackies = 3
        )

        return error
    }

    override suspend fun fetchNamesOfTrackies(dayOfWeek: String): List<String>? {

        val error = null

        val emptyList = emptyList<String>()

        val listWithData = listOf("X", "Y", "Z")

        return error
    }

    override suspend fun fetchNamesOfAllTrackies(): MutableList<String>? {

        val error = null

        val emptyList = emptyList<String>()

        val listWithData = mutableListOf("X", "Y", "Z", "Alfa", "Beta", "Gamma")

        return listWithData
    }

    override suspend fun fetchTodayTrackies(): List<TrackieViewState>? = null

    override suspend fun addNewTrackie(
        trackieViewState: TrackieViewState,
        onFailure: (String) -> Unit
    ) {}

    override suspend fun fetchTrackiesForToday(onFailure: (String) -> Unit): List<TrackieViewState>? {

        val error = null

        val emptyList = emptyList<TrackieViewState>()

        val listWithData = listOf(

            TrackieViewState(
                name= "X",
                totalDose=2,
                measuringUnit= "pcs",
                repeatOn= listOf("monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday"),
                ingestionTime=null
            ),

            TrackieViewState(
                name = "Y",
                totalDose = 1,
                measuringUnit = "pcs",
                repeatOn = listOf("monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday"),
                ingestionTime = null
            ),

            TrackieViewState(
                name = "Z",
                totalDose = 10,
                measuringUnit = "pcs",
                repeatOn = listOf("monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday"),
                ingestionTime = null
            )
        )

        return null
    }

    override suspend fun fetchStatesOfTrackiesForToday(onFailure: (String) -> Unit): Map<String, Boolean>? {

        val error = null

        val emptyMap = mapOf<String, Boolean>()

        val mapWithData = mapOf<String, Boolean>(
            "X" to true,
            "Y" to true,
            "Z" to false,
        )

        return error
    }

    override suspend fun deleteTrackie(
        trackieViewState: TrackieViewState,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {}

    override suspend fun fetchAllTrackies(
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ): List<TrackieViewState>? = null

    override suspend fun fetchWeeklyRegularity(
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ): Map<String, Map<Int, Int>>? {

        val error = null

        val emptyMap = mapOf<String, Map<Int, Int>>()

        val mapWithData = mutableMapOf<String, Map<Int, Int>> (
            "monday" to mapOf(5 to 0), // total to ingested
            "tuesday" to mapOf(5 to 0),
            "wednesday" to mapOf(5 to 0),
            "thursday" to mapOf(5 to 0),
            "friday" to mapOf(5 to 0),
            "saturday" to mapOf(5 to 0),
            "sunday" to mapOf(5 to 0),
        )

        return error
    }

    override suspend fun markTrackieAsIngested(
        trackieViewState: TrackieViewState,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {}
}

//statesOfTrackiesForToday = {Esticalopram=true, Vit. D3=true, Evastix=true}
//weeklyRegularity = {monday={3=0}, tuesday={3=0}, wednesday={3=0}, thursday={3=3}, friday={3=3}, saturday={3=3}, sunday={3=3}}