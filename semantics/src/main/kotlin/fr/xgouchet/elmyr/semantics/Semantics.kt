package fr.xgouchet.elmyr.semantics

import fr.xgouchet.elmyr.Case
import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.semantics.markov.MarkovReader
import fr.xgouchet.elmyr.semantics.markov.MarkovTable

internal val firstNameTable: MarkovTable by lazy {
    MarkovReader().parse("/first_name_mkv.csv")
}

internal val lastNameTable: MarkovTable by lazy {
    MarkovReader().parse("/last_name_mkv.csv")
}

internal val lipsumTable: MarkovTable by lazy {
    MarkovReader().parse("/lipsum_mkv.csv")
}

fun Forge.aFirstName(): String {
    return firstNameTable.generate(this).capitalize()
}

fun Forge.aLastName(): String {
    return lastNameTable.generate(this).capitalize()
}

fun Forge.anEmail(): String {
    val firstName = aFirstName()
    val lastName = aLastName()
    val ext = anAlphabeticalString(Case.LOWER, anInt(2,5))
    return "$firstName.$lastName@example.$ext"
}

fun Forge.lipsum(): String {
    return lipsumTable.generate(this).capitalize()
}