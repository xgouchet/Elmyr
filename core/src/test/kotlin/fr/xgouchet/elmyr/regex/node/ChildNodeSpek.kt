package fr.xgouchet.elmyr.regex.node

import fr.xgouchet.elmyr.Forge
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

/**
 * @author Xavier F. Gouchet
 */
class ChildNodeSpek : Spek({

    val stranger = object : BaseParentNode() {
        override fun build(forge: Forge, builder: StringBuilder) {}
    }

    describe("A child node") {

        context("with parent not child node and not root node") {

            val parent = object : BaseParentNode() {
                override fun build(forge: Forge, builder: StringBuilder) {}
            }

            val childNode = object : ChildNode {

                override fun getParent(): ParentNode = parent

                override fun build(forge: Forge, builder: StringBuilder) {}
            }

            it("is a descendant of its parent") {
                assertThat(childNode.isDescendantOf(parent)).isTrue()
            }

            it("is not a descendant of a stranger") {
                assertThat(childNode.isDescendantOf(stranger)).isFalse()
            }
        }

        context("with deep hierachy") {

            val parent = object : BaseParentNode() {
                override fun build(forge: Forge, builder: StringBuilder) {}
            }

            val childNode = object : ChildNode {
                override fun getParent(): ParentNode = parent

                override fun build(forge: Forge, builder: StringBuilder) {}
            }

            it("is a descendant of its parent") {
                assertThat(childNode.isDescendantOf(parent)).isTrue()
            }
        }
    }

    describe("A child node implementing ParentNode") {

        context("with parent not child node and not root node") {

            val parent = object : BaseParentNode() {
                override fun build(forge: Forge, builder: StringBuilder) {}
            }

            val childNode = object : BaseParentNode(), ChildNode {
                override fun getParent(): ParentNode = parent

                override fun build(forge: Forge, builder: StringBuilder) {}
            }

            it("is a descendant of itself") {
                assertThat(childNode.isDescendantOf(childNode)).isTrue()
            }

            it("is a descendant of its parent") {
                assertThat(childNode.isDescendantOf(parent)).isTrue()
            }

            it("is not a descendant of a stranger") {
                assertThat(childNode.isDescendantOf(stranger)).isFalse()
            }
        }
    }
})
