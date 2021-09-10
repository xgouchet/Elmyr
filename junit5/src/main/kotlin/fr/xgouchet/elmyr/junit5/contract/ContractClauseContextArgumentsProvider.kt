package fr.xgouchet.elmyr.junit5.contract

import fr.xgouchet.elmyr.annotation.ContractClause
import fr.xgouchet.elmyr.annotation.ContractOptIn
import fr.xgouchet.elmyr.contract.ContractClauseContext
import fr.xgouchet.elmyr.contract.ContractValidator
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import java.lang.reflect.Method
import java.util.stream.Stream

/**
 * An [ArgumentsProvider] design to fill arguments for test methods annotated with
 * [ContractClause].
 */
@ContractOptIn
class ContractClauseContextArgumentsProvider : ArgumentsProvider {

    override fun provideArguments(context: ExtensionContext): Stream<out Arguments> {
        val testClass = context.testClass.get()
        val testClassInstance = testClass.getDeclaredConstructor().newInstance()

        val contexts = generateContexts(testClass, testClassInstance)
        val clauses = generateClauses(testClass, testClassInstance)
        checkInvalidContexts(contexts, clauses)
        checkInvalidClauses(clauses, contexts)

        return contexts.map { Arguments.of(it) }.stream()
    }

    private fun checkInvalidClauses(
        clauses: List<Method>,
        contexts: List<ContractClauseContext>
    ) {
        val invalidClauses = clauses.filter { c -> contexts.none { it.clauseName == c.name } }
        if (invalidClauses.isNotEmpty()) {
            throw IllegalStateException(
                "The following clauses are missing from your generateClauseContexts() method\n" +
                        invalidClauses.joinToString("\n") { "- ${it.name}; " }
            )
        }
    }

    private fun checkInvalidContexts(
        contexts: List<ContractClauseContext>,
        clauses: List<Method>
    ) {
        val invalidContexts = contexts.filter { c -> clauses.none { it.name == c.clauseName } }
        if (invalidContexts.isNotEmpty()) {
            throw IllegalStateException(
                "The following contexts are referencing clauses that do not exist, " +
                        "or are not annotated with @ContractClause\n" +
                        invalidContexts.joinToString("\n") {
                            "- ${it.clauseName}; " +
                                    "constructor: ${it.constructorParameters}; " +
                                    "clause: ${it.clauseParameters}"
                        }
            )
        }
    }

    private fun generateClauses(
        testClass: Class<*>,
        testClassInstance: Any?
    ): List<Method> {
        if (testClassInstance !is ContractValidator<*, *>) {
            throw IllegalStateException(
                "Class ${testClass.name} doesn't implement the ContractValidator interface."
            )
        }
        val contractInstance = testClassInstance.instantiateContract()
        val contractClass = contractInstance.javaClass
        val clauses = contractClass.methods.filter {
            it.isAnnotationPresent(ContractClause::class.java)
        }.sortedBy { it.name }
        return clauses
    }

    private fun generateContexts(
        testClass: Class<*>,
        testClassInstance: Any?
    ): List<ContractClauseContext> {
        val generateClauseContextsMethod = testClass.methods.firstOrNull {
            it.name == "generateClauseContexts" &&
                    it.parameterTypes.isEmpty()
        } ?: throw IllegalStateException(
            "Class ${testClass.name} doesn't implement a static generateClauseContexts method."
        )
        val generatedList = generateClauseContextsMethod.invoke(testClassInstance)

        @Suppress("UNCHECKED_CAST")
        val contexts = (generatedList as Iterable<ContractClauseContext>)
            .sortedBy { it.clauseName }
        return contexts
    }
}