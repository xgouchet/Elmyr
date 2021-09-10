package fr.xgouchet.elmyr.contract

import fr.xgouchet.elmyr.annotation.ContractOptIn

@ContractOptIn
abstract class ContractValidator<T : Any, C : Contract<T>> {

    abstract fun instantiateContract(): C

    protected abstract fun instantiateSubject(constructorParameters: Array<Any?>): T

    fun validateClauses() {
        val contract = instantiateContract()

        val contextsIterable = contract.generateClauseContexts()

        contextsIterable.forEach { clauseContext ->
            validateClause(clauseContext, contract)
        }
    }

    fun validateClause(
        clauseContext: ContractClauseContext,
        contract: C = instantiateContract()
    ) {
        val subject = instantiateSubject(clauseContext.constructorParameters)
        contract.withImplementation(subject)

        val method = contract.javaClass.declaredMethods.firstOrNull {
            (it.name == clauseContext.clauseName) &&
                    parametersMatch(clauseContext.clauseParameters, it.parameterTypes)
        }

        if (method == null) {
            throw IllegalStateException(
                "Couldn't match claus ${clauseContext.clauseName} with the given parameters"
            )
        } else {
            method.invoke(contract, *clauseContext.clauseParameters)
        }
    }

    private fun parametersMatch(
        parameters: Array<Any?>,
        parameterTypes: Array<out Class<*>>
    ): Boolean {
        if (parameters.size != parameterTypes.size) {
            return false
        }

        for (i in parameters.indices) {
            val paramType = parameters[i]?.javaClass ?: continue

            val matches = when (parameterTypes[i]) {
                Integer.TYPE -> (paramType == Integer.TYPE) || (paramType == Integer::class.java)
                String::class.java -> (paramType == String::class.java)
                else -> TODO("${parameterTypes[i]} vs $paramType") // expectedType.isAssignableFrom(param.javaClass)
            }

            if (!matches) return false
        }

        return true
    }
}