# Teller Connect Android Example

This repo is a temporary workaround while we build our Android SDK, essentially a prototype for the future SDK.

This method of using Teller Connect is not supported unless separately agreed with your Teller point of contact.

## Usage - `ConnectActivity`
It provides a quick way of integrating Teller Connect into an Android app by starting `ConnectActivity` to receive a result later after the flow finishes:

To start Teller Connect:
```kotlin
val configuration = Configuration(
    appId = "YOUR-APPLICATION-ID-HERE",
    environment = Environment.SANDBOX
)

val intent = Intent(this, ConnectActivity::class.java)
val bundle = bundleOf(
    ConnectFragment.ARG_CONFIG to configuration,
    ConnectFragment.ARG_EVENT_LISTENER to connectEventListener
)
intent.putExtra(ConnectActivity.EXTRA_BUNDLE, bundle)
startActivityForResult(intent, RC_CONNECT_BANK_ACCOUNT)
```

To receive the result:
```kotlin
override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    if (resultCode == RESULT_OK) {
        when (requestCode) {
            RC_CONNECT_BANK_ACCOUNT -> {
                val registrationData = data?.getParcelableExtra<Registration>(ConnectActivity.RESULT_REGISTRATION)
                // use registration data
            }
        }
    } else {
        val error = data?.getParcelableExtra<Error>(ConnectActivity.RESULT_ERROR)
        if (error != null) {
            // Handle error
        } else {
            // Flow was canceled by user
        }
    }
}
```

## Usage - `ConnectFragment`

Alternatively, you may want to use `ConnectFragment` for a bit more control.
Take a look at `ConnectActivity` source code for details.