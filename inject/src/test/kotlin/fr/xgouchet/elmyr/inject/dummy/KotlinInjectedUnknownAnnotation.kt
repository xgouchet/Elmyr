package fr.xgouchet.elmyr.inject.dummy

import org.mockito.Mock

class KotlinInjectedUnknownAnnotation {

    @Mock
    lateinit var unknownFoo: Foo

    fun retrieveFooOrNull(): Foo? {
        return if (::unknownFoo.isInitialized) {
            unknownFoo
        } else {
            null
        }
    }
}
