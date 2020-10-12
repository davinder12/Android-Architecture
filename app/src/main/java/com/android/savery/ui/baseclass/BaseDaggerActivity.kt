package com.android.savery.ui.baseclass

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.MainThread
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.observe
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.android.savery.di.ViewModelFactory
import com.android.savery.ui.util.ResourceViewModel
import com.sdi.joyersmajorplatform.uiview.NetworkState
import com.sdi.joyersmajorplatform.uiview.recyclerview.DataBoundAdapterClass
import com.sdi.joyersmajorplatform.uiview.recyclerview.DataBoundPagedListAdapter
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import ywaste.com.ywaste.ui.util.PagedListViewModel
import javax.inject.Inject


abstract class BaseDaggerActivity : DaggerAppCompatActivity() {


//    protected fun requireVidePlayer() = requireBoolean(NavigationScreen.EXTRA_VIDEO_PLAYER)
//    protected fun requireReferralVisit() = requireBoolean(NavigationScreen.IS_FIRST_TIME_VISIT)
//    protected fun requireSocialLogin() = requireBoolean(NavigationScreen.IS_SOCIAL_LOGIN)
//    protected fun requireIsPaymentComplete() = requireBoolean(NavigationScreen.PAYMENT_COMPLETE)
//    protected fun requireUserId() = requireString(NavigationScreen.EXTRA_USER_ID)
//    protected fun requireResturantId() = requireString(NavigationScreen.EXTRA_RESTURANT_ID)
//    protected fun requirePaymentRequest() = requireObject(NavigationScreen.EXTRA_PAYMENT)
//    protected fun requireSponsorPaymentRequest() = requireSponsorObject(NavigationScreen.EXTRA_SPONSOR_PAYMENT)


//    protected fun requireActiveFoodReceipt() =
//        requireList(NavigationScreen.EXTRA_ACTIVE_RECEIPT_DETAIL)


    /**
     * [ViewModelFactory] which uses Dagger2 for dependency injection
     */
    @Inject
    lateinit var viewModelFactory: ViewModelFactory






    /*This is for viewPagerAdapter Adapter*/

    fun <X : DataBoundAdapterClass<T, *>, T> X.setup(
        viewPager: ViewPager2,
        data: List<T>
    ) {
        viewPager.adapter = this
        submitList(data)
    }


    protected fun <X : DataBoundPagedListAdapter<T, *>, T> initAdapter(
        adapter: X,
        recyclerView: RecyclerView,
        viewModel: PagedListViewModel<T>,
        hasFixedSize: Boolean = true,
        clickHandler: ((T) -> Unit)? = null
    ): X {
        recyclerView.setHasFixedSize(hasFixedSize)
        recyclerView.adapter = adapter
        subscriptions += adapter.retryClicks.subscribe(viewModel::retry)
        clickHandler?.let { subscribe(adapter.clicks, it) }
        viewModel.items.observe(
            this,
            Observer { adapter.submitList(it) })
        viewModel.frontLoadingState.observe(this, adapter::setNetworkState)
        viewModel.endLoadingState.observe(this, adapter::setNetworkState)
        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                if (positionStart == 0) recyclerView.scrollToPosition(0)
            }
        })
        return adapter
    }


    protected fun <X : DataBoundAdapterClass<T, *>, T> initAdapter(
        adapter: X,
        recycler: RecyclerView,
        list: LiveData<List<T>?>,
        clickHandler: ((T) -> Unit)? = null
    ): X {
        recycler.adapter = adapter
        list.observe(this, Observer {
            adapter.submitList(it)
        })
        clickHandler?.let { subscribe(adapter.clicks, it) }
        return adapter
    }




    // TODO Need to refactor retryClick  funtionality
    @SuppressLint("CheckResult")
    protected fun <X : DataBoundAdapterClass<T, *>, T, R> initAdapter(
        adapter: X,
        recycler: RecyclerView,
        viewModel: ResourceViewModel<R>,
        list: LiveData<List<T>?>,
        clickHandler: ((T) -> Unit)? = null
    ): X {
        recycler.adapter = adapter
        list.observe(this, adapter::submitList)
        adapter.retryClicks.subscribe(viewModel::retry)
        viewModel.networkState.observe(this, adapter::setNetworkState)
        clickHandler?.let { subscribe(adapter.clicks, it) }
        return adapter
    }


    fun <X : DataBoundAdapterClass<T, *>, T> initAdapter(
        adapter: X,
        viewPager: ViewPager2,
        data: LiveData<List<T>?>,
        clickHandler: ((T) -> Unit)? = null
    ): X {
        viewPager.adapter = adapter
        data.observe(this, adapter::submitList)
        clickHandler?.let { subscribe(adapter.clicks, it) }
        return adapter
    }




    /**
     * Wrapper for [ComponentActivity.viewModels]
     * Uses the dagger viewModelFactory by default to avoid having to specify it each time.
     */
    @MainThread
    inline fun <reified VM : ViewModel> viewModels() = viewModels<VM>
    { viewModelFactory }


    /**
     * Subscribes to a [Observable] and handles disposing.
     */
    fun <T> subscribe(stream: Observable<T>?, handler: (T) -> Unit) {
        if (stream == null) return
        subscriptions += stream.subscribe(handler) {
            //  Timber.e(it)
        }
    }

    /**
     * Container for RxJava subscriptions.
     */
    private val subscriptions = CompositeDisposable()


    fun showMessage(message: String?) {
        message?.let {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }
    }
