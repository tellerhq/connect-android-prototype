# Teller Connect Android Example

This repo is a temporary workaround while we build our Android SDK, essentially a prototype for the future SDK.

This method of using Teller Connect is not supported unless separately agreed with your Teller point of contact.

## Usage

To start Teller Connect:
```kotlin
val configuration = Configuration(
    appId = "YOUR-APPLICATION-ID-HERE",
    environment = Environment.SANDBOX
)

val bundle = bundleOf(ConnectFragment.ARG_CONFIG to configuration)
supportFragmentManager.commit {
    setReorderingAllowed(true)
    addToBackStack("TellerConnect")
    add<ConnectFragment>(R.id.fragmentContainer, args = bundle)
}
```

To receive results and events, the holding activity must implement `ConnectListener`.