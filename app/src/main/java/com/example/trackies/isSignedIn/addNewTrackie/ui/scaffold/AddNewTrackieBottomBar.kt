package com.example.trackies.isSignedIn.addNewTrackie.ui.scaffold

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.trackies.isSignedIn.addNewTrackie.vm.AddNewTrackieViewModel
import com.example.trackies.ui.sharedUI.customButtons.mediumButtonChangingColor
import com.example.trackies.ui.sharedUI.customButtons.mediumTextButton
import com.example.trackies.ui.theme.Dimensions
import com.example.trackies.ui.theme.SecondaryColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun addNewTrackieBottomBar(
    addNewTrackieViewModel: AddNewTrackieViewModel,
    onClearAll: () -> Unit,
    onAdd: () -> Unit
) {

    var buttonAddNewTrackieIsEnabled by remember {

        mutableStateOf(false)
    }

    CoroutineScope(Dispatchers.IO).launch {

        addNewTrackieViewModel.buttonAddNewTrackieIsEnabled.collect {
            buttonAddNewTrackieIsEnabled = it
        }
    }

    Row(

        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .background(
                color = SecondaryColor,
                shape = RoundedCornerShape(size = Dimensions.roundedCornersOfBigElements)
            ),

        content = {

            Row(

                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxSize(),

                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,

                content = {

                    mediumTextButton(
                        text = "clear all",
                        onClick = {
                            onClearAll()
                        }
                    )

                    mediumButtonChangingColor(
                        textToDisplay = "add",
                        isEnabled = buttonAddNewTrackieIsEnabled,
                        onClick = {
                            onAdd()
                        }
                    )
                }
            )
        }
    )
}