package fr.xgouchet.elmyr.junit5

import fr.xgouchet.elmyr.annotation.Forgery
import fr.xgouchet.elmyr.junit5.dummy.Foo
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

internal class KotlinAnnotationChildTest : KotlinAnnotationTest() {

    @Test
    fun withForgeryFromParentClass(@Forgery foo: Foo) {
        Assertions.assertThat(foo).isNotNull()
    }
}
