package fr.xgouchet.elmyr.junit5

import fr.xgouchet.elmyr.ForgeConfigurator
import java.lang.annotation.Inherited
import kotlin.reflect.KClass
import org.junit.platform.commons.annotation.Testable

/**
 * Annotate a test class to configure the Forgery extension.
 */
@Testable
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Inherited
annotation class ForgeConfiguration(
    val value: KClass<out ForgeConfigurator>
)
