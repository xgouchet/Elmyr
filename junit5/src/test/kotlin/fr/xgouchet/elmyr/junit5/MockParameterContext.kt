package fr.xgouchet.elmyr.junit5

import java.lang.reflect.Method
import java.lang.reflect.Parameter
import java.util.Optional
import org.junit.jupiter.api.extension.ParameterContext

class MockParameterContext(
    private val targetClass: Class<*>,
    private val targetMethod: Method,
    private val targetParameter: Parameter
) : ParameterContext {

    override fun <A : Annotation?> findRepeatableAnnotations(annotationType: Class<A>?): MutableList<A> {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun <A : Annotation?> findAnnotation(annotationType: Class<A>?): Optional<A> {
        return Optional.ofNullable(parameter.getAnnotationsByType(annotationType).firstOrNull())
    }

    override fun getParameter(): Parameter {
        return targetParameter
    }

    override fun getIndex(): Int {
        return 0
    }

    override fun getTarget(): Optional<Any> {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun isAnnotated(annotationType: Class<out Annotation>?): Boolean {
        return targetParameter.isAnnotationPresent(annotationType)
    }
}
