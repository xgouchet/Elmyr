package fr.xgouchet.elmyr.annotation

/**
 * Marks classes used in the experimental Contract feature
 */
@RequiresOptIn(message = "This API is experimental. It may be changed in the future without notice.")
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
annotation class ContractOptIn