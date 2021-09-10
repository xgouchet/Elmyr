package fr.xgouchet.elmyr.mockito.test

import fr.xgouchet.elmyr.Forge
import fr.xgouchet.elmyr.annotation.ContractDefinition
import fr.xgouchet.elmyr.annotation.ContractOptIn
import fr.xgouchet.elmyr.annotation.IntForgery
import fr.xgouchet.elmyr.annotation.StringForgery
import fr.xgouchet.elmyr.junit5.ForgeExtension
import fr.xgouchet.elmyr.mockito.prod.DataSourceWalker
import fr.xgouchet.elmyr.mockito.prod.DataWalker
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assumptions.assumeTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.Extensions
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.junit.jupiter.MockitoSettings
import org.mockito.quality.Strictness

@OptIn(ContractOptIn::class)
@Extensions(
    ExtendWith(MockitoExtension::class),
    ExtendWith(ForgeExtension::class)
)
@MockitoSettings(strictness = Strictness.LENIENT)
class DataSourceWalkerTest {

    lateinit var testedWalker: DataWalker

    @ContractDefinition
    lateinit var contractDataSource: DataSourceContract

    @IntForgery(min = 1, max = 512)
    var fakeSize: Int = 0

    @BeforeEach
    fun setUp() {
        // TODO automatic generation with Elmyr
        contractDataSource = DataSourceContract().apply {
            withMock()
        }
        // System.out.println("Using contract " + contractDataSource + " / " + contractDataSource.getMock());
        testedWalker = DataSourceWalker(contractDataSource.getMock())
    }

    //region walkToNext

    @Test
    fun walksToNextInTheMiddleOfTheDataSet(
        forge: Forge,
        @StringForgery fakeData: String
    ) {
        assumeTrue(fakeSize > 2)
        // Given
        val position: Int = forge.anInt(0, fakeSize - 2)
        contractDataSource.withSize(fakeSize)
        contractDataSource.withDataAt(position + 1, fakeData)

        // When
        testedWalker.walkTo(position)
        testedWalker.walkForward()
        val current = testedWalker.getCurrent()

        // Then
        assertThat(current).isEqualTo(fakeData)
    }


    @Test
    fun walksToNextAtTheEndOfTheDataSetWithLoopEnabled(
        @StringForgery fakeData: String
    ) {
        // Given
        contractDataSource.withSize(fakeSize)
        contractDataSource.withDataAt(0, fakeData)
        testedWalker.setLoopEnabled(true)

        // When
        testedWalker.walkTo(fakeSize - 1)
        testedWalker.walkForward()
        val current = testedWalker.getCurrent()

        // Then
        assertThat(current).isEqualTo(fakeData)
    }

    @Test
    fun walksToNextAtTheEndOfTheDataSetWithoutLoopEnabled(
        @StringForgery fakeData: String
    ) {
        // Given
        contractDataSource.withSize(fakeSize)
        contractDataSource.withDataAt(0, fakeData)
        testedWalker.setLoopEnabled(false)

        // When
        testedWalker.walkTo(fakeSize - 1)
        testedWalker.walkForward()

        // Then
        assertThrows<IndexOutOfBoundsException> {
            testedWalker.getCurrent()
        }
    }

    // endregion
}