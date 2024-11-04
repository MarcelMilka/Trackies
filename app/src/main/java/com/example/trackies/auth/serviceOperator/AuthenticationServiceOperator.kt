package com.example.trackies.auth.serviceOperator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trackies.auth.buisness.AuthenticationServices
import com.example.trackies.auth.di.AuthenticationModule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

object AuthenticationServiceOperator: ViewModel() {

    private var _service = MutableStateFlow(AuthenticationServices.FirebaseAuthenticationService)
    val service = _service.asStateFlow()

    fun setFirebaseAuthenticationService() {

        viewModelScope.launch {

            if (_service.value != AuthenticationServices.FirebaseAuthenticationService) {


                _service.emit(
                    AuthenticationServices.FirebaseAuthenticationService
                )

                AuthenticationModule().provideAuthenticationService()
            }
        }
    }

    fun setRoomAuthenticationService() {

        viewModelScope.launch {

            if (_service.value != AuthenticationServices.RoomAuthenticationService) {

                _service.emit(
                    AuthenticationServices.RoomAuthenticationService
                )

                AuthenticationModule().provideAuthenticationService()
            }
        }
    }
}