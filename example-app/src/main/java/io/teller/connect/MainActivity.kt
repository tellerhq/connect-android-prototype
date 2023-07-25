package io.teller.connect

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
        val config = Config(
            appId = "YOUR-APP-ID",
            skipPicker = true,
            environment = Environment.SANDBOX,
            selectAccount = SelectAccount.SINGLE,
            products = listOf(Product.VERIFY, Product.BALANCE, Product.TRANSACTIONS)
        )
    }

    private lateinit var fragmentContainer: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fragmentContainer = findViewById(R.id.fragmentContainer)
        findViewById<View>(R.id.connectFragmentButton).setOnClickListener {
            startTellerConnectFragment(config)
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
    ConnectFragment example code:
     */
    private fun startTellerConnectFragment(config: Config) {
        val bundle = bundleOf(ConnectFragment.ARG_CONFIG to config)
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

    override fun onEvent(name: String, data: Map<String, Any>) {
        Timber.d("$name: $data")
    }

    override fun onFailure(error: Error) {
        handleError(error)
        removeTellerConnectFragment()
    }

    private fun removeTellerConnectFragment() {
        supportFragmentManager.popBackStack()
    }

    @Deprecated("Deprecated by Android")
    override fun onBackPressed() {
        if (supportFragmentManager.fragments.isNotEmpty()) {
            removeTellerConnectFragment()
        } else {
            super.onBackPressed()
        }
    }
}
