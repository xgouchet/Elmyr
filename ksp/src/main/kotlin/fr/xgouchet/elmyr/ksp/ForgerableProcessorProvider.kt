package fr.xgouchet.elmyr.ksp

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

/**
 * a [SymbolProcessorProvider] providing a [ForgerableProcessor].
 */
class ForgerableProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return ForgerableProcessor(
            environment.codeGenerator,
            environment.logger
        )
    }
}
