package dev.tasdelen.tagggr

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.widget.Toast


class CompleteProfile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complete_profile)

        val userEmail: String? = intent?.getStringExtra("userEmail").toString()

        val userEmailEditText = findViewById<EditText>(R.id.edit_user_email)

        userEmailEditText.setText(userEmail)
        userEmailEditText.setEnabled(false)

        Log.i("Tag", "data : $userEmail")

        //TODO Completer le profil et enregistrer les infos en db  + forcer le fait d'avoir un +
        // compte completé pour utiliser l'application
    }

    override fun onBackPressed() {
        Toast.makeText(this,"Please complete your profile", Toast.LENGTH_SHORT).show()
    }
}