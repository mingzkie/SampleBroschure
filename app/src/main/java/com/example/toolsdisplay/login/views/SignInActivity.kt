package com.example.toolsdisplay.login.views

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.toolsdisplay.R
import com.example.toolsdisplay.base.ScopeActivity
import com.example.toolsdisplay.home.views.HomeActivity
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
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        loginViewModel = ViewModelProvider(this, loginViewModelFactory).get(LoginViewModel::class.java)
        initWatchData()
        progressBar = findViewById(R.id.login_progress)
        findViewById<TextView>(R.id.button_sign_in).setOnClickListener {
            progressBar.visibility = View.VISIBLE
            loggedIn()
        }

    }

    private fun initWatchData() = launch {
        loginViewModel.accessToken.observe(this@SignInActivity, Observer { newAuth ->
            if(newAuth == null) return@Observer

            progressBar.visibility = View.GONE
            if(!newAuth.isNullOrEmpty()) {
                startActivity(Intent(applicationContext, HomeActivity::class.java))
            }
        })
    }

    private fun loggedIn() {

         var username = findViewById<TextView>(R.id.sign_in_email).text.toString()
         var password = findViewById<TextView>(R.id.sign_in_password).text.toString()
         loginViewModel.attemptLogin(username, password)

    }


}