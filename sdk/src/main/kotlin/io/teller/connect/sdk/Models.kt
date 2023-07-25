package io.teller.connect.sdk

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class Config(
    /**
     * Your Teller application id, can be found in https://teller.io/settings/application.
     */
    val appId: String,
    /**
     * The environment to use for enrolling the user's accounts.
     */
    val environment: Environment? = null,
    /**
     *  The institution id.
     *  If set, Teller Connect will skip the institution picker
     *  and load the first step for the corresponding institution.
     */
    val institution: String? = null,
    val selectAccount: SelectAccount? = null,
    val products: List<Product>,
    /**
     * Set to true to disable going back to the picker screen from an institution screen.
     */
    val skipPicker: Boolean = false,
    /**
     *  The User ID of the Teller user
     *  you want to add more enrollments to.
     */
    val userId: String? = null,
    /**
     *  The ID of the Teller enrollment
     *  you want to update if it has become disconnected.
     */
    val enrollmentId: String? = null,
    /**
     * The Connect Token returned by one of Teller API's endpoints
     * and used to initialize Teller Connect to perform a particular task
     * (e.g. as completing a payment requiring multi-factor authentication).
     */
    val connectToken: String? = null,
    /**
     * An arbitrary string chosen by your application to allow for the cryptographic signing of
     * the enrollment object passed to the onSuccess callback.
     * This value must be randomly generated and unique to the current session.
     */
    val nonce: String? = null,
    /**
     * Additional parameters.
     */
    val additionalParams: Map<String, String> = emptyMap()
) : Parcelable

@Keep
enum class Environment(private val env: String) {
    SANDBOX("sandbox"),
    DEVELOPMENT("development"),
    PRODUCTION("production");

    override fun toString(): String {
        return env
    }
}

@Keep
enum class SelectAccount(private val option: String) {
    DISABLED("disabled"),
    SINGLE("single"),
    MULTIPLE("multiple");

    override fun toString(): String {
        return option
    }
}

enum class Product(private val product: String) {
    VERIFY("verify"),
    BALANCE("balance"),
    TRANSACTIONS("transactions"),
    IDENTITY("identity");

    override fun toString(): String {
        return product
    }
}

@Keep
@Parcelize
data class Registration(val accessToken: String, val user: User, val enrollment: Enrollment) : Parcelable

@Keep
@Parcelize
data class User(val id: String) : Parcelable

@Keep
@Parcelize
data class Enrollment(val id: String, val institution: Institution) : Parcelable

@Keep
@Parcelize
data class Payment(val id: String) : Parcelable

@Keep
@Parcelize
data class Payee(val id: String) : Parcelable

@Keep
@Parcelize
data class Balance(val ledger: String, val available: String) : Parcelable

@Keep
@Parcelize
data class RoutingInfo(val ach: String, val wire: String? = null) : Parcelable

@Keep
@Parcelize
data class Institution(val name: String) : Parcelable

@Keep
@Parcelize
data class Error(val type: String, val code: String, val message: String) : Parcelable