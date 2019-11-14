package dev.tasdelen.tagggr

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import dev.tasdelen.tagggr.user.User


class CompleteProfile : AppCompatActivity() {

    private var userSession: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    //Firebase references
    private var mAuth: FirebaseAuth? = null
    private var progressBar: ProgressBar? = null

    //Realtime database
//    private val firebaseDatabase = FirebaseDatabase.getInstance()
//    private val databaseReference = firebaseDatabase.reference.child("users")


    val db = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complete_profile)

        init()
    }

    private fun init() {
        val userEmail: String? = intent?.getStringExtra("userEmail").toString()
        val userEmailEditText = findViewById<EditText>(R.id.edit_user_email)

        userEmailEditText.setText(userEmail)
        userEmailEditText.setEnabled(false)

        mAuth = FirebaseAuth.getInstance()

        Log.i("Tag", "data : $userEmail")

        //TODO Completer le profil et enregistrer les infos en db  + forcer le fait d'avoir un +
        // compte completé pour utiliser l'application
        val buttonSumbit = findViewById<Button>(R.id.complete_profile_submit)
        buttonSumbit.setOnClickListener() {
            init_register_user(userEmail!!)

        }
    }

    fun init_register_user(userEmail: String) {
        val userNickname: String = findViewById<EditText>(R.id.edit_user_nickname).text.toString()

        if (!TextUtils.isEmpty(userNickname)) {
            completeRegistrationFirebase(userNickname, userEmail)
        } else {
            Toast.makeText(this, "Please introduce nickname", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBackPressed() {
        Toast.makeText(this, "Please complete your profile", Toast.LENGTH_SHORT).show()
    }

    private fun completeRegistrationFirebase(
        userNickname: String,
        userEmail: String
    ): Boolean {
        val userDisplayName = userSession?.displayName
        val user = User(userDisplayName!!, userEmail, userNickname)
        var status: Boolean = false

        db.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference ->
                status = true
                Log.d("Tag", "DocumentSnapshot added with ID: ${documentReference.id}")
                Toast.makeText(this, "Success", Toast.LENGTH_LONG).show()
                val i = Intent(this, MainActivity::class.java)
                startActivity(i)
                finish()
            }
            .addOnFailureListener { e ->
                Log.w("Tag", "Error adding document", e)
                status = false
            }


        //RealTime Database !
//        databaseReference.push().setValue(user)
//            .addOnSuccessListener {
//                Toast.makeText(this, "Success", Toast.LENGTH_LONG).show()
//                val i = Intent(this, MainActivity::class.java)
//                startActivity(i)
//                finish()
//
//            }
//            .addOnFailureListener { ex: Exception ->
//                Log.d("Tag", ex.toString())
//
//            }

        return status
    }
}