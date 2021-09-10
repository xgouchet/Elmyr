/*
 * Unless explicitly stated otherwise all files in this repository are licensed under the Apache License Version 2.0.
 * This product includes software developed at Datadog (https://www.datadoghq.com/).
 * Copyright 2016-Present Datadog, Inc.
 */

package fr.xgouchet.elmyr.contract

import fr.xgouchet.elmyr.annotation.ContractOptIn

@ContractOptIn
abstract class Contract<T : Any> {

    private var instance: T? = null
    protected var isInstanceMock = true
        private set

    // region Contract subject

    /**
     * Make this contract use a mock instance.
     */
    protected fun withMock(mock: T): Contract<T> {
        checkNoInstance()

        instance = mock
        isInstanceMock = true
        return this
    }

    /**
     * Make this contract use a concrete implementation instance.
     *
     * @param implementation the implementation instance
     */
    fun withImplementation(implementation: T): Contract<T> {
        checkNoInstance()

        instance = implementation
        isInstanceMock = false
        return this
    }

    /**
     * @return the mock used by this contract.
     * @throws IllegalStateException if the current instance is not a mock.
     */
    fun getMock(): T {
        return if (isInstanceMock) {
            val msg =
                "This contract is using a null instance : make sure you initialize it using `withMock(…)`"
            instance ?: throw IllegalStateException(msg)
        } else {
            throw IllegalStateException("Contract is not currently using a mock")
        }
    }

    /**
     * @return the concrete instance used by this contract.
     * @throws IllegalStateException if the current instance is a mock.
     */
    fun getImplementation(): T {
        return if (isInstanceMock) {
            throw IllegalStateException("Contract is currently using a mock")
        } else {
            val msg =
                "This contract is using a null instance : make sure you initialize it using `withInstance(…)`"
            instance ?: throw IllegalStateException(msg)
        }
    }

    /**
     * Applies a block of instruction on the instance, if the instance is a mock. If this contract
     * currently uses a concrete implementation instance, the block won't be used.
     *
     * @param block the block to apply
     */
    fun applyIfMock(block: (T) -> Unit) {
        if (isInstanceMock) {
            val currentInstance = instance
            if (currentInstance != null) {
                block(currentInstance)
            }
        }
    }

    /**
     * Applies a block of instruction on the instance, if the instance is a concrete implementation.
     * If this contract currently uses a mock instance, the block won't be used.
     *
     * @param block the block to apply
     */
    fun applyIfImplementation(block: (T) -> Unit) {
        if (!isInstanceMock) {
            val currentInstance = instance
            if (currentInstance != null) {
                block(currentInstance)
            }
        }
    }

    // endregion

    // region Abstract

    /**
     * @return the parameters to use as input for the given clause to test
     */
    abstract fun generateClauseContexts(): Iterable<ContractClauseContext>

    // endregion

    // region Internal

    private fun checkNoInstance() {
        if (instance != null) {
            if (isInstanceMock) {
                throw IllegalStateException("This contract is already using a mock instance")
            } else {
                throw IllegalStateException("This contract is already using a real instance")
            }
        }
    }

    // endregion
}
