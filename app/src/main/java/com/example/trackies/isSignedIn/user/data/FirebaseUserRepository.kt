package com.example.trackies.isSignedIn.user.data

import android.util.Log
import com.example.globalConstants.DaysOfWeek
import com.example.trackies.isSignedIn.xTrackie.buisness.TrackieModel
import com.example.trackies.isSignedIn.xTrackie.buisness.TrackieModelEntity
import com.example.trackies.isSignedIn.xTrackie.buisness.convertEntityToTrackieModel
import com.example.trackies.isSignedIn.user.buisness.LicenseModel
import com.example.trackies.isSignedIn.user.buisness.LicenseModelEntity
import com.example.trackies.isSignedIn.user.buisness.convertEntityToLicenseModel
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseUserRepository @Inject constructor(
    var uniqueIdentifier: String
): UserRepository {

    val firebase: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val users = firebase.collection("users")
    private val user = users.document(uniqueIdentifier)
    private val usersLicense = user.collection("user's information").document("license")
    private val usersTrackies = user.collection("user's trackies").document("trackies")
    private val namesOfTrackies = user.collection("names of trackies").document("names of trackies")
    private val usersWeeklyStatistics = user.collection("user's statistics").document("user's weekly statistics")

    override suspend fun needToResetPastWeekRegularity(currentDayOfWeek: String): Boolean? {

        val weeklyRegularity = fetchWeeklyRegularity()

        return suspendCoroutine {

            if (weeklyRegularity != null) {

                var resetPastWeekActivity = false
                var passedCurrentDayOfWeek = false

                weeklyRegularity.forEach {

                    if (!resetPastWeekActivity) {

                        if (passedCurrentDayOfWeek) {

                            val totalIngestedRatio = it.value

                            resetPastWeekActivity =
                                ((totalIngestedRatio as MutableMap<Int, Int>).values.toIntArray()[0]) > 0
                        }

                        if (!passedCurrentDayOfWeek) {
                            passedCurrentDayOfWeek = currentDayOfWeek == it.key
                        }
                    }
                }

                if (resetPastWeekActivity) {

                    it.resume(
                        value = true
                    )
                }

                else {

                    it.resume(
                        value = false
                    )
                }
            }

            else {

                it.resume(
                    value = null
                )
            }
        }
    }

    override suspend fun resetWeeklyRegularity(currentDayOfWeek: String): Boolean {

        var passedCurrentDayOfWeek = false

        var resetIsSuccessful = true

        return try {

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

                    val namesOfTrackiesForThisDayOfWeek = fetchNamesOfTrackies(dayOfWeek)

                    if (namesOfTrackiesForThisDayOfWeek != null) {

                        namesOfTrackiesForThisDayOfWeek.forEach { nameOfTrackie ->

                            usersWeeklyStatistics
                                .collection(dayOfWeek)
                                .document(nameOfTrackie)
                                .update("ingested", false)
                        }
                    }

                    else {

                        resetIsSuccessful = false
                    }
                }

                else {

                    passedCurrentDayOfWeek = currentDayOfWeek == dayOfWeek
                }
            }

            resetIsSuccessful
        }

        catch (e: Exception) {

            Log.d("FirebaseUserRepository error", "$e")
            false
        }
    }

//  This method is responsible for checking if the user's unique identifier exists in the database.
//  If the unique identifier does not exist - a new document named after the user's unique identifier will be created.
    override suspend fun isFirstTimeInTheApp(): Boolean? {

        return suspendCoroutine { continuation ->

            users.document(uniqueIdentifier)
                .get()
                .addOnSuccessListener { user ->

                    if (!(user.exists())) {

                        addNewUser()
                        continuation.resume(
                            value = true
                        )
                    }

                    else {

                        continuation.resume(
                            value = false
                        )
                    }
                }
                .addOnFailureListener {

                    continuation.resume(
                        value = null
                    )
                }
        }
    }


