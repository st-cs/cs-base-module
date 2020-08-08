package com.cs.cs_base_module.mvvm

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.cs.cs_base_module.event.SingleLiveEvent
import com.trello.rxlifecycle4.LifecycleProvider
import java.util.HashMap

open class BaseViewModel(application: Application) : AndroidViewModel(application),IBaseViewModel {
    var TAG = this.javaClass.name
    private var lifecycle: LifecycleProvider<*>? = null
    private var lifecycleObserver: Lifecycle? = null

    fun getLifecycle(): LifecycleProvider<*>? {
        return lifecycle
    }

    fun getLifecycleObserver(): Lifecycle? {
        return lifecycleObserver
    }

    fun setLifecycleObserver(lifecycleObserver: Lifecycle) {
        this.lifecycleObserver = lifecycleObserver
    }

    open fun injectLifecycle(lifecycle: LifecycleProvider<*>) {
        this.lifecycle = lifecycle
    }

    private var ui: UIChangeLiveData? = null

    fun getUi(): UIChangeLiveData {
        if (ui == null) {
            ui = UIChangeLiveData()
        }
        return ui!!
    }

    /**
     * 跳转页面
     *
     * @param cls 所跳转的目的Activity类
     */
    fun startActivity(cls: Class<*>) {
        startActivity(cls, null)
    }

    /**
     * 跳转页面
     *
     * @param cls    所跳转的目的Activity类
     * @param bundle 跳转所携带的信息
     */
    fun startActivity(cls: Class<*>, bundle: Bundle?) {
        val params = HashMap<String, Any>()
        params[ParameterField.CLASS] = cls
        if (bundle != null) {
            params[ParameterField.BUNDLE] = bundle
        }
        ui!!.getStartActivityEvent()!!.postValue(params)
    }

//    fun addFragment(baseFragment: BaseFragment<*,*>) {
//        ui!!.getAddFragment().setValue(baseFragment)
//    }
//
//    fun replaceFragment(baseFragment: BaseFragment<*,*>) {
//        ui!!.getReplaceFragment().setValue(baseFragment)
//    }

    /**
     * 跳转页面
     *
     * @param cls    所跳转的目的Activity类
     * @param bundle 跳转所携带的信息
     */
    fun startActivityForResult(cls: Class<*>, bundle: Bundle?, requestCode: Int) {
        val params = HashMap<String, Any>()
        params[ParameterField.CLASS] = cls
        if (bundle != null) {
            params[ParameterField.BUNDLE] = bundle
        }
        params[ParameterField.REQUESTCODE] = requestCode
        ui!!.getStartActivityEvent()!!.postValue(params)
    }

    open fun showLoading() {
        showLoading("")
    }

    fun showLoading(text: String) {
        ui?.getshowLoadingEvent()!!.setValue(text)
    }

    open fun hideLoading() {
        ui?.getHideLoadingEvent()!!.call()
    }

    /**
     * 关闭activity 无动画
     */
    fun finish() {
        ui!!.getFinishEvent()!!.call()
    }

    /**
     * 关闭activity 带动画
     */
    fun onBackPressed() {
        ui!!.getOnBackPressedEvent()!!.call()
    }

    override fun onAny(owner: LifecycleOwner, event: Lifecycle.Event) {

    }

    override fun onCreate() {

    }

    override fun onDestroy() {

    }

    override fun onStart() {

    }

    override fun onStop() {

    }

    override fun onResume() {

    }

    override fun onPause() {

    }

    /**
     * 有轮询任务的时候复写
     */
    fun refreshByPoll() {

    }

    fun getString(res: Int): String {
        return getApplication<Application>().resources.getString(res)
    }

    fun getColor(res: Int): Int {
        return getApplication<Application>().resources.getColor(res)
    }

    inner class UIChangeLiveData {
        private var finishEvent: SingleLiveEvent<Void>? = null
        private var onBackPressedEvent: SingleLiveEvent<Void>? = null
        private var showLoadingEvent: SingleLiveEvent<String>? = null
        private var hideLoadingEvent: SingleLiveEvent<Void>? = null
        private var startActivityEvent: SingleLiveEvent<Map<String, Any>>? = null
        private var startActivityForResultEvent: SingleLiveEvent<Map<String, Any>>? = null
        private var addFragment: SingleLiveEvent<BaseFragment<*,*>>? = null
        private var replaceFragment: SingleLiveEvent<BaseFragment<*,*>>? = null

        fun getFinishEvent(): SingleLiveEvent<Void> {
            finishEvent = createLiveData(this.finishEvent)
            return finishEvent!!
        }

        fun getOnBackPressedEvent(): SingleLiveEvent<Void> {
            onBackPressedEvent = createLiveData(onBackPressedEvent)
            return onBackPressedEvent!!
        }

        fun getStartActivityEvent(): SingleLiveEvent<Map<String, Any>> {
            startActivityEvent = createLiveData(startActivityEvent)
            return startActivityEvent!!
        }

        fun getStartActivityForResultEvent(): SingleLiveEvent<Map<String, Any>> {
            startActivityForResultEvent = createLiveData(startActivityForResultEvent)
            return startActivityForResultEvent!!
        }

        fun getshowLoadingEvent(): SingleLiveEvent<String> {
            showLoadingEvent = createLiveData(showLoadingEvent)
            return showLoadingEvent!!
        }

        fun getHideLoadingEvent(): SingleLiveEvent<Void> {
            hideLoadingEvent = createLiveData(hideLoadingEvent)
            return hideLoadingEvent!!
        }

        fun getAddFragment(): SingleLiveEvent<BaseFragment<*,*>> {
            addFragment = createLiveData(addFragment)
            return addFragment!!
        }

        fun getReplaceFragment(): SingleLiveEvent<BaseFragment<*,*>> {
            replaceFragment = createLiveData(replaceFragment)
            return replaceFragment!!
        }

        private fun<T> createLiveData(liveData: SingleLiveEvent<T>?): SingleLiveEvent<T> {
            var liveData = liveData
            if (liveData == null) {
                liveData = SingleLiveEvent()
            }
            return liveData
        }
    }

    object ParameterField {
        var CLASS = "CLASS"
        var BUNDLE = "BUNDLE"
        var REQUESTCODE = "REQUESTCODE"
    }

}