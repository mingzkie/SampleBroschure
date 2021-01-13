package com.example.toolsdisplay.login.views

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.toolsdisplay.R
import com.example.toolsdisplay.base.ScopeActivity
import com.example.toolsdisplay.home.views.HomeActivity
import com.example.toolsdisplay.splashscreen.SplashScreenActivity
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance


class SignInActivity : ScopeActivity(), KodeinAware {

    override val kodein by closestKodein()
    private val loginViewModelFactory by instance<LoginViewModelFactory>()


    companion object {
        val LOGOUT_REASON_KEY : String = "LOGUT_REASON_KEY"
    }

    private lateinit var loginViewModel: LoginViewModel

    private lateinit var popUpDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        loginViewModel = ViewModelProvider(this, loginViewModelFactory).get(LoginViewModel::class.java)
        initWatchData()
        setUpView()

    }

    private fun setUpView() {
        popUpDialog = Dialog(this, R.style.ProgressDialogTheme)
        findViewById<TextView>(R.id.button_sign_in).setOnClickListener {
            loggedIn()
        }
    }

    private fun initWatchData() = launch {
        loginViewModel.accessToken.observe(this@SignInActivity, Observer { newAuth ->
            if(newAuth == null) return@Observer

            if(!newAuth.isNullOrEmpty()) {
                popUpDialog.hide()
                startActivity(Intent(applicationContext, SplashScreenActivity::class.java))
                finish()
            }
        })
    }

    private fun loggedIn() {

         var username = findViewById<TextView>(R.id.sign_in_email).text.toString()
         var password = findViewById<TextView>(R.id.sign_in_password).text.toString()
         showLoadingDialog()
         loginViewModel.attemptLogin(username, password)

    }

    private fun showLoadingDialog() {
        popUpDialog.setContentView(R.layout.loading_indicator)
        popUpDialog.setCancelable(false)
        popUpDialog.setCanceledOnTouchOutside(false)
        popUpDialog.show()
    }


}