//  This method gets called when the function 'firstTimeInTheApp' detects there's not any document named after the user's unique identifier.
//  Creates a document named after the user's unique identifier which contains all the user's data to be used in this application.
    override fun addNewUser() {

        Log.d("Halla!", "Adding new user.")
        users
            .document(uniqueIdentifier)
            .set({})
            .continueWith {
                val update = hashMapOf<String, Any>(
                    "arity" to FieldValue.delete()
                )
                user.update(update)
            }

//      "user's information" -> "license"
        usersLicense.set(LicenseModel(active = false, validUntil = null, totalAmountOfTrackies = 0))

//      "names of trackies" -> "names of trackies"
        namesOfTrackies.set(hashMapOf("whole week" to listOf<String>()))
        namesOfTrackies.update(DaysOfWeek.monday, listOf<String>() )
        namesOfTrackies.update(DaysOfWeek.tuesday, listOf<String>())
        namesOfTrackies.update(DaysOfWeek.wednesday, listOf<String>())
        namesOfTrackies.update(DaysOfWeek.thursday, listOf<String>())
        namesOfTrackies.update(DaysOfWeek.friday, listOf<String>())
        namesOfTrackies.update(DaysOfWeek.saturday, listOf<String>())
        namesOfTrackies.update(DaysOfWeek.sunday, listOf<String>())

//      "user's trackies" -> "trackies"
        usersTrackies
            .set({})
            .continueWith { usersTrackies.update(hashMapOf<String, Any>("arity" to FieldValue.delete())) }

//      "user's statistics" -> "user's weekly statistics"
        usersWeeklyStatistics
            .set({})
            .continueWith {
                usersWeeklyStatistics.update(hashMapOf<String, Any>("arity" to FieldValue.delete()))
            }
    }


//  This method retrieves information about the user's license which contains following information:
//  - 'active' determines if the user has premium account
//  - 'totalAmountOfTrackies' (when user does not have a premium account, there's a limited amount of trackies)
//  - 'validUntil' determines how long the premium account is active
//  license information gets returned as data class LicenseViewState
    override suspend fun fetchUsersLicense(): LicenseModel? {

        return suspendCoroutine { continuation ->

            usersLicense
                .get()
                .addOnSuccessListener { document ->

                    val licenseViewState = document.toObject(LicenseModelEntity::class.java)

                    if (licenseViewState != null) {

                        LicenseModelEntity(
                            active = licenseViewState.active,
                            validUntil = licenseViewState.validUntil,
                            totalAmountOfTrackies = licenseViewState.totalAmountOfTrackies
                        ).let { licenseViewStateEntity ->

                            if (licenseViewStateEntity.active != null && licenseViewStateEntity.totalAmountOfTrackies != null) {

                                val licenseViewState = licenseViewStateEntity.convertEntityToLicenseModel()
                                continuation.resume(licenseViewState)
                            }

                            else {
                                continuation.resume(null)
                            }
                        }
                    }

                    else {
                        return@addOnSuccessListener
                    }
                }
                .addOnFailureListener {
                    continuation.resume(null)
                }
        }
    }


//  This method fetches names of all Trackies which are assigned to a day of week passed as a parameter 'dayOfWeek'.
//  E.g. when 'monday' gets passed, the method fetches names of all Trackies assigned to monday.
//  This method helps the method 'fetchTodayTrackies' to fetch information about trackie.
    override suspend fun fetchNamesOfTrackies(dayOfWeek: String): List<String>? {

        return suspendCoroutine { continuation ->

            namesOfTrackies
                .get()
                .addOnSuccessListener { document ->

                    if (document.exists()) {

                        val namesOfTrackies = document.get(dayOfWeek) as? List<String>

                        if (namesOfTrackies != null) {
                            continuation.resume(namesOfTrackies)
                        }

                        else {
                            continuation.resume(null)
                        }
                    }

                    else {
                        continuation.resume(null)
                    }
                }
                .addOnFailureListener {
                    continuation.resume(null)
                }
        }
    }


//  This method is responsible for fetching all existing names of trackies created by the user.
    override suspend fun fetchNamesOfAllTrackies(): MutableList<String>? =
        fetchNamesOfTrackies(dayOfWeek = "whole week")?.toMutableList()


