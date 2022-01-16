package fr.xgouchet.elmyr.spek

import fr.xgouchet.elmyr.Forge
import kotlin.reflect.full.memberProperties
import org.spekframework.spek2.lifecycle.ExecutionResult
import org.spekframework.spek2.lifecycle.GroupScope
import org.spekframework.spek2.lifecycle.LifecycleListener
import org.spekframework.spek2.lifecycle.Scope
import org.spekframework.spek2.lifecycle.TestScope

/**
 * A Spek [LifecycleListener] used to synchronize your [Forge] instance with Spek.
 * @property forge the [Forge] instance to keep synced with Spek scopes
 * @property seeds the seeds map (provide a specific seed for each scope in your Spek class)
 */
class ForgeLifecycleListener(
    private val forge: Forge,
    private val seeds: Map<String, Long>
) : LifecycleListener {

    private val usedSeeds: MutableMap<String, Long> = mutableMapOf()

    // region LifecycleListener

    /** @inheritdoc */
    override fun beforeExecuteGroup(group: GroupScope) {
        super.beforeExecuteGroup(group)
        beforeExecuteScope(group)
    }

    /** @inheritdoc */
    override fun beforeExecuteTest(test: TestScope) {
        super.beforeExecuteTest(test)
        beforeExecuteScope(test)
    }

    /** @inheritdoc */
    override fun afterExecuteTest(test: TestScope, result: ExecutionResult) {
        super.afterExecuteTest(test, result)
        if (result is ExecutionResult.Failure) {
            System.err.println(getTestFailedMessage(test))
        }
    }

    // endregion

    // region Internal

    private fun beforeExecuteScope(scope: Scope) {
        println("scope ${scope.path()}")
        val scopePath = scope.path()
        val seed = seeds[scopePath] ?: Forge.seed()
        forge.seed = seed
        usedSeeds[scopePath] = seed
    }

    private fun getTestFailedMessage(test: TestScope): String {
        val mapEntries = mutableListOf<String>()
        var scope: Scope? = test

        while (scope != null) {
            val path = scope.path()
            val seed = usedSeeds[path]
            if (seed != null) {
                mapEntries.add("\t\t\t\"$path\" to 0x${seed.toString(RADIX_HEXA)}L")
            }
            scope = scope.parent
        }

        return TEST_FAIL_MESSAGE.format(
            test.path(),
            forge.seed,
            mapEntries.joinToString(",\n")
        )
    }

    private fun Scope.path(): String {
        val scopeId = this.getProperty("id") ?: return ""
        val scopeName = scopeId.getProperty("name") ?: return ""
        val type = scopeId.getProperty("type") ?: ""
        val typeName = type.toString().lowercase()

        val simpleName = if (typeName == "class") {
            scopeName.toString().split(".").last()
        } else {
            scopeName.toString()
        }
        val parentPath = parent?.path()
        return if (parentPath == null) {
            simpleName
        } else {
            "$parentPath/$simpleName"
        }
    }

    private fun <T : Any> T.getProperty(name: String): Any? {
        val targetClass = javaClass.kotlin
        val property = targetClass.memberProperties.firstOrNull { it.name == name }
        return property?.invoke(this)
    }

    companion object {

        private const val RADIX_HEXA = 16

        private const val TEST_FAIL_MESSAGE = "<%s> failed with Forge seed 0x%xL\n" +
            "Add the following seeds to your Spek class :\n\n" +
            "\tval forge = spekForge(\n" +
            "\t\tmapOf(\n" +
            "%s\n" +
            "\t\t)\n" +
            "\t)"
    }
}
