package fr.xgouchet.elmyr.annotation

/**
 * Mark a a field as a Contract holder.
 */
@ContractOptIn
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class ContractDefinition()
