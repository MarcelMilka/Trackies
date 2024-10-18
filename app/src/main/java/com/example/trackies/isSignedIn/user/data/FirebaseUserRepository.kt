package com.example.trackies.isSignedIn.user.data

import android.util.Log
import com.example.trackies.isSignedIn.constantValues.DaysOfWeek
import com.example.trackies.isSignedIn.user.buisness.licenseViewState.LicenseViewState
import com.example.trackies.isSignedIn.user.buisness.licenseViewState.LicenseViewStateEntity
import com.example.trackies.isSignedIn.user.buisness.licenseViewState.convertEntityToLicenseViewState
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
    private val usersInformation = user.collection("user's information").document("license")
    private val usersTrackies = user.collection("user's trackies").document("trackies")
    private val namesOfTrackies = user.collection("names of trackies").document("names of trackies")
    private val usersWeeklyStatistics = user.collection("user's statistics").document("user's weekly statistics")

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

    private fun addNewUser() {

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
        usersInformation.set(LicenseViewState(active = false, validUntil = null, totalAmountOfTrackies = 0))

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

    override suspend fun fetchUsersLicenseInformation(): LicenseViewState? {

        return suspendCoroutine { continuation ->

            usersInformation
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
}