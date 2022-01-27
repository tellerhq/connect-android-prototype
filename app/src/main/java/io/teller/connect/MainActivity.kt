package io.teller.connect

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.google.android.material.snackbar.Snackbar
import io.teller.connect.sdk.*
import timber.log.Timber

class MainActivity : Activity() {

    companion object {
        const val RC_CONNECT_BANK_ACCOUNT = 42
        const val RC_CONNECT_PAYEE = 43
        const val RC_CONNECT_PAYMENT = 44

        val configuration = Configuration(
            appId = "YOUR-APP-ID-HERE",
            environment = Environment.SANDBOX
        )
    }

    private lateinit var connectButton: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        connectButton = findViewById(R.id.connectButton)
        connectButton.setOnClickListener {
            startTellerConnect()
        }
    }

    private fun startTellerConnect() {
        val intent = Intent(this, ConnectActivity::class.java)
        intent.putExtra(ConnectActivity.ARG_CONFIG, configuration)
        startActivityForResult(intent, RC_CONNECT_BANK_ACCOUNT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                RC_CONNECT_BANK_ACCOUNT -> {
                    val registrationData = data?.getParcelableExtra<Registration>(ConnectActivity.RESULT_REGISTRATION)
                    Timber.i("Registration data: $registrationData")
                    Snackbar.make(connectButton, "\uD83D\uDCB8 Success! \uD83D\uDCB8", Snackbar.LENGTH_LONG).show()
                }
            }
        } else {
            val error = data?.getParcelableExtra<Error>(ConnectActivity.RESULT_ERROR)
            if (error != null) {
                Snackbar.make(connectButton, "Error: ${error.message}", Snackbar.LENGTH_LONG).show()
            } else {
                Snackbar.make(connectButton, "Canceled by user", Snackbar.LENGTH_LONG).show()
            }
        }
    }
}