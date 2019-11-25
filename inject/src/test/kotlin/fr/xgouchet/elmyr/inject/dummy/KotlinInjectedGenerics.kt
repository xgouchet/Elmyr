package fr.xgouchet.elmyr.inject.dummy

import fr.xgouchet.elmyr.annotation.Forgery

open class KotlinInjectedGenerics {

    @Forgery lateinit var publicFooList: List<Foo>
    @Forgery lateinit var publicFooSet: Set<Foo>
    @Forgery lateinit var publicFooMap: Map<Foo, Bar>
    @Forgery lateinit var publicFooColletion: Collection<Foo>

    private fun doSomething() {
    }
}
