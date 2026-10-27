[![OpenDSE-TestGen Build & Test](https://github.com/SDARG/opendse-testgen/actions/workflows/gradle.yml/badge.svg)](https://github.com/SDARG/opendse-testgen/actions/workflows/gradle.yml)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/ef6dc436d8844aa7be33bf8ba51cf37c)](https://app.codacy.com/gh/SDARG/opendse-testgen/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade)
[![Codacy Badge](https://app.codacy.com/project/badge/Coverage/fd2e81e87d924e2baf0331dea12b7d17)](https://app.codacy.com/gh/SDARG/opendse-testgen/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_coverage)
[![](https://jitpack.io/v/sdarg/opendse-testgen.svg)](https://jitpack.io/#sdarg/opendse-testgen)

# OpenDSE-TestGen

_OpenDSE-TestGen_  is a testcase generator for the OpenDSE framework, written in Java.
It generates a specification graph consisting of an application graph, an architecture graph, and a set of mapping edges connecting both graphs.
The specification is stored in the XML format defined by OpenDSE.

## Features

The following architecture types are supported:

* Gateway
* NoC
* Backbone

The following application patterns are supported:

* N parallel functions with a configurable width in their application graphs
* Mixed-Criticality application that contains safety-critical and non-critical functions

## Java Versions

OpenDSE-TestGen requires Java 21 (LTS) or higher.

## Credits

Brought to you by

* Alexander Reichle
* Lukas Pfeifer
* Michael Glaß

This project uses

* [opendse](https://github.com/sdarg/opendse) as underlying graph-based system model