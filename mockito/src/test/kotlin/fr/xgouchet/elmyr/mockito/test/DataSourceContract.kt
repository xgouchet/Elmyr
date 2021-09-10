package fr.xgouchet.elmyr.mockito.test

import fr.xgouchet.elmyr.annotation.ContractClause
import fr.xgouchet.elmyr.annotation.ContractOptIn
import fr.xgouchet.elmyr.contract.ContractClauseContext
import fr.xgouchet.elmyr.mockito.MockitoContract
import fr.xgouchet.elmyr.mockito.prod.DataSource

@ContractOptIn
open class DataSourceContract : MockitoContract<DataSource>(
    DataSource::class.java
) {

    // region Contract

    override fun generateClauseContexts(): Iterable<ContractClauseContext> {
        return listOf(
            ContractClauseContext("withSize", arrayOf(42), arrayOf(42)),
            ContractClauseContext("withDataAt", arrayOf(42), arrayOf(13, "foo"))
        )
    }

    // endregion

    @ContractClause("Size property, IOOBException when querying outside of bounds")
    fun withSize(size: Int) {
        whenever { it.size() }.thenReturn(size)
        whenever { it.getDataAt(-1) }.thenThrow(IndexOutOfBoundsException())
        whenever { it.getDataAt(size) }.thenThrow(IndexOutOfBoundsException())
    }

    @ContractClause("Ensures stored data at index is retrieved")
    fun withDataAt(index: Int, data: String) {
        applyIfImplementation {
            putData(it, index, data)
        }
        whenever { it.getDataAt(index) }.thenReturn(data)
    }

    // region Internal

    private fun putData(dataSource: DataSource, index: Int, data: String) {
        dataSource.put(index, data)
    }

}