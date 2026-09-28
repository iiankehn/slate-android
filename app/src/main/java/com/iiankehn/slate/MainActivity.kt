package com.iiankehn.slate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.iiankehn.slate.ui.SlateApp
import com.iiankehn.slate.ui.theme.SlateTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SlateTheme {
                SlateApp()
            }
        }
    }
}
