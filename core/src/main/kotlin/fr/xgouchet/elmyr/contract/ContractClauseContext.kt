package fr.xgouchet.elmyr.contract

import fr.xgouchet.elmyr.annotation.ContractClause
import fr.xgouchet.elmyr.annotation.ContractOptIn

/**
 * A class holding the constructor and clause parameters as a context to run the tests for a
 * [ContractClause] annotated method.
 */
@ContractOptIn
data class ContractClauseContext(
    val clauseName: String,
    val constructorParameters: Array<Any?>,
    val clauseParameters: Array<Any?>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ContractClauseContext

        if (clauseName != other.clauseName) return false
        if (!constructorParameters.contentEquals(other.constructorParameters)) return false
        if (!clauseParameters.contentEquals(other.clauseParameters)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = clauseName.hashCode()
        result = 31 * result + constructorParameters.contentHashCode()
        result = 31 * result + clauseParameters.contentHashCode()
        return result
    }
}