package com.tanicerdas.app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

class AuthActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    private val TAG = "AuthActivity"

    private val googleSignInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        Log.d(TAG, "onActivityResult called: code = ${result.resultCode}")
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            Log.d(TAG, "Google Sign-In success: ${account.email}")
            firebaseAuthWithGoogle(account.idToken!!)
        } catch (e: ApiException) {
            Log.e(TAG, "Google Sign-In failed", e)
            Toast.makeText(this, "Google Sign-In gagal: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        val emailEt: EditText = findViewById(R.id.et_email)
        val passwordEt: EditText = findViewById(R.id.et_password)
        val loginBtn: Button = findViewById(R.id.btn_login)
        val googleBtn: ImageButton = findViewById(R.id.btn_google_signin)
        val registerTv: TextView = findViewById(R.id.tv_register)
        val forgotTv: TextView = findViewById(R.id.tv_forgot_password)

        loginBtn.setOnClickListener {
            val email = emailEt.text.toString().trim()
            val password = passwordEt.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email dan password wajib diisi", Toast.LENGTH_SHORT).show()
            } else {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            goToMain()
                        } else {
                            Toast.makeText(
                                this,
                                "Login gagal: ${task.exception?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }

        googleBtn.setOnClickListener {
            Log.d(TAG, "Google Sign-In button clicked")
            val signInIntent = googleSignInClient.signInIntent
            googleSignInLauncher.launch(signInIntent)
        }

        registerTv.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        forgotTv.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null) {
            goToMain()
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        Log.d(TAG, "firebaseAuthWithGoogle: idToken=${idToken.take(20)}...")
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null) {
                        val uid = user.uid
                        val name = user.displayName ?: ""
                        val email = user.email ?: ""
                        val photoUrl = user.photoUrl?.toString() ?: ""

                        val db = FirebaseFirestore.getInstance()
                        val userDoc = db.collection("users").document(uid)

                        userDoc.get().addOnSuccessListener { document ->
                            if (!document.exists()) {
                                val newUser = hashMapOf(
                                    "uid" to uid,
                                    "name" to name,
                                    "email" to email,
                                    "photoUrl" to photoUrl,
                                    "storeName" to "",
                                    "perluDikirim" to 0,
                                    "pembatalan" to 0,
                                    "retur" to 0,
                                    "review" to 0
                                )
                                userDoc.set(newUser)
                                    .addOnSuccessListener {
                                        Log.d(TAG, "User Firestore data created")
                                        goToMain()
                                    }
                                    .addOnFailureListener { e ->
                                        Log.e(TAG, "Gagal menyimpan data user: ${e.message}")
                                        Toast.makeText(
                                            this,
                                            "Gagal menyimpan data pengguna",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                            } else {
                                goToMain()
                            }
                        }.addOnFailureListener {
                            Log.e(TAG, "Gagal membaca data user")
                            goToMain()
                        }
                    } else {
                        Toast.makeText(this, "User null setelah sign in", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e(TAG, "Firebase Auth failed", task.exception)
                    Toast.makeText(
                        this,
                        "Autentikasi Google gagal: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun goToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
