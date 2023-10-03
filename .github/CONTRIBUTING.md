
# Contributing Guide

First of all, thanks for reading this guide, as any contribution to this project is deeply appreciated. 

## Code guidelines 

### Coding conventions

Start reading our code and you'll get the hang of it. We use `KtLint` to ensure we use a common syntax, and follow standard Kotlin conventions; 

This is open source software. Consider the people who will read your code, and make it look nice for them.

### Check

The whole project is tested, and covered by static analysis tools to ensure reliability and maintainability. 

You can run all the local checks with the `./gradlew check` task.

 - Detekt
 - KtLint
 - Unit tests

### Submitting a PR

Any Pull Request is welcome. Before submitting a Pull Request, please verify you comply with the following checklist :

- [x] All public classes, methods and fields must be documented
- [x] All code must be unit tested (duh…)
- [x] All code should be usable with and without the Android SDK, from Java and Kotlin

### Publishing (for maintainers)

To be able to publish on Maven Central, you need to create a `local.properties` file 
at the root of the project with the following properties:

```
# Maven OSSRH credentials
ossrhUsername=
ossrhPassword=

# GPG Signing key info
signing.keyId=…
signing.secretKeyRingFile=…
signing.password=…
```