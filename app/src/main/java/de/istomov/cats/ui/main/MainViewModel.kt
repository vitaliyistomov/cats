package de.istomov.cats.ui.main

import androidx.lifecycle.ViewModel
import de.istomov.cats.doOnIoObserveOnMain
import de.istomov.cats.domain.CatsRepository
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.BehaviorSubject
import timber.log.Timber

interface MainViewModelInputs {
    fun onMainButtonClicked()
    fun onImageLoadingError(message: String)
}

interface MainViewModelOutputs {
    val state: Observable<CatState>
}

class MainViewModel(
    private val catsRepository: CatsRepository
) : ViewModel(), MainViewModelInputs, MainViewModelOutputs {

    val input: MainViewModelInputs = this
    val outputs: MainViewModelOutputs = this

    private val catState = BehaviorSubject.createDefault<CatState>(CatStateEmpty)
    private val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

    override fun onMainButtonClicked() {
        catsRepository.catUrl()
            .doOnIoObserveOnMain()
            .doOnSubscribe { catState.onNext(CatStateLoading) }
            .subscribeBy(
                onNext = { catState.onNext(CatStateImage(url = it)) },
                onError = {
                    Timber.e(it, "Failed to get cats")
                    catState.onNext(CatStateError(error = it.message ?: ""))
                }
            )
            .addTo(compositeDisposable)
    }

    override fun onImageLoadingError(message: String) {
        catState.onNext(CatStateError(error = message))
    }

    override val state: Observable<CatState>
        get() {
            return catState.doOnIoObserveOnMain()
                .hide()
        }
}

sealed class CatState
object CatStateEmpty : CatState()
object CatStateLoading : CatState()
data class CatStateError(val error: String) : CatState()
data class CatStateImage(val url: String) : CatState()
