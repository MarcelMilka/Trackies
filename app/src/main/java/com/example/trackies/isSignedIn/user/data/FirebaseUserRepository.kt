package com.example.trackies.isSignedIn.user.data

import android.util.Log
import com.example.trackies.isSignedIn.constantValues.CurrentTime
import com.example.trackies.isSignedIn.constantValues.DaysOfWeek
import com.example.trackies.isSignedIn.trackie.TrackieViewState
import com.example.trackies.isSignedIn.trackie.TrackieViewStateEntity
import com.example.trackies.isSignedIn.trackie.convertEntityToTrackieViewState
import com.example.trackies.isSignedIn.user.buisness.licenseViewState.LicenseViewState
import com.example.trackies.isSignedIn.user.buisness.licenseViewState.LicenseViewStateEntity
import com.example.trackies.isSignedIn.user.buisness.licenseViewState.convertEntityToLicenseViewState
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
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
    override suspend fun fetchTodayTrackies(): List<TrackieViewState>? {

        val namesOfTrackiesForToday: List<String>? =
            fetchNamesOfTrackies(dayOfWeek = CurrentTime.getCurrentDayOfWeek())

        val fetchedTrackies: MutableList<TrackieViewState> = mutableListOf()

        return suspendCoroutine { continuation ->

            if (namesOfTrackiesForToday != null) {

                if (namesOfTrackiesForToday.isNotEmpty()) {

                    val tasks = namesOfTrackiesForToday.map { nameOfTheTrackie ->

                        usersTrackies.collection(nameOfTheTrackie).document(nameOfTheTrackie)
                            .get()
                            .addOnSuccessListener { document ->

                                val trackieViewStateEntity = document.toObject(TrackieViewStateEntity::class.java)

                                if (trackieViewStateEntity != null) {

                                    try {
                                        val trackieViewState = trackieViewStateEntity.convertEntityToTrackieViewState()
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
}