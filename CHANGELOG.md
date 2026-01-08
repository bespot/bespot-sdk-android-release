# 0.5.9 (2026-01-08)
### Removed
- Removed global options from consumer proguard rules
# 0.5.8 (2025-09-17)
### Added
- Secure access and refresh token storage
# 0.5.7 (2024-01-16)
- Added `ServiceUnavailableError.OutsideBusinessHours`
- Updated dependencies
# 0.5.6 (2023-11-16)
### Added
- Added support for Android 14
# 0.5.5 (2023-06-27)
### Added
- Exposed setAltUserId function for setting an alternative user ID.
- Updated Bespot Artifactory repository Maven url.
# 0.5.4 (2023-06-01)
### Added
- Introduced telemetry data updates through Bespot SDK
# 0.5.3 (2023-01-10)
### Added
- Added an internal mechanism for network self-healing
- Support R8 Full Mode
# 0.5.2 (2022-08-17)
### Added
- Added new supported metadata fields
# 0.5.1 (2022-05-04)
### Added
- Updated internal library dependencies
- Added new supported metadata fields
### Fixed
- Fixed sending all the metadata fields in all the status calls not just in the session start call
# 0.5.0 (2022-02-23)
### Added
- Added Failure.DeviceNotSupported, NoActiveAccountFound, FailureNoLocationOrReadingsData errors.
- System observers for Location services, Bluetooth availability, User permissions and network providers.
### Fixed
- When bluetooth or location is turned off, SDK promotes an Failure and waits to be available again.
- Fixed the wrong AWAY status promotion when location providers were disabled.
- When initialisation process is pending, all the other functions are waiting the process to be completed and then they start as expected without promoting any error.
### Changed
- From now no errors are fatal for the subscription. SDK keeps the subscription alive by waiting and observing device environmental problems to be resolved.
### Removed
- Removed NoConfigurationFound, CloseDistance, NoStoreForID errors.
- Removed Status.UNVERIFIED enum value.
# 0.4.2 (2021-12-17)
### Added
- Added StoreFailure.NoStoreFound and StoreFailure.StoreUnderMaintenance errors.
### Changed
- Less Failure.NotInitialized errors promoted by waiting when the initialization is in progress.
- Default bluetooth scanning interval time to 2sec.
# 0.4.1 (2021-11-12)
### Added
- Support Android-12
### Fixed
- R8 optimisation issue in the AppAuthenticationUseCase
- Socket timeouts are now mapped as Network Connection failure and sanitised network timeout values
# 0.4.0 (2021-10-19)
### Added
- Added the AWAY status for the beacon timeout mechanism
### Fixed
- Potential issue with re-authenticating an expired token
# 0.3.9 (2021-07-22)
### Fixed
- Potential dexing issue with joda-time
- Removed jcenter dependencies
- An issue that would cause problems when attempting to scan for beacons before the bluetooth adapter is completely enabled
# 0.3.8 (2021-06-24)
### Added
- Check for init status in every public method.
### Fixed
- Unsubscribe bug fixed when timeout status is promoted.
- Delayed results after unsubscribe fixed.
- LastStatus state fixed when enable/disable GPS.
- Minor internal fixes.
### Changed
- Remote logs optimisation.
# 0.3.7 (2021-06-04)
### Added
- The API URL can now be configured in the Bespot.init call, by passing the URL in the "base_api_url" configuration parameter. If no such parameter is passed, sane defaults are used.   
# 0.3.6 (2021-06-02)
### Added
- Configurations in Bespot.scanForBeacons().
# 0.3.5 (2021-05-28)
### Fixed
- Clearing the cached lastStatus value when subscription is terminated.
### Changed
- When no readings received the Bespot.subscribe() returns a StatusResult with OUT status value.
- If an error has been occurred when the subscription is active then the SDK promotes the error status and closes the subscription.
- Nullable config in subscribe()
# 0.3.3 (2021-05-17)
### Added
- Extra data added in beacon readings payloads to allow remote signal adjustments for different device types.
- Javadocs and Sources are included with the artifacts.
- Extended device info in headers.
### Fixed
- Minor internal fixes.
# 0.3.2 (2021-04-06)
### Added
- New configurations in SDK initialization. 
- Beacon scanning method *access granted based on init config.
- Change between Verified (production), Experimental and Raw INOUT implementation *access granted based on init config.
- Remote logging supported.
- Internal installation ID integrated.
- Set user identifier.
- Full obfuscated build.
- User Agent header.
- Accept-Language header.
- X-DeviceId & X-UserId headers.
### Changed
- Removed onDiscover callbacks from koktakt.io.
### Fixed
- JWT refresh token fixed.
# 0.3.1 (2021-02-26)
### Added
- The store's code (storeCode) in the StatusResult model.
# 0.3.0 (2021-02-18)
### Added
- New error types for store detection.
### Changed
- Location is now optional in subscribe().
- Removed the UNKNOWN and added the UNVERIFIED status value.
### Fixed
- Verified Status.
# 0.2.3 (2021-02-05)
### Added
- Implementation of lastStatus().
- A list of eids in StatusResult model.
# 0.2.2 (2021-01-26)
### Added
- Get all available stores.
- Get details about a store.
### Changed
- More generic on error callback.
### Fixed
- Minor internal fixes.
