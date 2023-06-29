package com.test.palmapi.mlkit.barcode.ui

import android.util.Log
import android.view.ViewGroup
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Cameraswitch
import androidx.compose.material.icons.sharp.FlashOff
import androidx.compose.material.icons.sharp.FlashOn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.common.util.concurrent.ListenableFuture
import com.test.palmapi.mlkit.barcode.QrCodeAnalyzer
import com.test.palmapi.ui.theme.textColor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Composable
fun CodeScannerView(code: MutableState<String>) {
    val context = LocalContext.current
    val cameraProviderFuture: ListenableFuture<ProcessCameraProvider> =
        ProcessCameraProvider.getInstance(context)
    var camera by remember {
        mutableStateOf<Camera?>(null)
    }
    var lens by remember { mutableStateOf(CameraSelector.LENS_FACING_BACK) }
    var preview by remember { mutableStateOf<Preview?>(null) }
    val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
    val cameraSelector: CameraSelector = CameraSelector.Builder().requireLensFacing(lens).build()
    var isFlashEnabled by remember { mutableStateOf(false) }
    val lifecycleOwner = LocalLifecycleOwner.current
    val barCodeVal = remember { mutableStateOf("") }
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            AndroidView(
                factory = { androidViewContext ->
                    PreviewView(androidViewContext).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT,
                        )
                        implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                    }
                },
                modifier = Modifier.fillMaxSize(),
                update = { previewView ->
                    val cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()
                    cameraProviderFuture.addListener({
                        preview = Preview.Builder().build().also {
                            it.setSurfaceProvider(previewView.surfaceProvider)
                        }
                        val barcodeAnalyser = QrCodeAnalyzer { barcodes ->
                            barcodes.forEach { barcode ->
                                barcode.rawValue?.let { barcodeValue ->
                                    barCodeVal.value = barcodeValue
                                    code.value = barcodeValue
                                }
                            }
                        }
                        val imageAnalysis: ImageAnalysis = ImageAnalysis.Builder()
                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                            .build().also {
                                it.setAnalyzer(cameraExecutor, barcodeAnalyser)
                            }

                        try {
                            cameraProvider.unbindAll()
                            camera = cameraProvider.bindToLifecycle(
                                lifecycleOwner, cameraSelector, preview, imageAnalysis
                            )
                        } catch (e: Exception) {
                            Log.d("TAG", "CameraPreview: ${e.localizedMessage}")
                        }
                    }, ContextCompat.getMainExecutor(context))
                },

                )
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 25.dp, start = 15.dp, end = 15.dp),
            contentAlignment = Alignment.TopStart
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF353739).copy(0.55f)),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = {
                    if (camera?.cameraInfo?.hasFlashUnit() == true) {
                        camera?.cameraControl?.enableTorch(!isFlashEnabled)
                        isFlashEnabled = !isFlashEnabled
                    }
                }) {
                    if (isFlashEnabled) {
                        Icon(
                            imageVector = Icons.Sharp.FlashOn,
                            contentDescription = "",
                            tint = textColor
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Sharp.FlashOff,
                            contentDescription = "",
                            tint = textColor
                        )
                    }

                }
                IconButton(onClick = {
                    lens =
                        if (lens == CameraSelector.LENS_FACING_BACK) CameraSelector.LENS_FACING_FRONT
                        else CameraSelector.LENS_FACING_BACK
                }) {
                    Icon(
                        imageVector = Icons.Sharp.Cameraswitch,
                        contentDescription = "",
                        tint = textColor
                    )
                }

            }
        }
    }

}










