package com.go.sport.ui.signin

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.core.os.bundleOf
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.go.sport.R
import com.go.sport.base.BaseActivity
import com.go.sport.network.APIManager
import com.go.sport.sharedpref.MySharedPreference
import com.go.sport.ui.dashboard.DashboardActivity
import com.go.sport.ui.signup.SignUpActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.gson.JsonParser
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_sign_in_first.*
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeUnit

class SignInFirstActivity : BaseActivity() {

    companion object {
        private const val TAG = "GoogleActivity"
        private const val RC_SIGN_IN = 9001
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private var callbackManager: CallbackManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in_first)

//        fullScreen()
        init()
        initListeners()
    }

    private fun init() {
        callbackManager = CallbackManager.Factory.create()

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        auth = Firebase.auth

        revokeAccess()
    }

    @SuppressLint("CheckResult")
    private fun initListeners() {
        RxView.clicks(cont_sign_in).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            /*val activityOptionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                    this,
                    iv_logo,
                    "logo"
                )
            val intent = Intent(
                this,
                SignInSecondActivity::class.java
            )
            startActivity(intent, activityOptionsCompat.toBundle())*/
            startActivity(this, SignInSecondActivity::class.java, false, 1)
        }

        RxView.clicks(tv_create_account).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            startActivity(this, SignUpActivity::class.java, false, 1)
        }

        RxView.clicks(iv_google).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            revokeAccess()
            signIn()
        }

        RxView.clicks(iv_facebook).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            revokeAccess()
//            LoginManager.getInstance().logInWithReadPermissions(this, listOf("public_profile"))
            btn_facebook.performClick()
        }

        btn_facebook.setReadPermissions("email", "public_profile")
        btn_facebook.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                Log.d(TAG, "facebook:onSuccess:$loginResult")
                handleFacebookAccessToken(loginResult.accessToken)
            }

            override fun onCancel() {
                Log.d(TAG, "facebook:onCancel")
            }

            override fun onError(error: FacebookException) {
                Log.d(TAG, "facebook:onError", error)
            }
        })
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun revokeAccess() {
        // Firebase sign out
        auth.signOut()

        // Google revoke access
        googleSignInClient.revokeAccess().addOnCompleteListener(this) {

        }

        // facebook logout
        LoginManager.getInstance().logOut()
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        pBar(1)
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in my User's information
                    val user = auth.currentUser
                    user?.let { loadFirebaseUser(it, true) }
                } else {
                    // If sign in fails, display a message to the myBookingUser.
                    errorToast("Authentication Failed.")
                }

                pBar(0)
            }
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        pBar(1)
        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithCredential:isSuccessful")
                    // Sign in success, update UI with the signed-in myBookingUser's information
                    val user = auth.currentUser
                    user?.let { loadFirebaseUser(it, false) }
                } else {
                    // If sign in fails, display a message to the myBookingUser.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    errorToast("Authentication failed.")
                }

                pBar(0)
            }
    }

    private fun loadFirebaseUser(firebaseUser: FirebaseUser, isGoogle: Boolean) {
        var uid = ""
        var firstName = ""
        var lastName = ""
        var email = ""

        for (profile in firebaseUser.providerData) {
            // UID specific to the provider
            uid = profile.providerId

            if (!profile.displayName.isNullOrEmpty()) {
                firstName = profile.displayName!!.split(" ")[0]
                if (profile.displayName!!.split(" ").size > 1)
                    lastName = profile.displayName!!.split(" ")[1]
            }

            /*if (!profile.email?.isNullOrEmpty()!!)
                email = profile.email!!*/
            profile.email?.let {
                email = it
            }
        }

        if (email.isNotEmpty()) {
            if (!isGoogle)
                facebookLogin(email, uid)
            else
                googleLogin(email, uid)
        }
    }

    private fun facebookLogin(email: String, token: String) {
        pBar(1)
        APIManager.facebookSignUp(email, token) { result, error ->
            pBar(0)

            if (error != null) {
                openSignUpOnError(error, email, token, "facebook")
                return@facebookSignUp
            }

            if (result?.status == true) {
                MySharedPreference(this).saveUserObject(result.user)
                startActivityFinishAll(this, DashboardActivity::class.java, true, 1)
            }
        }
    }

    private fun googleLogin(email: String, token: String) {
        pBar(1)
        APIManager.googleSignUp(email, token) { result, error ->
            pBar(0)

            if (error != null) {
                openSignUpOnError(error, email, token, "google")
                return@googleSignUp
            }

            if (result?.status == true) {
                MySharedPreference(this).saveUserObject(result.user)
                startActivityFinishAll(this, DashboardActivity::class.java, true, 1)
            }
        }
    }

    private fun openSignUpOnError(
        error: Throwable,
        email: String,
        token: String,
        type: String
    ) {
        var message = ""

        when (error) {
            is HttpException -> {
                // Kotlin will smart cast at this point
                val errorJsonString = error.response()?.errorBody()?.string()
                try {
                    message = JsonParser().parse(errorJsonString).asJsonObject["message"].asString
                } catch (ex: Exception) {
                    Log.e("mOnError", "Exception: $ex")
                }
            }
            is IOException -> {
                //message = "No Internet Connection"
            }
            else -> {
                message = if (error.message?.contains("Unable to resolve host") == true)
                    "No Internet Connection"
                else
                    error.message.toString()
            }
        }

        if (message != "") {
            errorToast(message)
            if (message.contains("not")) {
                startActivity(
                    this,
                    SignUpActivity::class.java,
                    false,
                    1,
                    bundleOf(Pair("emailAddress", email), Pair("token", token), Pair("socialType", type))
                )
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager?.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if ((requestCode == RC_SIGN_IN) and (resultCode == Activity.RESULT_OK)) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
                error("Google sign in failed $e")
            }
        }
    }
}