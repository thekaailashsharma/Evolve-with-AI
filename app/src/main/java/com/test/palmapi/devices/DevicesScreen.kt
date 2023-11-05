package com.test.palmapi.devices

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CastConnected
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.test.palmapi.MainViewModel
import com.test.palmapi.bottombar.BottomBar
import com.test.palmapi.mlkit.barcode.ui.DeviceScanner
import com.test.palmapi.ui.theme.appGradient
import com.test.palmapi.ui.theme.blueTint
import io.appwrite.Client
import io.appwrite.services.Databases

@Composable
fun DevicesScreen(
    email: String,
    pfp: String,
    name: String,
    viewModel: MainViewModel,
    navHostController: NavHostController
) {

    val devices = viewModel.devices.collectAsState()
    val isValidQR = viewModel.validQR.collectAsState()
    val isScannerVisible = remember { mutableStateOf(false) }
    val isProgressBarVisible = viewModel.isRegistered.collectAsState()
    val isConnected = viewModel.isConnected.collectAsState()

    Log.i("isProgressBarVisible", isProgressBarVisible.value.toString())
    val data = remember { mutableStateOf("") }

    LaunchedEffect(key1 = data.value) {
        if (data.value != "") {
            viewModel.validateQr(data.value)
        }
    }

    LaunchedEffect(key1 = isValidQR.value) {
        if(isValidQR.value){
            viewModel.successfullyRegister(
                deleteCollectionID = data.value,
                userCollectionID = email.substringBefore("@"),
                email = email,
                pfp = pfp,
                name = name,
                data = data.value
            )
            isScannerVisible.value = false
        }
    }

    Scaffold(bottomBar = {
        BottomBar(navController = navHostController)
    })
    {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(appGradient)
                .padding(it),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isScannerVisible.value) {
                DeviceScanner(isScannerVisible = isScannerVisible, code = data)
            } else {
                AnimatedVisibility(visible = isConnected.value) {
                    IconButton(onClick = { isScannerVisible.value = true }) {
                        Icon(
                            imageVector = Icons.Filled.CastConnected,
                            contentDescription = null,
                            tint = blueTint,
                            modifier = Modifier.size(100.dp)
                        )
                    }
                }
                AnimatedVisibility(visible = !isConnected.value) {
                    LaunchedEffect(key1 = Unit) {
                        viewModel.getDevices(email)
                    }
                    Column {
                        if (devices.value?.attributes?.isEmpty() == true) {
                            NoDevices(isScannerVisible = isScannerVisible)
                            if (isProgressBarVisible.value) {
                                Spacer(modifier = Modifier.height(20.dp))
                                LinearProgressIndicator(
                                    color = blueTint,
                                    modifier = Modifier.fillMaxWidth(0.75f)
                                )
                            }
                        } else {
                            AllDevices()
                        }
                    }
                }
            }


        }
    }

}

@Composable
fun NoDevices(isScannerVisible: MutableState<Boolean>) {

    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(
            onClick = {
                isScannerVisible.value = true
            },
            modifier = Modifier.size(100.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Devices,
                contentDescription = null,
                tint = blueTint,
                modifier = Modifier.size(80.dp),
            )
        }
    }

}

@Composable
fun AllDevices() {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = null,
                tint = blueTint,
                modifier = Modifier.size(50.dp)
            )
        }
    }

}

