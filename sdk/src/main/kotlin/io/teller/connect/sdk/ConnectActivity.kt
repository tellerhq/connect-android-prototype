package io.teller.connect.sdk

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit

class ConnectActivity : FragmentActivity(), ConnectListener {

    companion object {
        const val EXTRA_BUNDLE = "EXTRA_BUNDLE"
        const val RESULT_PAYEE = "RESULT_PAYEE"
        const val RESULT_PAYMENT = "RESULT_PAYMENT"
        const val RESULT_ERROR = "RESULT_ERROR"
        const val RESULT_REGISTRATION = "RESULT_REGISTRATION"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tc_activity_connect)

        if (savedInstanceState == null) {
            val bundle = intent.getBundleExtra(EXTRA_BUNDLE)!!
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<ConnectFragment>(R.id.connectFragmentContainer, args = bundle)
            }
        }
    }

    override fun onInit() {}

    override fun onExit() {
        setResult(RESULT_CANCELED)
        supportFinishAfterTransition()
    }

    override fun onSuccess(registration: Registration) {
        val intent = Intent()
        intent.putExtra(RESULT_REGISTRATION, registration)

        setResult(RESULT_OK, intent)
        supportFinishAfterTransition()
    }

    override fun onSuccess(payment: Payment) {
        val intent = Intent()
        intent.putExtra(RESULT_PAYMENT, payment)

        setResult(RESULT_OK, intent)
        supportFinishAfterTransition()
    }

    override fun onSuccess(payee: Payee) {
        val intent = Intent()
        intent.putExtra(RESULT_PAYEE, payee)

        setResult(RESULT_OK, intent)
        supportFinishAfterTransition()
    }

    override fun onFailure(error: Error) {
        val intent = Intent()
        intent.putExtra(RESULT_ERROR, error)

        setResult(RESULT_CANCELED, intent)
        supportFinishAfterTransition()
    }
}