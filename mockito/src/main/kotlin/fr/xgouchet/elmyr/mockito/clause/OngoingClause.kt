package fr.xgouchet.elmyr.mockito.clause

interface OngoingClause<T : Any, O> : CompleteClause<T> {

    infix fun doReturn(value: O): CompleteClause<T> {
        return thenReturn(value)
    }

    fun thenReturn(value: O): CompleteClause<T>

    // Additional method helps users of JDK7+ to hide heap pollution / unchecked generics array creation
    // TODO fun thenReturn(value: O, vararg values: O): OngoingClause<T, O>

    fun thenThrow(vararg throwables: Throwable): CompleteClause<T>

    // TODO fun thenThrow(throwableType: Class<out Throwable>): OngoingClause<T, O>

    // Additional method helps users of JDK7+ to hide heap pollution / unchecked generics array creation
    // TODO fun thenThrow(toBeThrown: Class<out Throwable>, vararg nextToBeThrown: Class<out Throwable>): OngoingClause<T, O>

    // TODO OngoingClause<TX, O> thenAnswer(Answer<?> answer);
    // TODO OngoingClause<TX, O> then(Answer<?> answer);
}