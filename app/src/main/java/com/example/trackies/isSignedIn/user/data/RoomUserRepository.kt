package com.example.trackies.isSignedIn.user.data

import android.util.Log
import com.example.globalConstants.CurrentTime
import com.example.globalConstants.DaysOfWeek
import com.example.globalConstants.annotationClasses.Tested
import com.example.trackies.aRoom.db.RoomDatabase
import com.example.trackies.isSignedIn.user.buisness.LicenseModel
import com.example.trackies.isSignedIn.user.buisness.entities.License
import com.example.trackies.isSignedIn.user.buisness.entities.Regularity
import com.example.trackies.isSignedIn.user.buisness.entities.convertLicenseToLicenseModel
import com.example.trackies.isSignedIn.user.buisness.entities.convertTrackieToTrackieModel
import com.example.trackies.isSignedIn.xTrackie.buisness.TrackieModel
import com.example.trackies.isSignedIn.xTrackie.buisness.convertTrackieModelToTrackieEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class RoomUserRepository @Inject constructor(
    var roomDatabase: RoomDatabase
): UserRepository {

    init {

        Log.d("Halla!", "init")

        CoroutineScope(Dispatchers.Default).launch {

            val license =
                roomDatabase
                .licenseDAO()
                .getLicense()

            if (license != null && license.isSignedIn == false) {

                roomDatabase
                    .licenseDAO()
                    .signIn()
            }
        }
    }

    @Tested
    override suspend fun isFirstTimeInTheApp(): Boolean? {

        val license = roomDatabase.licenseDAO().isFirstTimeInTheApp()

        if (license == null) {

            addNewUser()
        }

        return true
    }

    @Tested
    override fun addNewUser() {

        CoroutineScope(Dispatchers.Default).launch {

            roomDatabase
                .licenseDAO()
                .createLicense(
                    license = License(
                        first = 1,
                        isSignedIn = true,
                        totalAmountOfTrackies = 0
                    )
                )
        }
    }

    @Tested
    override suspend fun needToResetPastWeekActivity(): Boolean? {

        val weeklyRegularity =
            roomDatabase
                .regularityDAO()
                .fetchWeeklyRegularity()

        return try {

            if (weeklyRegularity != null) {

                val currentDayOfWeek = CurrentTime.getCurrentDayOfWeek()

                var passedCurrentDayOfWeek = false
                var daysOfWeekToCheck = mutableSetOf<String>()

                var resetPastWeekRegularity = false

                setOf(
                    DaysOfWeek.monday,
                    DaysOfWeek.tuesday,
                    DaysOfWeek.wednesday,
                    DaysOfWeek.thursday,
                    DaysOfWeek.friday,
                    DaysOfWeek.saturday,
                    DaysOfWeek.sunday,
                ).forEach { dayOfWeek ->

                    if (passedCurrentDayOfWeek) {

                        daysOfWeekToCheck.add(element = dayOfWeek)
                    }

                    else {

                        passedCurrentDayOfWeek =
                            currentDayOfWeek == dayOfWeek
                    }
                }

                weeklyRegularity
                    .forEach { regularity ->

                        if (daysOfWeekToCheck.contains(regularity.dayOfWeek)) {

                            if (regularity.ingested) {
                                resetPastWeekRegularity = true
                            }
                        }
                    }

                resetPastWeekRegularity
            }

            else {

                null
            }

        }

        catch (e: Exception) {

            null
        }
    }

    @Tested
    override suspend fun resetWeeklyRegularity(
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {

        try {

            setOf(
                DaysOfWeek.monday,
                DaysOfWeek.tuesday,
                DaysOfWeek.wednesday,
                DaysOfWeek.thursday,
                DaysOfWeek.friday,
                DaysOfWeek.saturday,
                DaysOfWeek.sunday,
            ).forEach { dayOfWeek ->

                roomDatabase
                    .regularityDAO()
                    .resetRegularity(
                        dayOfWeek = dayOfWeek
                    )
            }

            onSuccess()
        }

        catch (e: Exception) {

            onFailure("$e")
        }
    }

    @Tested
    override suspend fun fetchUsersLicense(): LicenseModel? {

        val licenseEntity = roomDatabase.licenseDAO().getLicense()

        val nullableLicenseModel = licenseEntity?.convertLicenseToLicenseModel()

        return nullableLicenseModel
    }

    @Tested
    override suspend fun fetchTrackiesForToday(onFailure: (String) -> Unit): List<TrackieModel>? {

        val currentDayOfWeek = CurrentTime.getCurrentDayOfWeek()

        val entitiesOfTrackiesForToday = roomDatabase
            .trackiesDAO()
            .getTrackiesForToday(currentDayOfWeek)

        val nullableListOfTrackiesForToday =
            entitiesOfTrackiesForToday
                ?.map { trackie ->
                    trackie.convertTrackieToTrackieModel()
                }

        return nullableListOfTrackiesForToday
    }

    @Tested
    override suspend fun fetchAllTrackies(
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ): List<TrackieModel>? {

        return try {

            roomDatabase
                .trackiesDAO()
                .getAllTrackies()
                    .map {
                        it.convertTrackieToTrackieModel()
                    }
        }

        catch (e: Exception) {
            onFailure("$e")
            null
        }
    }

    @Tested
    override suspend fun fetchStatesOfTrackiesForToday(onFailure: (String) -> Unit): Map<String, Boolean>? {

        val currentDayOfWeek = CurrentTime.getCurrentDayOfWeek()

        val regularity = roomDatabase
            .regularityDAO()
            .fetchStatesOfTrackiesForToday(currentDayOfWeek = currentDayOfWeek)

        if (regularity != null) {

            var mapOfRegularity = mutableMapOf<String, Boolean>()

            regularity.forEach { regularityEntity ->

                mapOfRegularity.put(
                    key = regularityEntity.name,
                    value = regularityEntity.ingested
                )
            }

            return mapOfRegularity
        }

        else {

            return null
        }
    }

    @Tested
    override suspend fun fetchWeeklyRegularity(
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ): Map<String, Map<Int, Int>>? {

        val regularity = roomDatabase
            .regularityDAO()
            .fetchWeeklyRegularity()

        if (regularity != null) {

            var mondayTotal = 0
            var mondayIngested = 0

            var tuesdayTotal = 0
            var tuesdayIngested = 0

            var wednesdayTotal = 0
            var wednesdayIngested = 0

            var thursdayTotal = 0
            var thursdayIngested = 0

            var fridayTotal = 0
            var fridayIngested = 0

            var saturdayTotal = 0
            var saturdayIngested = 0

            var sundayTotal = 0
            var sundayIngested = 0

            regularity.forEach { regularityEntity ->

                when (regularityEntity.dayOfWeek) {

                    DaysOfWeek.monday -> {

                        mondayTotal++
                        if (regularityEntity.ingested) {
                            mondayIngested++
                        }
                    }

                    DaysOfWeek.tuesday -> {

                        tuesdayTotal++
                        if (regularityEntity.ingested) {
                            tuesdayIngested++
                        }
                    }

                    DaysOfWeek.wednesday -> {

                        wednesdayTotal++
                        if (regularityEntity.ingested) {
                            wednesdayIngested++
                        }
                    }

                    DaysOfWeek.thursday -> {

                        thursdayTotal++
                        if (regularityEntity.ingested) {
                            thursdayIngested++
                        }
                    }

                    DaysOfWeek.friday -> {

                        fridayTotal++
                        if (regularityEntity.ingested) {
                            fridayIngested++
                        }
                    }

                    DaysOfWeek.saturday -> {

                        saturdayTotal++
                        if (regularityEntity.ingested) {
                            saturdayIngested++
                        }
                    }

                    DaysOfWeek.sunday -> {

                        sundayTotal++
                        if (regularityEntity.ingested) {
                            sundayIngested++
                        }
                    }
                }
            }

            return mapOf(
                DaysOfWeek.monday to mapOf(mondayTotal to mondayIngested),
                DaysOfWeek.tuesday to mapOf(tuesdayTotal to tuesdayIngested),
                DaysOfWeek.wednesday to mapOf(wednesdayTotal to wednesdayIngested),
                DaysOfWeek.thursday to mapOf(thursdayTotal to thursdayIngested),
                DaysOfWeek.friday to mapOf(fridayTotal to fridayIngested),
                DaysOfWeek.saturday to mapOf(saturdayTotal to saturdayIngested),
                DaysOfWeek.sunday to mapOf(sundayTotal to sundayIngested)
            )
        }

        else {

            return null
        }
    }

    @Tested
    override suspend fun addNewTrackie(
        trackieModel: TrackieModel,
        onFailure: (String) -> Unit
    ) {

        val license = roomDatabase
            .licenseDAO()
            .getLicense()

        if (license != null) {

//          Add Trackie to the table "Trackies"
            val trackie = trackieModel.convertTrackieModelToTrackieEntity()
            roomDatabase
                .trackiesDAO()
                .addNewTrackie(trackie = trackie)

//          Increase total amount of trackies in the table "License"
            val totalAmountOfTrackies = license.totalAmountOfTrackies + 1
            roomDatabase
                .licenseDAO()
                .increaseTotalAmountOfTrackiesByOne(totalAmountOfTrackies = totalAmountOfTrackies)

//          Add Trackie to the "Regularity" table.
            trackieModel
                .repeatOn
                .forEach { dayOfWeek ->
                roomDatabase
                    .regularityDAO()
                    .addTrackieToRegularity(
                        regularity = Regularity(
                            name = trackieModel.name,
                            dayOfWeek = dayOfWeek,
                            ingested = false
                        )
                    )
            }
        }

        else {

            onFailure("License is null")
        }
    }

    @Tested
    override suspend fun deleteTrackie(
        trackieModel: TrackieModel,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {

        val license = roomDatabase
            .licenseDAO()
            .getLicense()

        if (license != null) {

//          Delete Trackie from the table "Trackies"
            roomDatabase
                .trackiesDAO()
                .deleteTrackie(
                    nameOfTrackie = trackieModel.name
                )

//          Decrease total amount of trackies in the table "License" by one
            val totalAmountOfTrackies = license.totalAmountOfTrackies - 1
            roomDatabase
                .licenseDAO()
                .decreaseTotalAmountOfTrackiesByOne(
                    totalAmountOfTrackies = totalAmountOfTrackies
                )

//          Delete Trackie from the "Regularity" table.
            roomDatabase
                .regularityDAO()
                .deleteTrackieFromRegularity(
                    nameOfTrackie = trackieModel.name
                )

        }

        else {

            onFailure("License is null")
        }
    }

    @Tested
    override suspend fun markTrackieAsIngested(
        currentDayOfWeek: String,
        trackieModel: TrackieModel,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {

        try {

            roomDatabase
                .regularityDAO()
                .markTrackieAsIngested(
                    rowToUpdate = Regularity(
                        name = trackieModel.name,
                        dayOfWeek = currentDayOfWeek,
                        ingested = true
                    )
                )
        }

        catch (e: Exception) {

            onFailure("$e")
        }
    }

    override suspend fun deleteUsersData() {}

    override suspend fun fetchNamesOfTrackies(dayOfWeek: String): List<String>? = null

    override suspend fun fetchNamesOfAllTrackies(): MutableList<String>? = null

    override suspend fun fetchTodayTrackies(): List<TrackieModel>? = null
}