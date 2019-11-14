package dev.tasdelen.tagggr

import android.app.Activity
import android.app.Dialog
import android.content.Context
import com.facebook.CallbackManager
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import android.content.Intent
import android.content.RestrictionsManager
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.util.Base64
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.facebook.login.Login
import com.firebase.ui.auth.ErrorCodes
import com.google.firebase.auth.FirebaseUser
import dev.tasdelen.tagggr.user.User
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.Collections.emptyList


class LoginActivity : AppCompatActivity() {

    private var callbackManager: CallbackManager? = null
    private var userSession: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    private var doubleBackToExitPressedOnce = false


    companion object {

        private const val RC_SIGN_IN = 123
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        generate_key_hash()
        if (!login_check(userSession)) {
            createSignInIntent()
        }
    }

    private fun createSignInIntent() {
        // [START auth_fui_create_intent]
        // Choose authentication providers
        val providers = arrayListOf(
            AuthUI.IdpConfig.PhoneBuilder().build(),
            AuthUI.IdpConfig.FacebookBuilder().build()
        )


        // Create and launch sign-in intent
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setIsSmartLockEnabled(false)
                .setLogo(R.drawable.com_facebook_profile_picture_blank_portrait)
                .setAvailableProviders(providers)
                .build(),
            RC_SIGN_IN
        )
        // [END auth_fui_create_intent]
    }

    // [START auth_fui_result]
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                val userEmail: String? = response?.email
                val isNewUser: Boolean? = response?.isNewUser

                if (isNewUser!!) {
                    Log.i("Tag", "$ $userEmail ")
                    loadCompleteProfileActivity(userEmail!!)
                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.login_user_connected),
                        Toast.LENGTH_SHORT
                    )
                        .show()

                    loadMainActivity()
                }


            } else {
                Toast.makeText(this, getString(R.string.login_login_failed), Toast.LENGTH_SHORT)
                    .show()
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
                if (response == null) {
                    Toast.makeText(this, "Probleme survenu, essayez à nouveau", Toast.LENGTH_SHORT)
                        .show()
                    loadLoginActivity()
                    return
                }
                if (response.getError()?.getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(
                        this,
                        "Pas d'internet, vérifiez votre connexion",
                        Toast.LENGTH_SHORT
                    ).show()
                    //Show No Internet Notification
                    return
                }

                if (response.getError()?.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Toast.makeText(this, "Probleme inconu", Toast.LENGTH_SHORT).show()
                    //Shown Unknown Error Notification
                    return
                }
            }
        }
    }
    // [END auth_fui_result]

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, getString(R.string.tap_to_exit), Toast.LENGTH_SHORT).show()

        Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
    }

    private fun privacyAndTerms() {
        val providers = emptyList<AuthUI.IdpConfig>()
        // [START auth_fui_pp_tos]
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setTosAndPrivacyPolicyUrls(
                    "https://example.com/terms.html",
                    "https://example.com/privacy.html"
                )
                .build(),
            RC_SIGN_IN
        )
        // [END auth_fui_pp_tos]
    }


    fun login_check(user: FirebaseUser?): Boolean {
        if (user != null) {
            Toast.makeText(this, "User already logged in", Toast.LENGTH_SHORT).show()
            Log.i("Tag", "user already logged in")
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            finish()
            return true
        } else {
            return false
        }
    }

    fun loadLoginActivity() {
        val i = Intent(this, LoginActivity::class.java)
        startActivity(i)
        finish()
    }

    fun loadMainActivity() {
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
        finish()
    }

    fun loadCompleteProfileActivity(userEmail: String) {
        val i = Intent(this, CompleteProfile::class.java)
        Log.i("Tag", "data email before intent $userEmail")
        i.putExtra("userEmail", userEmail)
        setResult(Activity.RESULT_OK, i)
        startActivity(i)
        finish()
    }

    fun generate_key_hash() {
        // Add code to print out the key hash
        try {
            val info = packageManager.getPackageInfo(
                "dev.tasdelen.tagggr",
                PackageManager.GET_SIGNATURES
            )
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.i("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {

        } catch (e: NoSuchAlgorithmException) {

        }

    }


}
