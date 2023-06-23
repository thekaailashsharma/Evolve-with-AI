package com.test.palmapi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.test.palmapi.ui.theme.PalmApiTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PalmApiTheme {
                // A surface container using the 'background' color from the theme
                val viewModel: MainViewModel = hiltViewModel()
                LaunchedEffect(key1 = Unit){
                    viewModel.getApiData()
                }
                LazyColumn(modifier = Modifier.fillMaxSize()){
                    items(viewModel.apiData.value?.candidates?.size ?: 0) { index ->
                        Text(text = viewModel.apiData.value?.candidates?.get(index)?.output ?: "No Data",
                            color = Color.Black,
                            fontSize = 25.sp)
                    }
                }
            }
        }
    }
}


