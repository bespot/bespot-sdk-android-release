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
