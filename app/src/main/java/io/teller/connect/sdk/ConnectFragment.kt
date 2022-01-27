package io.teller.connect.sdk

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import androidx.fragment.app.Fragment
import androidx.webkit.*
import com.beust.klaxon.Klaxon
import com.google.android.material.snackbar.Snackbar
import io.teller.connect.R
import timber.log.Timber

class ConnectFragment : Fragment(R.layout.fragment_connect), WebViewCompat.WebMessageListener {

    companion object {
        const val ARG_CONFIG = "ARG_CONFIGURATION"
    }

    private val klaxon = Klaxon()
    private lateinit var webView: WebView
    private var listener: ConnectListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ConnectListener) {
            listener = context
        } else {
            throw RuntimeException("Parent activity of ConnectFragment must implement ConnectListener!")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val config = requireArguments().getParcelable<Configuration>(ARG_CONFIG)!!
        setUpWebView(view)
        startTellerConnect(config)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setUpWebView(view: View) {
        webView = view.findViewById(R.id.connectWebView)
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
            The postMessage API is supported since Android WebView 37 so this should never happen.
            If it turns out it's a problem, the older JavascriptInterface API can be used.
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
                    }""".trimIndent(), null
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
            environment?.let {
                val env = it.toString().lowercase()
                builder.appendQueryParameter("environment", env)
            }
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
        listener?.onInit()
    }

    private fun onExit() {
        listener?.onExit()
    }

    private fun onSuccess(message: SuccessMessage) {
        with(message.data) {
            when {
                accessToken != null && user != null && enrollment != null -> {
                    val registration = Registration(accessToken, user, enrollment)
                    listener?.onSuccess(registration)
                }
                payee != null -> listener?.onSuccess(payee)
                payment != null -> listener?.onSuccess(payment)
                else -> null
            }
        }
    }

    private fun onFailure(message: FailureMessage) {
        val error = Error(type = message.type, code = message.code, message = message.message)
        listener?.onFailure(error)
    }
}