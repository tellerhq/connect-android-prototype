package io.teller.connect.sdk

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

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
    val connectToken: String? = null
) : Parcelable

enum class Environment { SANDBOX, DEVELOPMENT, PRODUCTION }

@Parcelize
data class Registration(val accessToken: String, val user: User, val enrollment: Enrollment) : Parcelable

@Parcelize
data class User(val id: String) : Parcelable

@Parcelize
data class Enrollment(val id: String, val institution: Institution) : Parcelable

@Parcelize
data class Payment(val id: String) : Parcelable

@Parcelize
data class Payee(val id: String) : Parcelable

@Parcelize
data class Balance(val ledger: String, val available: String) : Parcelable

@Parcelize
data class RoutingInfo(val ach: String, val wire: String? = null) : Parcelable

@Parcelize
data class Institution(val name: String) : Parcelable

@Parcelize
data class Error(val type: String, val code: String, val message: String) : Parcelable