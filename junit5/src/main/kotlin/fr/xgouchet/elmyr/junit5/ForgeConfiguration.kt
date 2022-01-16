package fr.xgouchet.elmyr.junit5

import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.ForgeConfigurator
import java.lang.annotation.Inherited
import kotlin.reflect.KClass
import org.junit.platform.commons.annotation.Testable

/**
 * Annotate a test class to configure the Forgery extension.
 *
 * @property value the [ForgeConfigurator] instance to use (leave empty if not necessary).
 * @property seed the seed to reset the [Forge] for each test.
 */
@Testable
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Inherited
annotation class ForgeConfiguration(
    val value: KClass<out ForgeConfigurator> = ForgeConfigurator.NoOp::class,
    val seed: Long = 0L
)
