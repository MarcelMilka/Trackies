package com.example.trackies.isSignedIn.user.data

import android.util.Log
import com.example.trackies.isSignedIn.constantValues.CurrentTime
import com.example.trackies.isSignedIn.constantValues.DaysOfWeek
import com.example.trackies.isSignedIn.xTrackie.buisness.TrackieModel
import com.example.trackies.isSignedIn.xTrackie.buisness.TrackieModelEntity
import com.example.trackies.isSignedIn.xTrackie.buisness.convertEntityToTrackieModel
import com.example.trackies.isSignedIn.user.buisness.licenseViewState.LicenseViewState
import com.example.trackies.isSignedIn.user.buisness.licenseViewState.LicenseViewStateEntity
import com.example.trackies.isSignedIn.user.buisness.licenseViewState.convertEntityToLicenseViewState
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
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

//  This method is responsible for checking if the user's unique identifier exists in the database.
//  If the unique identifier does not exist - a new document named after the user's unique identifier will be created.
    override fun firstTimeInTheApp(
        anErrorOccurred: () -> Unit
    ) {

        users.document(uniqueIdentifier)
            .get()
            .addOnSuccessListener { user ->

                if (!(user.exists())) {
                    addNewUser()
                }
            }
            .addOnFailureListener {
                anErrorOccurred()
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
        usersLicense.set(LicenseViewState(active = false, validUntil = null, totalAmountOfTrackies = 0))

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
    override suspend fun fetchUsersLicense(): LicenseViewState? {

        return suspendCoroutine { continuation ->

            usersLicense
                .get()
                .addOnSuccessListener { document ->

                    val licenseViewState = document.toObject(LicenseViewStateEntity::class.java)

                    if (licenseViewState != null) {

                        LicenseViewStateEntity(
                            active = licenseViewState.active,
                            validUntil = licenseViewState.validUntil,
                            totalAmountOfTrackies = licenseViewState.totalAmountOfTrackies
                        ).let { licenseViewStateEntity ->

                            if (licenseViewStateEntity.active != null && licenseViewStateEntity.totalAmountOfTrackies != null) {

                                val licenseViewState = licenseViewStateEntity.convertEntityToLicenseViewState()
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

//  This method is responsible for fetching all Trackies assigned to the current day of week.
    override suspend fun fetchTodayTrackies(): List<TrackieModel>? {

        val namesOfTrackiesForToday: List<String>? =
            fetchNamesOfTrackies(dayOfWeek = CurrentTime.getCurrentDayOfWeek())

        val fetchedTrackies: MutableList<TrackieModel> = mutableListOf()

        return suspendCoroutine { continuation ->

            if (namesOfTrackiesForToday != null) {

                if (namesOfTrackiesForToday.isNotEmpty()) {

                    val tasks = namesOfTrackiesForToday.map { nameOfTheTrackie ->

                        usersTrackies.collection(nameOfTheTrackie).document(nameOfTheTrackie)
                            .get()
                            .addOnSuccessListener { document ->

                                val trackieViewStateEntity = document.toObject(TrackieModelEntity::class.java)

                                if (trackieViewStateEntity != null) {

                                    try {
                                        val trackieViewState = trackieViewStateEntity.convertEntityToTrackieModel()
                                        fetchedTrackies.add(element = trackieViewState)
                                    }
                                    catch (e: Exception) {
                                        Log.d("Halla!", "FirebaseUserRepository, fetchTodayTrackies: $e")
                                        continuation.resume(null)
                                    }
                                }
                                else {
                                    Log.d(
                                        "Halla!",
                                        "FirebaseUserRepository, fetchTodayTrackies: trackieViewStateEntity is null"
                                    )
                                    continuation.resume(null)
                                }
                            }
                            .addOnFailureListener { exception ->
                                Log.d("Halla!", "fetchTrackiesForToday, an error occurred while fetching data, $exception")
                            }
                    }

                    Tasks.whenAllComplete(tasks)
                        .addOnCompleteListener {
                            continuation.resume(fetchedTrackies)
                        }
                }

                else {
                    Log.d(
                        "Halla!",
                        "FirebaseUserRepository, fetchTodayTrackies: there are no any names of trackies assigned for this day"
                    )
                    continuation.resume(listOf())
                }
            }

            else {
                continuation.resume(null)
            }
        }
    }

//  This method is responsible for adding new trackie to the user's database.
    override suspend fun addNewTrackie(
    trackieViewState: TrackieModel,
    onFailure: (String) -> Unit
    ) {

        val licenseViewState = fetchUsersLicense()

        if (licenseViewState != null) {

            licenseViewState.totalAmountOfTrackies

//          add new trackie to 'user's trackies' -> 'trackies'
            usersTrackies.collection(trackieViewState.name).document(trackieViewState.name).set(trackieViewState)

//          update total amount of trackies owned by the user 'user's information' -> 'license'
            val updatedTotalAmountOfTrackies = (licenseViewState.totalAmountOfTrackies + 1)
            usersLicense.update("totalAmountOfTrackies", updatedTotalAmountOfTrackies)

//          add name of the trackie to 'names of trackies' -> 'names of trackies' -> 'whole week'
            namesOfTrackies.update("whole week", FieldValue.arrayUnion(trackieViewState.name))

//          add name of the trackie to 'names of trackies' -> '(names of trackies)' -> *specific day of week*
            trackieViewState.repeatOn.forEach { dayOfWeek ->

                namesOfTrackies.update(dayOfWeek, FieldValue.arrayUnion(trackieViewState.name))
            }

//          add name of the trackies to 'user's statistics' -> 'user's weekly statistics' -> *specific day of week*
            setOf(
                DaysOfWeek.monday,
                DaysOfWeek.tuesday,
                DaysOfWeek.wednesday,
                DaysOfWeek.thursday,
                DaysOfWeek.friday,
                DaysOfWeek.saturday,
                DaysOfWeek.sunday
            ).forEach { dayOfWeek ->

                if (trackieViewState.ingestionTime == null) {

                    if (trackieViewState.repeatOn.contains(dayOfWeek)) {

                        val fieldToSave = mutableMapOf<String, Boolean>()

                        fieldToSave["ingested"] = false

                        usersWeeklyStatistics
                            .collection(dayOfWeek)
                            .document(trackieViewState.name)
                            .set(fieldToSave)
                    }
                }
            }
        }

        else {
            onFailure("License view state is null")
        }
    }

//  This method is responsible for fetching all the user's trackies assigned to the current day of week.
    override suspend fun fetchTrackiesForToday(
        onFailure: (String) -> Unit
    ): List<TrackieModel>? {

        val namesOfTrackiesForToday: List<String>? =
            fetchNamesOfTrackies(dayOfWeek = CurrentTime.getCurrentDayOfWeek())

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
                                        continuation.resume(value = null)
                                        onFailure(e.toString())
                                    }
                                }
                            }

                            .addOnFailureListener { exception ->
                                onFailure(exception.toString())
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

    override suspend fun fetchStatesOfTrackiesForToday(
        onFailure: (String) -> Unit
    ): Map<String, Boolean>? {

        val namesOfTrackiesForToday: List<String>? =
            fetchNamesOfTrackies(dayOfWeek = CurrentTime.getCurrentDayOfWeek())

        var namesAndStatesOfTrackies = mutableMapOf<String, Boolean>()

        return suspendCoroutine { continuation ->

            if (namesOfTrackiesForToday != null) {

                if (namesOfTrackiesForToday.isNotEmpty()) {

                    namesOfTrackiesForToday.onEach {nameOfTheTrackie ->

                        usersWeeklyStatistics
                            .collection(CurrentTime.getCurrentDayOfWeek())
                            .document(nameOfTheTrackie)
                            .get()
                            .addOnSuccessListener {document ->

                                document.getBoolean("ingested").let { stateOfTheTrackie ->

                                    if (stateOfTheTrackie != null) {

                                        namesAndStatesOfTrackies[nameOfTheTrackie] = stateOfTheTrackie

                                        if (namesAndStatesOfTrackies.size == namesOfTrackiesForToday.size) {

                                            continuation.resume(value = namesAndStatesOfTrackies)
                                        }
                                    }

                                    else {
                                        continuation.resume(value = null)
                                    }
                                }
                            }

                            .addOnFailureListener {

                                continuation.resume(value = null)
                            }
                    }
                }

                else {
                    continuation.resume(value = emptyMap())
                }
            }

            else {
                continuation.resume(value = null)
            }
        }
    }

    override suspend fun deleteTrackie(
        trackieViewState: TrackieModel,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {

        val licenseViewState = fetchUsersLicense()

        if (licenseViewState != null) {

//          This method is responsible for deleting:
//              - a document named after Trackie, which is contained by a document, also named after Trackie.
//          (via deleting the document mentioned above, the collection also gets deleted.)
//          route: 'user's trackies' -> 'trackies' -> '${TrackieViewState.name}' -> '${TrackieViewState.name}
            fun deleteTrackieFromUsersTrackies (
                onSuccess: () -> Unit,
                onFailure: (String) -> Unit
            ) {

                usersTrackies
                    .collection(trackieViewState.name)
                    .document(trackieViewState.name)
                    .delete()
                    .addOnSuccessListener {
                        onSuccess()
                    }
                    .addOnFailureListener {
                        onFailure("$it")
                    }
            }


//          This method is responsible for decreasing total amount of Trackies by one.
//          route: "user's information" -> 'license'
            fun decreaseTotalAmountOfTrackies(
                onSuccess: () -> Unit,
                onFailure: (String) -> Unit
            ) {

                val totalAmountOfTrackies = (licenseViewState.totalAmountOfTrackies - 1)
                usersLicense.update("totalAmountOfTrackies", totalAmountOfTrackies)
                    .addOnSuccessListener {
                        onSuccess()
                    }
                    .addOnFailureListener {
                        onFailure("$it")
                    }
            }


//          This method is responsible for deleting name of the Trackie from the array which contains names of all the user's Trackies.
//          route: 'names of trackies' -> 'names of trackies'
            fun deleteNameOfTrackieFromWholeWeek(
                onSuccess: () -> Unit,
                onFailure: (String) -> Unit
            ) {

                namesOfTrackies.update("whole week", FieldValue.arrayRemove(trackieViewState.name))
                    .addOnSuccessListener {
                        onSuccess()
                    }
                    .addOnFailureListener {
                        onFailure("$it")
                    }
            }


//          This method is responsible for deleting name of the Trackie from the array which contains names of the user's Trackies
//          assigned for a particular day of week
//          route: 'names of trackies' -> 'names of trackies'
            fun deleteNameOfTrackieFromDaysOfWeek(
                onSuccess: () -> Unit,
                onFailure: (String) -> Unit
            ) {

                trackieViewState.repeatOn.forEach { dayOfWeek ->

                    namesOfTrackies.update(dayOfWeek, FieldValue.arrayRemove(trackieViewState.name))
                        .addOnSuccessListener {
                            onSuccess()
                        }
                        .addOnFailureListener {
                            onFailure("$it")
                        }
                }
            }


//          This method is responsible for deleting
//          route: 'user's statistics' -> 'user's weekly statistics'
            fun deleteTrackieFromWeeklyStatistics(
                onSuccess: () -> Unit,
                onFailure: (String) -> Unit
            ) {

                setOf(
                    DaysOfWeek.monday,
                    DaysOfWeek.tuesday,
                    DaysOfWeek.wednesday,
                    DaysOfWeek.thursday,
                    DaysOfWeek.friday,
                    DaysOfWeek.saturday,
                    DaysOfWeek.sunday
                ).forEach { dayOfWeek ->

                    usersWeeklyStatistics
                        .collection(dayOfWeek)
                        .document(trackieViewState.name)
                        .delete()
                        .addOnSuccessListener {
                            onSuccess()
                        }
                        .addOnFailureListener {
                            onFailure("$it")
                        }
                }
            }

            deleteTrackieFromUsersTrackies(
                onSuccess = {},
                onFailure = {
                    onFailure(it)
                }
            )

            decreaseTotalAmountOfTrackies(
                onSuccess = {},
                onFailure = {
                    onFailure(it)
                }
            )

            deleteNameOfTrackieFromWholeWeek(
                onSuccess = {},
                onFailure = {
                    onFailure(it)
                }
            )

            deleteNameOfTrackieFromDaysOfWeek(
                onSuccess = {},
                onFailure = {
                    onFailure(it)
                }
            )

            deleteTrackieFromWeeklyStatistics(
                onSuccess = {},
                onFailure = {
                    onFailure(it)
                }
            )
        }

        else {
            onFailure("licenseViewState is null")
        }
    }

    override suspend fun fetchAllTrackies(
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ): List<TrackieModel>? {

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

                                        allTrackies.add(element = trackieViewState)
                                    }

                                    catch (e: Exception) {

                                        continuation.resume(value = null)
                                        onFailure("$e")
                                    }
                                }
                            }
                            .addOnFailureListener {

                                continuation.resume(value = null)
                                onFailure("$it")
                            }
                    }

                    Tasks.whenAllComplete(tasks).addOnCompleteListener {

                        continuation.resume(value = allTrackies)
                    }
                }

                else {

                    continuation.resume(value = listOf<TrackieModel>())
                }
            }

            else {

                continuation.resume(value = null)
            }
        }
    }

    override suspend fun fetchWeeklyRegularity(
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ): Map<String, Map<Int, Int>>? {

        return suspendCoroutine { continuation ->

            val mapWithCalculatedRegularity = mutableMapOf<String, Map<Int, Int>>()
            val currentDayOfWeek = CurrentTime.getCurrentDayOfWeek()
            var foundCurrentDayOfWeek = false

            val jobs =  setOf(
                DaysOfWeek.monday,
                DaysOfWeek.tuesday,
                DaysOfWeek.wednesday,
                DaysOfWeek.thursday,
                DaysOfWeek.friday,
                DaysOfWeek.saturday,
                DaysOfWeek.sunday,
            ).map { dayOfWeek ->

                CoroutineScope(Dispatchers.Default).async {

                    if (!foundCurrentDayOfWeek) {

                        val namesOfTrackiesForThisDayOfWeek = fetchNamesOfTrackies(dayOfWeek)

                        var totalAmount = 0
                        var ingestedAmount = 0

                        if (namesOfTrackiesForThisDayOfWeek != null) {

                            if (namesOfTrackiesForThisDayOfWeek.isNotEmpty()) {

                                for (nameOfTrackie in namesOfTrackiesForThisDayOfWeek) {
                                    val documentSnapshot = usersWeeklyStatistics
                                        .collection(dayOfWeek)
                                        .document(nameOfTrackie)
                                        .get()
                                        .await()

                                    documentSnapshot.getBoolean("ingested")?.let { ingested ->
                                        totalAmount += 1
                                        if (ingested) {
                                            ingestedAmount += 1
                                        }
                                    }
                                }

                                mapWithCalculatedRegularity[dayOfWeek] = mapOf(totalAmount to ingestedAmount)
                            }

                            else {

                                mapWithCalculatedRegularity[dayOfWeek] = mapOf(0 to 0)
                            }

                            if (dayOfWeek == currentDayOfWeek) {

                                foundCurrentDayOfWeek = true
                            }
                        }

                        else {

                            continuation.resume(value = null)
                        }
                    }

                    else {

                        mapWithCalculatedRegularity[dayOfWeek] = mapOf(0 to 0)
                    }
                }
            }

            CoroutineScope(Dispatchers.Default).launch {

                jobs.awaitAll()

                val mapToReturn = mapOf(
                    DaysOfWeek.monday to mapWithCalculatedRegularity[DaysOfWeek.monday]!!,
                    DaysOfWeek.tuesday to mapWithCalculatedRegularity[DaysOfWeek.tuesday]!!,
                    DaysOfWeek.wednesday to mapWithCalculatedRegularity[DaysOfWeek.wednesday]!!,
                    DaysOfWeek.thursday to mapWithCalculatedRegularity[DaysOfWeek.thursday]!!,
                    DaysOfWeek.friday to mapWithCalculatedRegularity[DaysOfWeek.friday]!!,
                    DaysOfWeek.saturday to mapWithCalculatedRegularity[DaysOfWeek.saturday]!!,
                    DaysOfWeek.sunday to mapWithCalculatedRegularity[DaysOfWeek.sunday]!!,
                )

                continuation.resume(value = mapToReturn)
            }
        }
    }

    override suspend fun markTrackieAsIngested(
        trackieViewState: TrackieModel,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {

        if (trackieViewState.ingestionTime == null) {

            trackieViewState.repeatOn.forEach { dayOfWeek ->

                usersWeeklyStatistics
                    .collection(CurrentTime.getCurrentDayOfWeek()) // current day of week
                    .document(trackieViewState.name)
                    .update("ingested", true)
                    .addOnSuccessListener {
                        onSuccess()
                    }
                    .addOnFailureListener {
                        onFailure("$it")
                    }
            }
        }
    }
}