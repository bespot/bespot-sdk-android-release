# Bespot Android SDK

[![VERSION](https://img.shields.io/badge/VERSION-0.0.2-green)](#)
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
        maven {
            url 'https://bespot.jfrog.io/artifactory/bespot-sdk-android/'
            credentials {
                username = '${your_username}'
                password = '${your_password}'
            }
        }
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
Bespot.subscribe(location, object : StatusObserver {
        override fun onStatusError(error: StatusError) {

            }

        override fun onStatusUpdate(status: StatusResult) {

                    }
        })
```

For the unsubscribe procedure use the `Bespot.unsubscribe`

#### Request for last status

When the last status of the device is needed, you can retreive it by calling the `Bespot.lastStatus`. This will return a `StatusResult` object.

```kotlin
Bespot.lastStatus { statusResult, statusError ->  }
```

## Support

// TODO

## License

(C) Copyright 2020-2021 Bespot P.C. All rights reserved. See `LICENSE` for more information.
