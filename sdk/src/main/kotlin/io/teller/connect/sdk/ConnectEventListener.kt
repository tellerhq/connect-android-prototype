package io.teller.connect.sdk

import androidx.annotation.Keep
import java.io.Serializable

@Keep
open class ConnectEventListener: (String, Map<String, Any>) -> Unit, Serializable {
    /**
     * Called when a Teller Connect activity event is received.
     */
    override fun invoke(name: String, data: Map<String, Any>) {
        // to be overridden in client code
    }
}