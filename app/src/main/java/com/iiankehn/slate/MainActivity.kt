package com.iiankehn.slate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.iiankehn.slate.data.DocumentRepository
import com.iiankehn.slate.data.SlateDatabase
import com.iiankehn.slate.ui.SlateApp
import com.iiankehn.slate.ui.theme.SlateTheme

class MainActivity : ComponentActivity() {
    private val slateViewModel: SlateViewModel by viewModels {
        SlateViewModel.Factory(
            DocumentRepository(SlateDatabase.getInstance(applicationContext).slateDao()),
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SlateTheme {
                SlateApp(slateViewModel)
            }
        }
    }
}
