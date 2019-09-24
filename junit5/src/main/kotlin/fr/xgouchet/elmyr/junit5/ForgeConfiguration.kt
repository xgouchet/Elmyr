package fr.xgouchet.elmyr.junit5

import org.junit.platform.commons.annotation.Testable

/**
 * Annotate a test class to configure the Forgery extension.
 *
 * TODO s
 */
@Testable
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class ForgeConfiguration
