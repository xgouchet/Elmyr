package fr.xgouchet.elmyr.mockito

import fr.xgouchet.elmyr.annotation.ContractOptIn
import fr.xgouchet.elmyr.contract.Contract
import fr.xgouchet.elmyr.mockito.clause.MockingOngoingClause
import fr.xgouchet.elmyr.mockito.clause.OngoingClause
import fr.xgouchet.elmyr.mockito.clause.ValidatingOngoingClause
import org.mockito.Mockito

@ContractOptIn
abstract class MockitoContract<T : Any>(
    private val mockedClass: Class<T>
) : Contract<T>() {

    // region Contract

    fun withMock(): MockitoContract<T> {
        withMock(Mockito.mock(mockedClass))
        return this
    }

    // endregion

    // region Mockito

    fun <O> whenever(function: (T) -> O): OngoingClause<T, O> {
        return if (isInstanceMock) {
            MockingOngoingClause(getMock(), function)
        } else {
            ValidatingOngoingClause(getImplementation(), function)
        }
    }
}