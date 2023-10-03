package fr.xgouchet.elmyr.ksp

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFile
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSName
import com.google.devtools.ksp.symbol.KSNode
import com.google.devtools.ksp.symbol.KSValueParameter
import com.google.devtools.ksp.symbol.Modifier
import com.google.devtools.ksp.visitor.KSDefaultVisitor
import com.squareup.kotlinpoet.BOOLEAN
import com.squareup.kotlinpoet.BYTE
import com.squareup.kotlinpoet.CHAR
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.DOUBLE
import com.squareup.kotlinpoet.FLOAT
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.INT
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.LONG
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.SHORT
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.toKModifier
import com.squareup.kotlinpoet.ksp.toTypeName
import java.io.IOException
import java.io.OutputStreamWriter

/**
 * A Kotlin [SymbolProcessor] generating a [ForgeryFactory] implementation for every
 * data class in the compiled project.
 * @property codeGenerator the [CodeGenerator] used to create new code
 * @property logger the [KSPLogger] reporting info, warnings and errors while processing symbols
 */
class ForgerableProcessor(
    val codeGenerator: CodeGenerator,
    val logger: KSPLogger
) : SymbolProcessor {

    private var invoked = false

    /** @inheritdoc */
    override fun process(resolver: Resolver): List<KSAnnotated> {
        if (invoked) {
            logger.info("Already invoked, ignoring")
            return emptyList()
        }

        logger.info("Processing…")

        val visitor = FileVisitor(ClassVisitor(ConstructorVisitor()))
        val dataClasses = resolver.getAllFiles()

        dataClasses.map {
            it.accept(visitor, Unit)
        }.toList()

        invoked = true

        return emptyList()
    }

    private inner class FileVisitor(
        val classVisitor: KSDefaultVisitor<KSFile, Unit>
    ) : KSDefaultVisitor<Unit, Unit>() {

        override fun visitFile(file: KSFile, data: Unit) {
            logger.info("… File ${file.fileName}")

            file.declarations.forEach {
                it.accept(classVisitor, file)
            }

            logger.info("✔ File ${file.fileName}")
        }

        override fun defaultHandler(node: KSNode, data: Unit) {
            // no-op
        }
    }

    private inner class ClassVisitor(
        val constructorVisitor: KSDefaultVisitor<ConstructorContext, Unit>
    ) : KSDefaultVisitor<KSFile, Unit>() {

        override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: KSFile) {
            val classKind = classDeclaration.classKind
            val className = classDeclaration.simpleName.asString()
            logger.info("… $classKind $className")

            val modifiers = classDeclaration.modifiers
            val visibility = modifiers.firstOrNull { it in VISIBILITY_MODIFIERS }
            if (modifiers.contains(Modifier.DATA) && visibility in VISIBLE_MODIFIERS) {
                val primaryConstructor = classDeclaration.primaryConstructor
                val qualifiedName = classDeclaration.qualifiedName
                if (primaryConstructor == null) {
                    logger.warn("$classKind $className has no primary constructor")
                } else if (qualifiedName == null) {
                    logger.warn("$classKind $className has no primary constructor")
                } else {
                    val context = ConstructorContext(
                        data,
                        classDeclaration.packageName,
                        qualifiedName,
                        visibility
                    )
                    primaryConstructor.accept(constructorVisitor, context)
                }
            }

            classDeclaration.declarations.forEach { it.accept(this, data) }

            logger.info("✔ $classKind $className")
        }

        override fun defaultHandler(node: KSNode, data: KSFile) {
            // no-op
        }
    }

    private inner class ConstructorVisitor : KSDefaultVisitor<ConstructorContext, Unit>() {
        override fun visitFunctionDeclaration(
            function: KSFunctionDeclaration,
            data: ConstructorContext
        ) {
            logger.info("… ${data.classQualifiedName.asString()}.${function.simpleName.asString()}()")

            val fileSpec = generateFile(
                function,
                data.packageName,
                data.visibility?.toKModifier(),
                data.classQualifiedName
            )
            val dependencies = Dependencies(false, data.file)
            try {
                val outputStream = codeGenerator.createNewFile(
                    dependencies,
                    fileSpec.packageName,
                    fileSpec.name
                )
                val writer = OutputStreamWriter(outputStream, Charsets.UTF_8)
                fileSpec.writeTo(writer)
                try {
                    writer.flush()
                    writer.close()
                } catch (e: IOException) {
                    logger.warn(
                        "Error flushing writer for file ${fileSpec.packageName}.${fileSpec.name}: " +
                            "${e.message}."
                    )
                }
            } catch (e: IOException) {
                logger.error("Error writing file ${fileSpec.packageName}.${fileSpec.name}")
                logger.exception(e)
            }

            logger.info("✔ ${data.classQualifiedName.asString()}.${function.simpleName.asString()}()")
        }

        override fun defaultHandler(node: KSNode, data: ConstructorContext) {
            // no-op
        }

        private fun generateFile(
            function: KSFunctionDeclaration,
            packageName: KSName,
            visibility: KModifier?,
            classQualifiedName: KSName
        ): FileSpec {
            val factoryName = "${classQualifiedName.getShortName()}ForgeryFactory"
            logger.info("Creating file $factoryName")
            val fileSpecBuilder = FileSpec.builder(packageName.asString(), factoryName)

            fileSpecBuilder.addType(
                generateForgeryType(
                    function,
                    classQualifiedName,
                    visibility,
                    factoryName
                )
            )

            return fileSpecBuilder.indent("    ").build()
        }

        private fun generateForgeryType(
            function: KSFunctionDeclaration,
            classQualifiedName: KSName,
            visibility: KModifier?,
            factoryName: String
        ): TypeSpec {
            val returnType = ClassName.bestGuess(classQualifiedName.asString())
            val builder = TypeSpec.classBuilder(factoryName)
            if (visibility != null) {
                builder.addModifiers(visibility)
            }
            return builder
                .addSuperinterface(
                    ClassName.bestGuess("fr.xgouchet.elmyr.ForgeryFactory")
                        .parameterizedBy(returnType)
                )
                .addFunction(generateForgeryFun(function, returnType)).build()
        }

        private fun generateForgeryFun(
            function: KSFunctionDeclaration,
            returnType: TypeName
        ): FunSpec {
            val funSpecBuilder = FunSpec.builder("getForgery")
                .addModifiers(KModifier.OVERRIDE).returns(returnType)
                .addParameter("forge", ClassName.bestGuess("fr.xgouchet.elmyr.Forge"))

            val statementBuilder = CodeBlock.builder()

            function.parameters.forEach {
                funSpecBuilder.addCode(generateParameterForgery(it))
            }

            val arguments = function.parameters.joinToString(", ") { "$it = $it" }

            statementBuilder.addStatement("return %T(%L)", returnType, arguments)

            return funSpecBuilder.addCode(statementBuilder.build()).build()
        }

        private fun generateParameterForgery(
            parameter: KSValueParameter
        ): CodeBlock {
            logger.info("Converting type ${parameter.type}")
            val resolvedType = parameter.type.resolve()
            val isNullable = resolvedType.isMarkedNullable
            val nonNullType = resolvedType.makeNotNullable()
            val typeName = nonNullType.toTypeName()
            val forgeryMethod = when {
                typeName == STRING -> "aString()"
                typeName == BOOLEAN -> "aBool()"
                typeName == BYTE -> "aByte()"
                typeName == SHORT -> "aShort()"
                typeName == INT -> "anInt()"
                typeName == LONG -> "aLong()"
                typeName == FLOAT -> "aFloat()"
                typeName == DOUBLE -> "aDouble()"
                typeName == CHAR -> "aChar()"
                else -> "getForgery()"
            }
            val forgeryStatement = if (isNullable) {
                "forge.aNullable { $forgeryMethod }"
            } else {
                "forge.$forgeryMethod"
            }

            return CodeBlock.builder()
                .addStatement(
                    "val %L: %T = %L",
                    parameter.name?.asString(),
                    parameter.type.toTypeName(),
                    forgeryStatement
                ).build()
        }
    }

    internal data class ConstructorContext(
        val file: KSFile,
        val packageName: KSName,
        val classQualifiedName: KSName,
        val visibility: Modifier?
    )

    companion object {
        private val VISIBILITY_MODIFIERS = arrayOf(
            Modifier.PUBLIC,
            Modifier.PRIVATE,
            Modifier.INTERNAL,
            Modifier.PROTECTED
        )

        private val VISIBLE_MODIFIERS = arrayOf(
            Modifier.PUBLIC,
            Modifier.INTERNAL,
            null
        )
    }
}
