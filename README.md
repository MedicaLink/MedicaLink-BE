# MedicaLink EHR Backend
This is the official backend project of the MedicaLink EHR v1 application.

## About
This is a java spring boot application aimed at providing a rich REST API for all MedicaLink client portals.

## Getting Started
The MedicaLink backend is composed of the following three major components.
 - The Spring Boot Rest API.
 - FHIR Server Implementation.
 - Database Layer.

### The Spring Boot Rest API
Clone the repository and sync gradle.

### FHIR Server Implementation
MedicaLink uses an instance of the standard FHIR server offered by [HAPI](https://hapifhir.io/). A docker image of HAPI FHIR is used.