//  This method is responsible for adding new trackie to the user's database.
    override suspend fun addNewTrackie(trackieModel: TrackieModel): Boolean {

        val licenseViewState = fetchUsersLicense() ?: return false

        return try {

            firebase.runBatch { batch ->


//              add new trackie to 'user's trackies' -> 'trackies'
                val collectionTrackies =
                    usersTrackies
                    .collection(trackieModel.name)
                    .document(trackieModel.name)

                batch.set(
                    collectionTrackies,
                    trackieModel
                )


//              update total amount of trackies owned by the user 'user's information' -> 'license'
                val updatedTotalAmountOfTrackies = (licenseViewState.totalAmountOfTrackies + 1)

                batch.update(
                    usersLicense,
                    "totalAmountOfTrackies",
                    updatedTotalAmountOfTrackies
                )

//              add name of the trackie to 'names of trackies' -> 'names of trackies' -> 'whole week'
                batch.update(
                    namesOfTrackies,
                    "whole week",
                    FieldValue.arrayUnion(trackieModel.name)
                )

//              add name of the trackie to 'names of trackies' -> '(names of trackies)' -> *specific day of week*
                trackieModel
                    .repeatOn
                    .forEach { dayOfWeek ->

                        batch
                            .update(
                                namesOfTrackies,
                                dayOfWeek,
                                FieldValue.arrayUnion(trackieModel.name)
                            )
                    }

//              add name of the trackies to 'user's statistics' -> 'user's weekly statistics' -> *specific day of week*
                setOf(
                    DaysOfWeek.monday,
                    DaysOfWeek.tuesday,
                    DaysOfWeek.wednesday,
                    DaysOfWeek.thursday,
                    DaysOfWeek.friday,
                    DaysOfWeek.saturday,
                    DaysOfWeek.sunday
                ).forEach { dayOfWeek ->

                    if (trackieModel.ingestionTime == null) {

                        if (trackieModel.repeatOn.contains(dayOfWeek)) {

                            val fieldToSave = mutableMapOf<String, Boolean>()

                            fieldToSave["ingested"] = false

                            val statisticsDocument =
                                usersWeeklyStatistics
                                .collection(dayOfWeek)
                                .document(trackieModel.name)

                            batch.set(
                                statisticsDocument,
                                fieldToSave
                            )
                        }
                    }
                }
            }.await()

            true
        }

        catch (e: Exception) {

            Log.d("FirebaseUserRepository error", "$e")
            false
        }
    }


