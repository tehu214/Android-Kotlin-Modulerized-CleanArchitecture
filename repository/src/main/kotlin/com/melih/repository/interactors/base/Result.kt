package com.melih.repository.interactors.base


/**
 * Result class that wraps any [Success], [Failure] or [State] that can be generated by any derivation of [BaseInteractor]
 */
sealed class Result<out T> {

    // region Subclasses

    class Success<out T>(val successData: T) : Result<T>()
    class Failure(val errorData: Reason) : Result<Nothing>()

    sealed class State : Result<Nothing>() {
        class Loading : State()
        class Loaded : State()
    }
    // endregion

    // region Functions

    inline fun handle(stateBlock: (State) -> Unit, failureBlock: (Reason) -> Unit, successBlock: (T) -> Unit) {
        when (this) {
            is Success -> successBlock(successData)
            is Failure -> failureBlock(errorData)
            is State -> stateBlock(this)
        }
    }

    inline fun handleSuccess(successBlock: (T) -> Unit) {
        if (this is Success)
            successBlock(successData)
    }

    inline fun handleFailure(errorBlock: (Reason) -> Unit) {
        if (this is Failure)
            errorBlock(errorData)
    }

    inline fun handleState(stateBlock: (State) -> Unit) {
        if (this is State)
            stateBlock(this)
    }
    // endregion
}
