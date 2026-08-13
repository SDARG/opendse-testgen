[![OpenDSE-TestGen Build & Test](https://github.com/SDARG/opendse-testgen/actions/workflows/gradle.yml/badge.svg)](https://github.com/SDARG/opendse-testgen/actions/workflows/gradle.yml)

# OpenDSE-TestGen
_OpenDSE-TestGen_  is a testcase generator for the OpenDSE framework, written in Java.
It generates a specification graph consisting of an application graph, an architecture graph, and a set of mapping edges connecting both graphs.
The specification is stored in the XML format defined by OpenDSE.

## Features
The following architecture types are supported:
*	Gateway
*	NoC
*	Backbone

The following application patterns are supported:
*	N parallel functions with a configurable width in their application graphs
*	Mixed-Criticality application that contains safety-critical and non-critical functions

## Java Versions
OpenDSE-TestGen requires Java 21 (LTS) or higher.


## Credits
Brought to you by
*   Alexander Reichle
*   Lukas Pfeifer
*   Michael Glaß

This project uses
*   [opendse](https://github.com/sdarg/opendse) as underlying graph-based system model
