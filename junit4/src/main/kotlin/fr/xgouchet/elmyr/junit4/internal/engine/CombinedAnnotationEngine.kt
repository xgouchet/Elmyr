package fr.xgouchet.elmyr.junit4.internal.engine

import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.junit4.ForgeAnnotationEngine

internal class CombinedAnnotationEngine(
    forge: Forge
) : ForgeAnnotationEngine {

    private val delegates = listOf(
            JavaAnnotationEngine(forge),
            KotlinAnnotationEngine(forge)
    )

    override fun process(testInstance: Any) {
        delegates.forEach {
            it.process(testInstance)
        }
    }
}