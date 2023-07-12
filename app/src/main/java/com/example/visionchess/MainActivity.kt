package com.example.visionchess

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.visionchess.ui.theme.ThemeHelper


class MainActivity : ComponentActivity() {

    private val handler = Handler(Looper.getMainLooper())
    private val themeHelper = ThemeHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //////////////////////////////////////////////////////////////////////////////////////////////
        // This is the code that makes the loading screen
        //////////////////////////////////////////////////////////////////////////////////////////////
        setContent {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.actuallogobig),
                    contentDescription = "Vision Chess Logo",
                    modifier = Modifier.offset(y = (-100).dp)
                )
                ProgressBar(percentage = 1f, number = 100)
                themeHelper.VisionChessTheme{}
            }

        }
        //////////////////////////////////////////////////////////////////////////////////////////////
        // This is the code that makes the Fade In and Fade Out animation
        //////////////////////////////////////////////////////////////////////////////////////////////
        handler.postDelayed({
            setContentView(R.layout.activity_main)
            val blackScreen = findViewById<ImageView>(R.id.myBlackScreen)
            val bigLogo = findViewById<ImageView>(R.id.bigLogo)
            val animationFadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
            val animationFadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out)
            blackScreen.startAnimation(animationFadeIn)
            blackScreen.startAnimation(animationFadeOut)
            bigLogo.startAnimation(animationFadeIn)
            bigLogo.startAnimation(animationFadeOut)

        }, 3000)
        handler.postDelayed({
            setContentView(R.layout.activity_main)
            val blackScreen = findViewById<ImageView>(R.id.myBlackScreen)
            blackScreen.visibility = ImageView.INVISIBLE
            val bigLogo = findViewById<ImageView>(R.id.bigLogo)
            bigLogo.visibility = ImageView.INVISIBLE
            switchToHomeScreen()
        }, 4500)

    }

    private fun switchToHomeScreen() {
        handler.postDelayed({
            startActivity(Intent(applicationContext,HomeScreen::class.java))
                    overridePendingTransition(0,0)
        }, 250)
    }


    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }


}

