package dev.tasdelen.tagggr

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.facebook.CallbackManager
import com.google.firebase.auth.FirebaseAuth
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.FacebookCallback
import java.util.Arrays.asList
import dev.tasdelen.tagggr.R
import com.facebook.login.widget.LoginButton
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import java.util.*
import com.facebook.login.LoginManager
import android.util.Log


class MainActivity : AppCompatActivity() {

    private var callbackManager: CallbackManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)




        // Login
        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"))
        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    Log.d("letsSee", "Facebook token: " + loginResult.accessToken.token)

                }

                override fun onCancel() {
                    Log.d("letsSee", "Facebook onCancel.")

                }

                override fun onError(error: FacebookException) {
                    Log.d("letsSee", "Facebook onError.")

                }
            })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        callbackManager?.onActivityResult(requestCode, resultCode, data)

        Log.d("letsSee", "malsehnnnnnn: " + data)
    }
}
