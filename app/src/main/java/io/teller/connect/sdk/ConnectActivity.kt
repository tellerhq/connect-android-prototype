package io.teller.connect.sdk

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.webkit.ConsoleMessage
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.webkit.*
import com.beust.klaxon.Klaxon
import com.google.android.material.snackbar.Snackbar
import io.teller.connect.R
import timber.log.Timber

class ConnectActivity : Activity(), WebViewCompat.WebMessageListener {

    companion object {
        const val ARG_CONFIG = "EXTRA_TC_CONFIGURATION"
        const val RESULT_PAYEE = "RESULT_PAYEE"
        const val RESULT_PAYMENT = "RESULT_PAYMENT"
        const val RESULT_ERROR = "RESULT_ERROR"
        const val RESULT_REGISTRATION = "RESULT_REGISTRATION"
    }

    private lateinit var webView: WebView
    private val klaxon = Klaxon()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connect)

        val config = intent.getParcelableExtra<Configuration>(ARG_CONFIG)!!

        setUpWebView()
        startTellerConnect(config)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setUpWebView() {
        webView = findViewById(R.id.connectWebView)

        webView.webChromeClient = object : WebChromeClient() {
            override fun onConsoleMessage(message: ConsoleMessage): Boolean {
                Timber.w(
                    "${message.message()} -- From line " +
                            "${message.lineNumber()} of ${message.sourceId()}"
                )
                return true
            }
        }

        webView.settings.javaScriptEnabled = true

        if (WebViewFeature.isFeatureSupported(WebViewFeature.WEB_MESSAGE_LISTENER)) {
            WebViewCompat.addWebMessageListener(
                webView,
                "AndroidApp",
                setOf("https://teller.io"),
                this
            )
        } else {
            /*
            TODO Could this feature be missing in any device sold in the past 5 years?
            If yes, we should use JsInterface instead.
            */
            Snackbar.make(
                webView,
                "Missing WebViewFeature.WEB_MESSAGE_LISTENER",
                Snackbar.LENGTH_INDEFINITE
            ).show()
        }

        webView.webViewClient = object : WebViewClientCompat() {
            override fun onPageFinished(view: WebView?, url: String?) {
                webView.evaluateJavascript(
                    """
                    window.postMessage = function(message, origin, transfer) {
                        window.AndroidApp.postMessage(message);
                    }
                """.trimIndent(), null
                )
            }
        }
    }

    private fun startTellerConnect(config: Configuration) {
        val builder = Uri.Builder()
            .scheme("https")
            .authority("teller.io")
            .appendPath("connect")
            .appendPath(config.appId)

        with(config) {
            environment?.let { builder.appendQueryParameter("environment", it.toString().lowercase()) }
            institution?.let { builder.appendQueryParameter("institution", it) }
            selectAccount?.let { builder.appendQueryParameter("selectAccount", it) }
            userId?.let { builder.appendQueryParameter("userId", it) }
            enrollmentId?.let { builder.appendQueryParameter("enrollmentId", it) }
            connectToken?.let { builder.appendQueryParameter("connectToken", it) }
        }

        webView.loadUrl(builder.toString())
    }

    override fun onPostMessage(
        view: WebView,
        message: WebMessageCompat,
        sourceOrigin: Uri,
        isMainFrame: Boolean,
        replyProxy: JavaScriptReplyProxy
    ) {
        message.data?.let { data ->
            when (val m = klaxon.parse<Message>(data)) {
                is ReadyMessage -> onReady()
                is ExitMessage -> onExit()
                is SuccessMessage -> onSuccess(m)
                is FailureMessage -> onFailure(m)
                else -> Timber.d("UNKNOWN")
            }
        }
    }

    private fun onReady() {
        Timber.d("READY")
        // TODO expose event to caller
    }

    private fun onExit() {
        setResult(RESULT_CANCELED)
        finish()
    }

    private fun onSuccess(message: SuccessMessage) {
        val intent = Intent()

        with(message.data) {
            if (accessToken != null && user != null && enrollment != null) {
                val registration = Registration(accessToken, user, enrollment)
                intent.putExtra(RESULT_REGISTRATION, registration)
            }
            if (payee != null) intent.putExtra(RESULT_PAYEE, payee)
            if (payment != null) intent.putExtra(RESULT_PAYMENT, payment)
        }

        setResult(RESULT_OK, intent)
        finish()
    }

    private fun onFailure(message: FailureMessage) {
        val intent = Intent()
        val error = Error(type = message.type, code = message.code, message = message.message)
        intent.putExtra(RESULT_ERROR, error)

        setResult(RESULT_CANCELED, intent)
        finish()
    }
}