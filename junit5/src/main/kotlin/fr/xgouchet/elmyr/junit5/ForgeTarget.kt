package fr.xgouchet.elmyr.junit5

sealed class ForgeTarget<T>(
    val type: String
) {
    abstract val parent: String
    abstract val name: String
    abstract val value: T

    data class ForgeParamTarget<T>(
        override val parent: String,
        override val name: String,
        override val value: T
    ) : ForgeTarget<T>("param")

    data class ForgeFieldTarget<T>(
        override val parent: String,
        override val name: String,
        override val value: T
    ) : ForgeTarget<T>("field")
}
