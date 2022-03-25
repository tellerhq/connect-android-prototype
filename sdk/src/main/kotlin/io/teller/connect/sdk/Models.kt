package io.teller.connect.sdk

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class Configuration(
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
    /**
     *  Can be set to one of:
     *  `disabled` - automatically connect all the supported financial accounts associated with this user's account at the institution (default)
     *  `single` - the user will see a list of supported financial accounts and will need to select one to connect
     *  `multiple` - the user will see a list of supported financial accounts and will need to select one or more to connect
     */
    val selectAccount: String? = null,
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
     * Additional parameters.
     */
    val additionalParams: Map<String, String> = emptyMap()
) : Parcelable

@Keep
enum class Environment { SANDBOX, DEVELOPMENT, PRODUCTION }

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