package io.teller.connect

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.google.android.material.snackbar.Snackbar
import io.teller.connect.sdk.*
import timber.log.Timber

class MainActivity : FragmentActivity(), ConnectListener {

    companion object {
        const val RC_CONNECT_BANK_ACCOUNT = 42
        const val RC_CONNECT_PAYEE = 43
        const val RC_CONNECT_PAYMENT = 44

        val configuration = Configuration(
            appId = "YOUR-APP-ID",
            skipPicker = true,
            environment = Environment.SANDBOX,
            selectAccount = "single"
        )
    }

    private lateinit var fragmentContainer: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.connectActivityButton).setOnClickListener {
            startTellerConnectActivity(configuration)
        }

        fragmentContainer = findViewById(R.id.fragmentContainer)
        findViewById<View>(R.id.connectFragmentButton).setOnClickListener {
            startTellerConnectFragment(configuration)
        }
    }

    private fun showSuccess(registration: Registration?) {
        val message =
            "\uD83D\uDCB8 Success! \uD83D\uDCB8\naccessToken: ${registration?.accessToken}"
        Snackbar.make(fragmentContainer, message, Snackbar.LENGTH_LONG).show()
    }

    private fun handleError(error: Error?) {
        if (error != null) {
            Snackbar.make(fragmentContainer, "Error: ${error.message}", Snackbar.LENGTH_LONG)
                .show()
        } else {
            Snackbar.make(fragmentContainer, "Failure", Snackbar.LENGTH_LONG).show()
        }
    }

    /*
    ConnectActivity example code:
     */
    private fun startTellerConnectActivity(configuration: Configuration) {
        val intent = Intent(this, ConnectActivity::class.java)
        val bundle = bundleOf(
            ConnectFragment.ARG_CONFIG to configuration,
            ConnectFragment.ARG_EVENT_LISTENER to connectEventListener
        )
        intent.putExtra(ConnectActivity.EXTRA_BUNDLE, bundle)
        startActivityForResult(intent, RC_CONNECT_BANK_ACCOUNT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                RC_CONNECT_BANK_ACCOUNT -> {
                    val registrationData =
                        data?.getParcelableExtra<Registration>(ConnectActivity.RESULT_REGISTRATION)
                    showSuccess(registrationData!!)
                }
            }
        } else {
            val error = data?.getParcelableExtra<Error>(ConnectActivity.RESULT_ERROR)
            handleError(error)
        }
    }

    /*
    ConnectFragment example code:
     */
    private fun startTellerConnectFragment(configuration: Configuration) {
        val bundle = bundleOf(
            ConnectFragment.ARG_CONFIG to configuration,
            ConnectFragment.ARG_EVENT_LISTENER to connectEventListener
        )
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            addToBackStack("TellerConnect")
            add<ConnectFragment>(R.id.fragmentContainer, args = bundle)
        }
    }

    override fun onInit() {
        Timber.d("Initialized Teller Connect")
    }

    override fun onExit() {
        handleError(null)
        removeTellerConnectFragment()
    }

    override fun onSuccess(registration: Registration) {
        showSuccess(registration)
        removeTellerConnectFragment()
    }

    override fun onSuccess(payment: Payment) {
        // handle payment success
        removeTellerConnectFragment()
    }

    override fun onSuccess(payee: Payee) {
        // handle payee success
        removeTellerConnectFragment()
    }

    override fun onFailure(error: Error) {
        handleError(error)
        removeTellerConnectFragment()
    }

    private val connectEventListener = object: ConnectEventListener() {
        override fun invoke(name: String, data: Map<String, Any>) {
            Timber.d("$name: $data")
        }
    }

    private fun removeTellerConnectFragment() {
        supportFragmentManager.popBackStack()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.fragments.isNotEmpty()) {
            removeTellerConnectFragment()
        } else {
            super.onBackPressed()
        }
    }
}
