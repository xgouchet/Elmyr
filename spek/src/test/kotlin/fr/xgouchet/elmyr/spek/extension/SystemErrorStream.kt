package fr.xgouchet.elmyr.spek.extension

import java.io.ByteArrayOutputStream

/**
 * Marks a [ByteArrayOutputStream] parameter in a test method as the [System.err] stream receiver.
 *
 * @see [SystemStreamExtension]
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class SystemErrorStream
