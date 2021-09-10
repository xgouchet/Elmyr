package fr.xgouchet.elmyr.mockito.test

import fr.xgouchet.elmyr.annotation.ContractOptIn
import fr.xgouchet.elmyr.junit5.contract.ContractTest
import fr.xgouchet.elmyr.mockito.prod.ArrayDataSource
import fr.xgouchet.elmyr.mockito.prod.DataSource

@OptIn(ContractOptIn::class)
class ArrayDataSourceTest : ContractTest<DataSource, DataSourceContract>() {

    override fun instantiateContract(): DataSourceContract {
        return DataSourceContract()
    }

    override fun instantiateSubject(constructorParameters: Array<Any?>): DataSource {
        val size = constructorParameters.firstOrNull() as? Int
        checkNotNull(size)
        return ArrayDataSource(size)
    }
}