//
//    fun showSnackMessage(message: String?) {
//        message?.let {
//            val snackBar = Snackbar.make(find(android.R.id.content), it, Snackbar.LENGTH_SHORT)
//            snackBar.show()
//        }
//    }
//
//
//    fun locationSnackMessage() {
//        Snackbar.make(find(android.R.id.content), R.string.gpsvalidation, Snackbar.LENGTH_LONG)
//            .setAction(R.string.setting) {
//                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
//                startActivity(intent)
//            }.show()
//    }

    protected fun bindNetworkState(
        networkState: LiveData<NetworkState>,
        dialog: AlertDialog? = null,
        @StringRes success: Int? = null,
        @StringRes error: Int? = null,
        loadingIndicator: View? = null,
        onError: (() -> Unit)? = null,
        onSuccess: (() -> Unit)? = null
    ) {
        networkState.observe(this, Observer {
            when (it.status) {
                NetworkState.Status.RUNNING -> {
                    loadingIndicator?.visibility = View.VISIBLE
                    dialog?.show()
                }

                NetworkState.Status.FAILED -> {
                    showMessage(it.msg)
                    loadingIndicator?.visibility = View.GONE
                    dialog?.dismiss()
                    onError?.invoke()

                }
                NetworkState.Status.SUCCESS -> {
                    success?.let { showMessage(resources.getString(success)) }
                    loadingIndicator?.visibility = View.GONE
                    dialog?.dismiss()
                    onSuccess?.invoke()
                }
            }
        })
    }

    private fun requireBoolean(tag: String): Boolean {
        return intent.getBooleanExtra(tag, false)
    }
//
//    private fun requireList(tag: String): SaveReceiptResponse.Body? {
//        var activeReceiptRespose: SaveReceiptResponse.Body? = null
//        intent.getParcelableExtra<SaveReceiptResponse.Body>(tag)?.let {
//            activeReceiptRespose = it
//        }
//        return activeReceiptRespose
//    }
//
//    private fun requireObject(tag: String): SaveReceiptRequestModel? {
//        var activeReceiptRespose: SaveReceiptRequestModel? = null
//        intent.getParcelableExtra<SaveReceiptRequestModel>(tag)?.let {
//            activeReceiptRespose = it
//        }
//        return activeReceiptRespose
//    }
//
//
//    private fun requireSponsorObject(tag: String): SponsorSaveReceiptRequest? {
//        var activeReceiptRespose: SponsorSaveReceiptRequest? = null
//        intent.getParcelableExtra<SponsorSaveReceiptRequest>(tag)?.let {
//            activeReceiptRespose = it
//        }
//        return activeReceiptRespose
//    }
//


    private fun requireString(tag: String): String {
        var field = ""
        intent.getStringExtra(tag)?.let {
            field = it
        }
        return field
    }


}
