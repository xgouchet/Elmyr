package fr.xgouchet.elmyr

class Foo(
    val b: Boolean
)

class Bar(
    val b: Boolean
)

open class Food

class Egg(
    val b: Boolean
) : Food()

class Bacon(
    val b: Boolean
) : Food()