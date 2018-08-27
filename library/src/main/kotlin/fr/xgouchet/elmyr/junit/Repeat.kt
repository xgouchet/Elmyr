package fr.xgouchet.elmyr.junit

/**
 * @property count the number of times a test must be ran
 * @property failureThreshold the number of accepted failed tests
 * @author Xavier F. Gouchet
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class Repeat(val count: Int,
                        val failureThreshold: Int = 0,
                        val ignoreThreshold: Int = 0)
