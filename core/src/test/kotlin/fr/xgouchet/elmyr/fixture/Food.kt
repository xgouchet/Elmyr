package fr.xgouchet.elmyr.fixture

internal sealed class Food {

    class Egg(
        val b: Boolean
    ) : Food()

    class Bacon(
        val b: Boolean
    ) : Food()
}
