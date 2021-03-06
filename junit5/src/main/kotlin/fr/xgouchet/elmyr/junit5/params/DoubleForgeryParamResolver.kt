package fr.xgouchet.elmyr.junit5.params

import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.annotation.DoubleForgery

internal object DoubleForgeryParamResolver :
    PrimitiveForgeryParamResolver<DoubleForgery>(
        java.lang.Double.TYPE,
        java.lang.Double::class.java,
        DoubleForgery::class.java
    ) {

    // region PrimitiveForgeryParamResolver

    override fun forgePrimitive(annotation: DoubleForgery, forge: Forge): Any? {
        return if (!annotation.standardDeviation.isNaN()) {
            check(annotation.min == -Double.MAX_VALUE) {
                "You can only use an DoubleForgery with min and max or with mean and standardDeviation"
            }
            check(annotation.max == Double.MAX_VALUE) {
                "You can only use an DoubleForgery with min and max or with mean and standardDeviation"
            }
            forge.aGaussianDouble(annotation.mean, annotation.standardDeviation)
        } else {
            check(annotation.mean == 0.0) {
                "You can only use an DoubleForgery with min and max or with mean and standardDeviation"
            }
            forge.aDouble(annotation.min, annotation.max)
        }
    }

    // endregion
}
