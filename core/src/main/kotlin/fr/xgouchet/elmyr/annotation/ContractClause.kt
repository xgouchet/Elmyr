package fr.xgouchet.elmyr.annotation

/**
 * Mark a a method as a Contract clause.
 * @param description a description of the clause and its context
 */
@ContractOptIn
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
annotation class ContractClause(val description: String)
