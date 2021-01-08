# Bespot Android SDK

[![VERSION](https://img.shields.io/badge/VERSION-0.1.0-green)](#)
[![API](https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat)](#)

Bespot Android SDK offers indoor location tracking and analytics reporting to 3rd party apps using BLE technology and Machine Learning.

## Features

- [x] Indoor location (in/out)
- [ ] Indoor area detection
- [ ] Outdoor location
- [ ] Analytics

## Requirements

// TODO

## Installation

Add the Bespot Artifactory repository to your list of repositories:

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

And then add the dependency in your gradle file:

```gradle
dependencies {
    implementation "com.bespot:library:${latest_version}"
}
```

## Usage

// TODO

## Support

// TODO

## License

(C) Copyright 2020-2021 Bespot P.C. All rights reserved. See `LICENSE` for more information.
