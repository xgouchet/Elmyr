package fr.xgouchet.elmyr.junit4.fixture

internal data class Baz(
    val i: Int,
    val s: String,
    val type: Type
) {
    enum class Type { A, B, C }
}
