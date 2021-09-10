package fr.xgouchet.elmyr.mockito.clause

import fr.xgouchet.elmyr.contract.shouldThrow

class ValidatingOngoingClause<T : Any, O>(
    private val subject: T,
    private val function: (T) -> O
) : OngoingClause<T, O> {

    override fun thenReturn(value: O): CompleteClause<T> {
        val actual = function(subject)
        if (actual != value) {
            throw AssertionError(
                String.format(
                    "[%nExpecting:%n <%s>%nto be equal to:%n <%s>%nbut was not.] ",
                    actual,
                    value
                )
            )
        }
        return this
    }

    override fun thenThrow(vararg throwables: Throwable): CompleteClause<T> {
        throwables.forEach {
            subject.shouldThrow(it) {
                function(subject)
            }
        }
        return this
    }
}
