package fr.xgouchet.elmyr.dummy

internal sealed class Food {

    class Egg(
        val b: Boolean
    ) : Food()

    class Bacon(
        val b: Boolean
    ) : Food()
}
