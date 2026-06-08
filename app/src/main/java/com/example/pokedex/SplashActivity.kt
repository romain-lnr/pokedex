package com.example.pokedex

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val title = findViewById<TextView>(R.id.textSplashTitle)

        // État de départ : invisible et réduit
        title.alpha = 0f
        title.scaleX = 0.5f
        title.scaleY = 0.5f

        // Animation : apparition en fondu + agrandissement
        title.animate()
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(900)
            .start()

        // Après l'animation, on ouvre la liste
        lifecycleScope.launch {
            delay(1800)
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()  // retire le splash : le bouton retour ne doit pas y revenir
        }
    }
}