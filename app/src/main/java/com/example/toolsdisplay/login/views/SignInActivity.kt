package com.example.toolsdisplay.login.views

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.toolsdisplay.R
import com.example.toolsdisplay.base.ScopeActivity
import com.example.toolsdisplay.home.views.HomeActivity
import com.example.toolsdisplay.service.ServiceDataSourceImpl
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

    private lateinit var loadingDialog: Dialog
    private lateinit var errorDialog: Dialog

    private lateinit var errorTitle: TextView
    private lateinit var errorMessage: TextView
    private lateinit var dialogOkButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        loginViewModel = ViewModelProvider(this, loginViewModelFactory).get(LoginViewModel::class.java)
        initWatchData()
        setUpView()
        checkAccessToken()

    }

    private fun setUpView() {
        loadingDialog = Dialog(this, R.style.ProgressDialogTheme)
        errorDialog = Dialog(this, R.style.ProgressDialogTheme)

        errorDialog.setContentView(R.layout.error_layout)

        this.errorTitle = errorDialog.findViewById(R.id.error_title)
        this.errorMessage = errorDialog.findViewById(R.id.error_message)
        this.dialogOkButton = errorDialog.findViewById(R.id.ok_button)

        findViewById<Button>(R.id.button_sign_in).setOnClickListener {
            showLoadingDialog()
            loggedIn()
        }

        dialogOkButton.setOnClickListener {
            hideErrorDialog()
        }
    }

    private fun checkAccessToken() = launch {
        if(!loginViewModel.getAccessToken().isNullOrEmpty()) {
            startActivity(Intent(applicationContext, SplashScreenActivity::class.java))
            finish()
        }
    }

    private fun initWatchData() {
        loginViewModel.accessToken.observe(this, Observer { newAuth ->
            if(newAuth == null) return@Observer

            if(!newAuth.isNullOrEmpty()) {
                hideErrorDialog()
                hideLoadingDialog()
                startActivity(Intent(applicationContext, SplashScreenActivity::class.java))
                finish()
            }
        })

        loginViewModel.errorEvent.observe(this, Observer { errorEvent ->
            hideLoadingDialog()
            showErrorDialog(errorEvent)
        })

    }

    private fun loggedIn() {

         var username = findViewById<TextView>(R.id.sign_in_email).text.toString()
         var password = findViewById<TextView>(R.id.sign_in_password).text.toString()
         loginViewModel.attemptLogin(username, password)

    }

    private fun showLoadingDialog() {
        loadingDialog.setContentView(R.layout.loading_indicator)
        loadingDialog.setCancelable(false)
        loadingDialog.setCanceledOnTouchOutside(false)
        loadingDialog.show()
    }

    private fun hideLoadingDialog() {
        loadingDialog.dismiss()
    }

    override fun onPause() {
        super.onPause()
        hideLoadingDialog()
        hideErrorDialog()
    }

    override fun onDestroy() {
        super.onDestroy()
        hideLoadingDialog()
        hideErrorDialog()
    }

    private fun showErrorDialog(errorEventParam: ServiceDataSourceImpl.ErrorResponseEvent){
        if(errorEventParam == ServiceDataSourceImpl.ErrorResponseEvent.CONNECTION_EROR) {
            this.errorTitle.text = getString(R.string.error_connection_title)
            this.errorMessage.text = getString(R.string.error_connection_message)
        } else {
            this.errorTitle.text = getString(R.string.error_generic_title)
            this.errorMessage.text = getString(R.string.error_generic_message)
        }

        errorDialog.show()

    }

    private fun hideErrorDialog(){
        errorDialog.dismiss()
    }


}