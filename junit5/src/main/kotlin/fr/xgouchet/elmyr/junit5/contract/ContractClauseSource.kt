package fr.xgouchet.elmyr.junit5.contract

import fr.xgouchet.elmyr.annotation.ContractOptIn
import org.junit.jupiter.params.provider.ArgumentsSource

/**
 * An [ArgumentsSource] providing arguments to a test method annotated with this.
 */
@ContractOptIn
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@ArgumentsSource(ContractClauseContextArgumentsProvider::class)
annotation class ContractClauseSource
