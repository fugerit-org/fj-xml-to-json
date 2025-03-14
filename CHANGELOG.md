# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Changed

- fj-bom set to 1.7.2
- fj-lib set to 8.6.6

## [1.2.0] - 2024-02-27

### Added

- New SampleObfuscateFun function

### Changed

- fj-bom set to 1.6.1
- Default randomizer uses now java.util.Random instead of SecureRandom (but it is possible to customize it)

## [1.1.1] - 2024-02-25

### Changed

- Added support for Reader/Writer to ProcessJson utl

## [1.1.0] - 2024-02-25

### Added

- ProcessJson utility

## [1.0.0] - 2024-02-18

### Changed

- fj-bom set to 1.6.0
- fj-lib set to 8.4.8
- Workflows review
- Upgraded build_maven_package workflow to version 1.0.1, (accepts DISABLE_MAVEN_DEPENDENCY_SUBMISSION)

## [0.1.1] - 2023-10-01

### Added

- utility methods for subclasses of XmlToJsonConverter

### Fixed

- custom XmlToJsonConverter test

## [0.1.0] - 2023-10-01

### Added

- [maven build ans sonar scan workflow](.github/workflows/build_maven_package.yml)
- xml to json conversion (see [xml conversion](src/main/docs/xml_conversion.md))
- json to xml conversion (see [xml conversion](src/main/docs/xml_conversion.md))
- [yaml support](src/main/docs/yaml_support.md) documentation
