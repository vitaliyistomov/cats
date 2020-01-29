package de.istomov.cats.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import de.istomov.cats.R
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.main_fragment.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel: MainViewModel by viewModel()
    private val picasso: Picasso by inject()
    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.outputs.state
            .subscribe { renderState(it) }
            .addTo(compositeDisposable)

        main_button.setOnClickListener { viewModel.input.onMainButtonClicked() }
    }

    private fun renderState(state: CatState) {
        when (state) {
            is CatStateEmpty -> renderEmpty()
            is CatStateLoading -> renderLoading()
            is CatStateError -> renderError(state.error)
            is CatStateImage -> renderImage(state.url)
        }
    }

    private fun renderEmpty() {
        main_empty.isVisible = true
        main_image.isVisible = false
        main_progress.isVisible = false
    }

    private fun renderLoading() {
        main_empty.isVisible = false
        main_image.isVisible = false
        main_progress.isVisible = true
    }

    private fun renderError(error: String) {
        Snackbar.make(main_container, error, Snackbar.LENGTH_SHORT).show()
    }

    private fun renderImage(url: String) {
        main_empty.isVisible = false
        picasso.load(url).into(main_image, object : Callback {
            override fun onSuccess() {
                main_image.isVisible = true
                main_progress.isVisible = false
            }

            override fun onError(e: Exception?) {
                Timber.e(e, "Failed to load the image")
                viewModel.input.onImageLoadingError(e?.message ?: "Unknown error loading an awesome cat picture")
            }
        })
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }
}
