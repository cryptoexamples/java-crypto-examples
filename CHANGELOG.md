# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/)
and this project adheres to [Semantic Versioning](http://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [0.4.1] - 2018-08-02

### Fixed

- Fixed inclusion of source code links to old files

## [0.4.0] - 2018-08-02

### Changed

- Changed class names to conform with guidelines
- Added demonstrate-methods for all examples
- Corrected meta format for file encryption example

### Fixed

- Added missing UTF-8 encoding in assymetric encryption example
- Fixed file encryption not needing trim anymore
- Fixed Headings according to guidlines
- Fixed import statements not expclicit in file encryption example

## [0.3.0] - 2018-07-18

### Changed

- Changed PBKDF2 hash function to SHA-512
- Increased PBKDF2 salt size to 64 bytes (equal to SHA-512 size)
- Increased PBKDF2 iterations to 10000
- Refactored variable names
- Refactored main methods to call a demonstrate...-method

### Fixed

- Exceptions were logged including the stack trace, now only the localized message is logged

## [0.2.0] - 2018-05-13

### Changed

- Fixed version link in Changelog
- Refactored examples
- Updated class comments

### Removed

- Removed easy object oriented examples that used a separate class
- Removed mentions of the need for unlimited policy files (unlimited is default since JDK / Java SE 9)
- Removed commented out code

## [0.1.1] - 2018-04-28

### Changed

- fixed Changelog

## [0.1.0] - 2018-04-28

### Added

- added Changelog
- added Asymmetric RSA String Encryption

## [X.Y.Z] - XXXX-XX-XX (TEMPLATE for new versions)

### Added

- added something
- added something else

### Changed

- changed something
- changed something else

### Deprecated

- deprecated something
- deprecated something else

### Removed

- removed something
- removed something else

### Fixed

- fixed something
- fixed something else

### Security

- made some security relevant changes
- made other security relevant changes

[Unreleased]: https://github.com/cryptoexamples/java-crypto-examples/compare/v0.4.1...HEAD
[0.4.1]: https://github.com/cryptoexamples/java-crypto-examples/compare/v0.4.0...v0.4.1
[0.4.0]: https://github.com/cryptoexamples/java-crypto-examples/compare/v0.3.0...v0.4.0
[0.3.0]: https://github.com/cryptoexamples/java-crypto-examples/compare/v0.2.0...v0.3.0
[0.2.0]: https://github.com/cryptoexamples/java-crypto-examples/compare/v0.1.1...v0.2.0
[0.1.1]: https://github.com/cryptoexamples/java-crypto-examples/compare/v0.1.0...v0.1.1
[0.1.0]: https://github.com/cryptoexamples/java-crypto-examples/releases/tag/v0.1.0