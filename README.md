# Bespot Android SDK

[![VERSION](https://img.shields.io/badge/VERSION-0.2.2-green)](#)
[![API](https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat)](#)

Bespot Android SDK offers indoor location tracking and analytics reporting to 3rd party apps using BLE technology and Machine Learning.

## Features

- [x] Indoor location (in/out)
- [ ] Indoor area detection
- [ ] Outdoor location
- [ ] Analytics

## Installation

Add the Bespot Artifactory repository to your root `build.gradle`.

```gradle
allprojects {
    repositories {
        maven {url 'https://bespot.jfrog.io/artifactory/bespot-sdk-android/'}
    }
}
```

And then add the dependency in your project's `build.gradle`:

```gradle
dependencies {
    implementation "com.bespot:library:${latest_version}"
}
```

## Usage

First initialise Bespot SDK with the provided **App Id** and **App Secret**
```kotlin
Bespot.init(this, "your_app_id", "your_secret")
```

The Bespot SDK requires three permissions. It needs permission for [Fine Location](https://developer.android.com/reference/android/Manifest.permission#ACCESS_FINE_LOCATION), for [Bluetooth](https://developer.android.com/reference/android/Manifest.permission#BLUETOOTH) and for [Bluetooth Admin](https://developer.android.com/reference/android/Manifest.permission#BLUETOOTH_ADMIN). 

#### Subscribe for indoor location events

In order to receive indoor location changes you need to subscribe to `Bespot.subscribe`. This will return a 'StatusResult' object with the status of the device.

```kotlin
Bespot.subscribe(location, object: StatusObserver {
    override fun onStatusUpdate(status: StatusResult) {
        // Handle new status
    }

    override fun onError(error: Failure) {
        // Handle error
    }
})
```

For the unsubscribe procedure use the `Bespot.unsubscribe`

#### Request for last status

When the last status of the device is needed, you can retreive it by calling the `Bespot.lastStatus`. This will return a `StatusResult` object.

```kotlin
Bespot.lastStatus { statusResult, statusError ->  ... }
```

#### Request for stores

To receive the available stores, call the `Bestpo.getStores`.

```kotlin
Bespot.getStores(object: StoresCallback {
                override fun onStoreReceived(stores: List<Store>) {
                     // Handle available stores
                }

                override fun onError(error: Failure) {
                    // Handle error
                }
            })
``` 

#### Request for store information

You can request for information about a store by using the `Bespot.getStore` method and passing the `store_id`.

```kotlin
Bespot.getStore("store_id", object: StoreCallback {
                override fun onStoreReceived(stores: Store) {
                     // Handle store details
                }

                override fun onError(error: Failure) {
                    // Handle error
                }
            })
``` 

## Support

If you find a bug please fill out an [issue report](https://gitlab.com/bespot/bespot-sdk-android-release/-/issues) or contact us at [dev@bespot.me](dev@bespot.me)

## License

(C) Copyright 2020-2021 Bespot P.C. All rights reserved. See `LICENSE` for more information.
