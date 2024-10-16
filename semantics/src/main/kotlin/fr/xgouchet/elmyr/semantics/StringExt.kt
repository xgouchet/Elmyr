package fr.xgouchet.elmyr.semantics

import java.util.Locale

/**
 * Returns a copy of this string having its first letter title-cased,
 * using the rules of the provided locale.
 * @param locale the Locale to use (default: [Locale.US])
 */
fun String.capitalized(locale: Locale = Locale.US): String {
    return replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(locale) else it.toString()
    }
}
