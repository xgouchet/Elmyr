
### Check

You can run all the local checks with the `./gradlew check` task.

 - Detekt
 - Ktlint
 - Unit tests

### Publishing

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