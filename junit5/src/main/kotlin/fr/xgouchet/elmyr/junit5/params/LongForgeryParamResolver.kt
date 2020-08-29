package fr.xgouchet.elmyr.junit5.params

import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.annotation.LongForgery

internal class LongForgeryParamResolver :
    PrimitiveForgeryParamResolver<LongForgery>(
        java.lang.Long.TYPE,
        java.lang.Long::class.java,
        LongForgery::class.java
    ) {

    // region PrimitiveForgeryParamResolver

    override fun forgePrimitive(annotation: LongForgery, forge: Forge): Any? {
        return if (annotation.standardDeviation >= 0) {
            check(annotation.min == Long.MIN_VALUE) {
                "You can only use an LongForgery with min and max or with mean and standardDeviation"
            }
            check(annotation.max == Long.MAX_VALUE) {
                "You can only use an LongForgery with min and max or with mean and standardDeviation"
            }
            forge.aGaussianLong(annotation.mean, annotation.standardDeviation)
        } else {
            check(annotation.mean == 0L) {
                "You can only use an LongForgery with min and max or with mean and standardDeviation"
            }
            forge.aLong(annotation.min, annotation.max)
        }
    }

    // endregion
}
