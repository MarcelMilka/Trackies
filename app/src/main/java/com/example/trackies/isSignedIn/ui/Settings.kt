package com.example.trackies.isSignedIn.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BugReport
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.Logout
import androidx.compose.material.icons.rounded.Password
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.trackies.ui.sharedUI.customButtons.IconButtonToNavigateBetweenActivities
import com.example.trackies.ui.sharedUI.customSpacers.verticalSpacerL
import com.example.trackies.ui.sharedUI.customText.textHeadlineMedium
import com.example.trackies.ui.sharedUI.customText.textTitleMedium
import com.example.trackies.ui.sharedUI.customText.textTitleSmall
import com.example.trackies.ui.theme.BackgroundColor

@Composable
fun settings(
    usersEmail: String,
    onReturnHomeScreen: () -> Unit,
    onChangeEmail: () -> Unit,
    onChangePassword: () -> Unit,
    onDeleteAccount: () -> Unit,
    onChangeLanguage: () -> Unit,
    onReportInAppBug: () -> Unit,
    onDisplayInfoAboutThisApp: () -> Unit,
    onLogout: () -> Unit,
) {

    Box(

        modifier = Modifier
            .fillMaxSize()
            .background(color = BackgroundColor),

        content = {

            Column(

                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
                    .background(color = BackgroundColor),

                verticalArrangement = Arrangement.SpaceBetween,

                content = {

                    Column(

                        modifier = Modifier
                            .fillMaxWidth(),

                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Top,

                        content = {

                            IconButtonToNavigateBetweenActivities(icon = Icons.Rounded.Home) { onReturnHomeScreen() }

                            verticalSpacerL()
                            textHeadlineMedium(content = "Settings")

                            verticalSpacerL()

                            textHeadlineMedium(content = "Account")

                            elementOfSettings(enabled = false, icon = Icons.Rounded.Email, header = "E-mail", detail = usersEmail) { onChangeEmail() }

                            elementOfSettings(enabled = true, icon = Icons.Rounded.Password, header = "Change password", detail = null) { onChangePassword() }

                            elementOfSettings(enabled = true, icon = Icons.Rounded.Delete, header = "Delete account", detail = null) { onDeleteAccount() }

                            verticalSpacerL()

                            textHeadlineMedium(content = "Application")

                            elementOfSettings(enabled = false, icon = Icons.Rounded.Language, header = "Language", detail = "English") { onChangeLanguage() }

                            elementOfSettings(enabled = true, icon = Icons.Rounded.BugReport, header = "Report in-app bug", detail = null) { onReportInAppBug() }

                            elementOfSettings(enabled = true, icon = Icons.Rounded.Info, header = "About this app", detail = null) { onDisplayInfoAboutThisApp() }

                            elementOfSettings(enabled = true, icon = Icons.Rounded.Logout, header = "Logout", detail = null) { onLogout() }
                        }
                    )
                }
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable fun elementOfSettings(enabled: Boolean, icon: ImageVector, header: String, detail: String?, onClick: () -> Unit) {

    Surface(

        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),

        color = BackgroundColor,

        enabled = enabled,

        onClick = { onClick() },

        content = {

            Row(

                Modifier
                    .fillMaxWidth()
                    .height(35.dp),

                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,

                content = {

                    Icon(

                        imageVector = icon,
                        contentDescription = null,
                        tint = White,
                        modifier = Modifier
                            .size(37.dp)
                    )

                    Spacer( modifier = Modifier.width(5.dp) )

                    Column(

                        modifier = Modifier
                            .fillMaxWidth()
                            .height(35.dp),

                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Center,

                        content = {

                            textTitleMedium(content = header)

                            if (detail != null) { textTitleSmall(content = detail) }
                        }
                    )
                }
            )
        }
    )
}