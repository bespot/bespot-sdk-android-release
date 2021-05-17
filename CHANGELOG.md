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
