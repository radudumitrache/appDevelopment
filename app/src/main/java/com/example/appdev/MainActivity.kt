package com.example.appdev

import android.graphics.Typeface
import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.res.ResourcesCompat
import com.example.appdev.ui.theme.AppDevTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        val titleBanner = findViewById<TextView>(R.id.AppTitle);
        val subtextBanner = findViewById<TextView>(R.id.SubtitleBanner)
        val typeface : Typeface? = ResourcesCompat.getFont(this,R.font.InterVariable);
        titleBanner.setTypeface(typeface);
        subtextBanner.setTypeface(typeface);

    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AppDevTheme {
        Greeting("Android")
    }
}