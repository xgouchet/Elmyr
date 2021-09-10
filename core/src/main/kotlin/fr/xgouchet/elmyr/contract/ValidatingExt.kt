package fr.xgouchet.elmyr.contract


fun <T, O> T.shouldThrow(
    expected: Throwable,
    function: (T) -> O
) {
    var match = false
    var thrown: Throwable? = null
    try {
        function(this)
    } catch (t: Throwable) {
        thrown = t
        match = expected.javaClass.isAssignableFrom(t.javaClass)
    }

    if (thrown == null) {
        throw AssertionError("Expecting " + expected.javaClass.simpleName + " to be thrown")
    } else if (!match) {
        throw AssertionError("Expecting " + expected.javaClass.simpleName + " to be thrown, but was " + thrown.javaClass.simpleName)
    }
}

fun <T, O> T.shouldThrow(
    expected: Class<out Throwable>,
    function: (T) -> O
) {
    var match = false
    var thrown: Throwable? = null
    try {
        function(this)
    } catch (t: Throwable) {
        thrown = t
        match = expected.isAssignableFrom(t.javaClass)
    }

    if (thrown == null) {
        throw AssertionError("Expecting " + expected.simpleName + " to be thrown")
    } else if (!match) {
        throw AssertionError("Expecting " + expected.simpleName + " to be thrown, but was " + thrown.javaClass.simpleName)
    }
}
