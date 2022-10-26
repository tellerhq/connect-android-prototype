package io.teller.connect.sdk

interface ConnectListener {

    /**
     * Called when the client library has finished initializing.
     * In most scenarios this will go unused but in the special case
     * where you wish to execute some code after the library has been executed,
     * this is the place to put it.
     */
    fun onInit()

    /**
     * called when your user leaves the Teller Connect flow by closing it.
     * This callback only fires when a successful enrollment did not occur
     * and can be used to execute code which reacts to a user leaving the flow.
     */
    fun onExit()

    /**
     * Called when the user successfully completed the enrollment flow.
     */
    fun onSuccess(registration: Registration)

    /**
     * Called when the user successfully completed the payment flow.
     */
    fun onSuccess(payment: Payment)

    /**
     * Called when the user successfully completed the payee flow.
     */
    fun onSuccess(payee: Payee)

    /**
     * Called when payee or payment creation fails.
     */
    fun onFailure(error: Error)
}