//  This method is responsible for fetching all the user's trackies assigned to the current day of week.
    override suspend fun fetchTrackiesForToday(currentDayOfWeek: String): List<TrackieModel>? {

        val namesOfTrackiesForToday: List<String>? =
            fetchNamesOfTrackies(dayOfWeek = currentDayOfWeek)

        val trackiesForToday: MutableList<TrackieModel> = mutableListOf()

        return suspendCoroutine { continuation ->

            if (namesOfTrackiesForToday != null) {

                if (namesOfTrackiesForToday.isNotEmpty()) {

                    val tasks = namesOfTrackiesForToday.map { nameOfTheTrackie ->

                        usersTrackies.collection(nameOfTheTrackie).document(nameOfTheTrackie)
                            .get()
                            .addOnSuccessListener { document ->

                                val trackieViewStateEntity = document.toObject(TrackieModelEntity::class.java)

                                if (trackieViewStateEntity != null) {

                                    val trackieViewState = trackieViewStateEntity.convertEntityToTrackieModel()

                                    try {
                                        trackiesForToday.add(trackieViewState)
                                    }

                                    catch (e: Exception) {

                                        Log.d("FirebaseUserRepository error", "$e")
                                        continuation.resume(
                                            value = null
                                        )
                                    }
                                }
                            }

                            .addOnFailureListener { exception ->

                                continuation.resume(
                                    value = null
                                )
                            }
                    }

                    Tasks.whenAllComplete(tasks).addOnCompleteListener {
                        continuation.resume(value = trackiesForToday)
                    }
                }

                else {
                    continuation.resume(value = listOf())
                }
            }

            else {
                continuation.resume(value = null)
            }
        }
    }


    override suspend fun fetchStatesOfTrackiesForToday(currentDayOfWeek: String): Map<String, Boolean>? {

        val namesOfTrackiesForToday: List<String>? =
            fetchNamesOfTrackies(dayOfWeek = currentDayOfWeek)

        var namesAndStatesOfTrackies = mutableMapOf<String, Boolean>()

        return suspendCoroutine { continuation ->

            if (namesOfTrackiesForToday != null) {

                if (namesOfTrackiesForToday.isNotEmpty()) {

                    namesOfTrackiesForToday.onEach {nameOfTheTrackie ->

                        usersWeeklyStatistics
                            .collection(currentDayOfWeek)
                            .document(nameOfTheTrackie)
                            .get()
                            .addOnSuccessListener {document ->

                                document.getBoolean("ingested").let { stateOfTheTrackie ->

                                    if (stateOfTheTrackie != null) {

                                        namesAndStatesOfTrackies[nameOfTheTrackie] = stateOfTheTrackie

                                        if (namesAndStatesOfTrackies.size == namesOfTrackiesForToday.size) {

                                            continuation.resume(
                                                value = namesAndStatesOfTrackies
                                            )
                                        }
                                    }

                                    else {

                                        continuation.resume(
                                            value = null
                                        )
                                    }
                                }
                            }

                            .addOnFailureListener {

                                continuation.resume(
                                    value = null
                                )
                            }
                    }
                }

                else {

                    continuation.resume(
                        value = emptyMap()
                    )
                }
            }

            else {

                continuation.resume(
                    value = null
                )
            }
        }
    }


    override suspend fun deleteTrackie(trackieModel: TrackieModel): Boolean {

        val licenseViewState = fetchUsersLicense() ?: return false

        return try {

            firebase.runBatch { batch ->

//              1: Deleting a document named after the Trackie
//                 route: 'user's trackies' -> 'trackies' -> '${TrackieViewState.name}' -> '${TrackieViewState.name}
                batch.delete(
                    usersTrackies
                        .collection(trackieModel.name)
                        .document(trackieModel.name)
                )

//              2: Decreasing total amount of Trackies by one.
//                 route: "user's information" -> 'license'
                val totalAmountOfTrackies = (licenseViewState.totalAmountOfTrackies - 1)
                batch.update(
                    usersLicense,
                    "totalAmountOfTrackies",
                    totalAmountOfTrackies
                )

//              3: Deleting name of the Trackie from the array which contains names of all the user's Trackies.
//                  route: 'names of trackies' -> 'names of trackies'
                batch.update(
                    namesOfTrackies,
                    "whole week",
                    FieldValue.arrayRemove(trackieModel.name)
                )

//              4: Deleting name of the Trackie from the array which contains names of the user's Trackies assigned for a particular day of week
//                 route: 'names of trackies' -> 'names of trackies'
                trackieModel.repeatOn.forEach { dayOfWeek ->

                    batch.update(
                        namesOfTrackies,
                        dayOfWeek,
                        FieldValue.arrayRemove(trackieModel.name)
                    )
                }

//              5: Removing Trackie from the weekly regularity:
                setOf(
                    DaysOfWeek.monday,
                    DaysOfWeek.tuesday,
                    DaysOfWeek.wednesday,
                    DaysOfWeek.thursday,
                    DaysOfWeek.friday,
                    DaysOfWeek.saturday,
                    DaysOfWeek.sunday
                ).forEach { dayOfWeek ->

                    batch.delete(
                        usersWeeklyStatistics
                            .collection(dayOfWeek)
                            .document(trackieModel.name)
                    )
                }
            }.await()

            true
        }

        catch (e: Exception) {

            Log.d("FirebaseUserRepository error", "$e")
            false
        }
    }


    override suspend fun fetchAllTrackies(): List<TrackieModel>? {

        val namesOfAllTrackies: List<String>? = fetchNamesOfTrackies("whole week")
        val allTrackies: MutableList<TrackieModel> = mutableListOf()

        return suspendCoroutine { continuation ->

            if (namesOfAllTrackies != null) {

                if (namesOfAllTrackies.isNotEmpty()) {

                    val tasks = namesOfAllTrackies.map { nameOfTrackie ->

                        usersTrackies.collection(nameOfTrackie).document(nameOfTrackie)
                            .get()
                            .addOnSuccessListener { document ->

                                val trackieViewStateEntity = document.toObject(TrackieModelEntity::class.java)

                                if (trackieViewStateEntity != null) {

                                    val trackieViewState = trackieViewStateEntity.convertEntityToTrackieModel()

                                    try {

                                        allTrackies.add(
                                            element = trackieViewState
                                        )
                                    }

                                    catch (e: Exception) {

                                        Log.d("FirebaseUserRepository error", "$e")
                                        continuation.resume(
                                            value = null
                                        )
                                    }
                                }
                            }
                            .addOnFailureListener {

                                continuation.resume(
                                    value = null
                                )
                            }
                    }

                    Tasks.whenAllComplete(tasks).addOnCompleteListener {

                        continuation.resume(
                            value = allTrackies
                        )
                    }
                }

                else {

                    continuation.resume(
                        value = listOf<TrackieModel>()
                    )
                }
            }

            else {

                continuation.resume(
                    value = null
                )
            }
        }
    }


    override suspend fun fetchWeeklyRegularity(): Map<String, Map<Int, Int>>? {

        val daysOfWeek = listOf(
            DaysOfWeek.monday,
            DaysOfWeek.tuesday,
            DaysOfWeek.wednesday,
            DaysOfWeek.thursday,
            DaysOfWeek.friday,
            DaysOfWeek.saturday,
            DaysOfWeek.sunday
        )

        return try {

            withContext(Dispatchers.Default) {

                val weeklyRegularityAsList = daysOfWeek.map {

                    async {

                        val dailyRegularity =
                            fetchDailyRegularity(dayOfWeek = it)

                        if (dailyRegularity != null) {

                            mapOf(
                                it to dailyRegularity
                            )
                        }

                        else {

                            return@async null
                        }
                    }
                }.awaitAll()

                val weeklyRegularity = weeklyRegularityAsList
                    .filterNotNull() // removing null values
                    .flatMap {

                        it.entries
                    }
                    .associate {

                        it.toPair()
                    }

                if (weeklyRegularity.keys.containsAll(daysOfWeek)) {

                    return@withContext weeklyRegularity
                }

                else {

                    return@withContext null
                }
            }
        }

        catch (e: Exception) {

            Log.d("FirebaseUserRepository error", "$e")
            return null
        }
    }



    private suspend fun fetchDailyRegularity(dayOfWeek: String): Map<Int, Int>? {

        return try {

            withContext(Dispatchers.Default) {

                val namesOfTrackies =
                    fetchNamesOfTrackies(dayOfWeek = dayOfWeek)

                if (namesOfTrackies != null) {

                    var totalAmountOfTrackies = 0
                    var ingestedAmountOfTrackies = 0

                    for (nameOfTrackie in namesOfTrackies) {
                        val task = usersWeeklyStatistics
                            .collection(dayOfWeek)
                            .document(nameOfTrackie)
                            .get()
                            .await() // Using kotlinx-coroutines-play-services for Firebase compatibility

                        val ingested = task.getBoolean("ingested")

                        if (ingested != null) {

                            totalAmountOfTrackies++

                            if (ingested) {

                                ingestedAmountOfTrackies++
                            }
                        }
                    }

                    for (nameOfTrackie in namesOfTrackies) {

                        usersWeeklyStatistics
                            .collection(dayOfWeek)
                            .document(nameOfTrackie)
                            .get()
                            .addOnSuccessListener {

                                val ingested: Boolean? =
                                    it.getBoolean("ingested")

                                ingested?.let {

                                    totalAmountOfTrackies = totalAmountOfTrackies + 1

                                    if (it) {

                                        ingestedAmountOfTrackies = ingestedAmountOfTrackies + 1
                                    }
                                }
                            }
                            .addOnFailureListener {

                                return@addOnFailureListener
                            }
                    }

                    return@withContext mapOf(totalAmountOfTrackies to ingestedAmountOfTrackies)
                }

                else {

                    return@withContext null
                }
            }
        }

        catch (e: Exception) {

            Log.d("FirebaseUserRepository error", "$e")
            return null
        }
    }


    override suspend fun markTrackieAsIngested(
        currentDayOfWeek: String,
        trackieModel: TrackieModel,
    ): Boolean {

        return try {

            firebase.runBatch { batch ->

                if (trackieModel.ingestionTime == null) {

                    val docRef =
                        usersWeeklyStatistics
                        .collection(currentDayOfWeek)
                        .document(trackieModel.name)

                    batch.update(
                        docRef,
                        "ingested",
                        true
                    )
                }

            }.await()

            true
        }

        catch (e: Exception) {

            Log.d("FirebaseUserRepository error", "$e")
            false
        }
    }


    override suspend fun deleteUsersData() {

        usersLicense.delete()
        usersTrackies.delete()
        namesOfTrackies.delete()
        usersWeeklyStatistics.delete()
        user.delete()
    }
}