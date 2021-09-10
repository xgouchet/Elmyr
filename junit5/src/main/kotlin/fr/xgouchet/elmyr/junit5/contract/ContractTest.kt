package fr.xgouchet.elmyr.junit5.contract

import fr.xgouchet.elmyr.annotation.ContractOptIn
import fr.xgouchet.elmyr.contract.Contract
import fr.xgouchet.elmyr.contract.ContractClauseContext
import fr.xgouchet.elmyr.contract.ContractValidator
import org.junit.jupiter.params.ParameterizedTest

@ContractOptIn
abstract class ContractTest<T : Any, C : Contract<T>> : ContractValidator<T, C>() {

    // Contract validation

    @Suppress("Junit5MalformedParameterized")
    @ParameterizedTest
    @ContractClauseSource
    fun testClauseContext(clauseContext: ContractClauseContext) {
        validateClause(clauseContext)
    }

    fun generateClauseContexts(): Iterable<ContractClauseContext> {
        return instantiateContract().generateClauseContexts()
    }

    // endregion

}