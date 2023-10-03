package fr.xgouchet.elmyr.inject.fixture

internal data class Baz(
    val i: Int,
    val s: String,
    val type: Type
) {
    enum class Type { A, B, C }
}
