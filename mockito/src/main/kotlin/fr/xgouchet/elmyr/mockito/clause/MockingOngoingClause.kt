package fr.xgouchet.elmyr.mockito.clause

import org.mockito.kotlin.doReturn
import org.mockito.kotlin.whenever

class MockingOngoingClause<T : Any, O>(
    private val subject: T,
    private val function: (T) -> O
) : OngoingClause<T, O> {

    override fun thenReturn(value: O): CompleteClause<T> {
        whenever(function(subject)).thenReturn(value)
        return this
    }

    override fun thenThrow(vararg throwables: Throwable): CompleteClause<T> {
        whenever(function(subject)).thenThrow(*throwables)
        return this
    }
}
