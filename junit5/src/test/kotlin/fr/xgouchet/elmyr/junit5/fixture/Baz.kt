package fr.xgouchet.elmyr.junit5.fixture

internal data class Baz(
    val i: Int,
    val s: String,
    val type: Type
) {
    enum class Type { A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z }
}
