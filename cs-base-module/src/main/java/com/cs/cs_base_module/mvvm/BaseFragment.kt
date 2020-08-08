package com.cs.cs_base_module.mvvm

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cs.cs_base_module.event.NULLEvent
import com.trello.rxlifecycle4.components.support.RxFragment
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.lang.reflect.ParameterizedType

abstract class BaseFragment<V : ViewDataBinding, VM : BaseViewModel> : RxFragment(),IHost {
    val TARGET_REQUEST = 0X323
    var TAG = this.javaClass.getName()

    val CANCEL_BG = -1
    protected var binding: V? = null
    protected lateinit var viewModel: VM
    private var viewModelId: Int = 0

    protected var bIsViewCreated: Boolean = false
    protected var bIsDataLoaded: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            getContentView(inflater, container, savedInstanceState),
            container,
            false
        )
        binding?.getRoot()?.isClickable = true
        setStatusBar()
        if (setBackgroundResource() != -1) {
            binding?.getRoot()?.setBackgroundResource(setBackgroundResource())
        }
        return binding?.getRoot()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bIsViewCreated = true
        //初始化ViewModel dataBinding
        initViewDataBinding()
        //注册和viewModel的契约回调事件
        registerUIChangeLiveDataCallback()

        //页面事件监听的方法，一般用于ViewModel层转到View层的事件注册
        initViewObservable()
        initParams()
        //初始化数据
        initData()

        if (userVisibleHint && !bIsDataLoaded) {
            loadData()
            bIsDataLoaded = true
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && bIsViewCreated && !bIsDataLoaded) {
            loadData()
            bIsDataLoaded = true
        }
    }

    /**
     * 这个方法，属于延迟加载，只有在Fragment真正显示的时候才会调用
     */
    open fun loadData() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        //解除ViewModel生命周期感应
        lifecycle.removeObserver(viewModel!!)
        if (binding != null) {
            binding?.unbind()
        }
        bIsViewCreated = false
        bIsDataLoaded = false
    }

    fun initViewDataBinding() {
        viewModel = getCreateViewModel()
        viewModelId = getViewModelId()
        if (viewModel == null) {
            val modelClass: Class<*>
            val type = javaClass.getGenericSuperclass()
            if (type is ParameterizedType) {
                modelClass = (type as ParameterizedType).actualTypeArguments[1] as Class<BaseViewModel>
            } else {
                //如果没有指定泛型参数，则默认使用BaseViewModel
                modelClass = BaseViewModel::class.java
            }
            viewModel = createViewModel(this, modelClass) as VM
        }
        binding?.setVariable(viewModelId, viewModel)
        //viewModel拥有生命周期
        lifecycle.addObserver(viewModel!!)
        //
        viewModel?.setLifecycleObserver(lifecycle)
        //viewModel注入RxLifecycle
        viewModel?.injectLifecycle(this)
    }

    fun registerUIChangeLiveDataCallback() {
        //finish 关闭页面
        viewModel?.getUi()?.getFinishEvent()?.observe(this, Observer{
            if (isFinish()) {
                return@Observer
            }
            activity?.finish()
        })
        //onBackPressed 关闭页面
        viewModel?.getUi()?.getOnBackPressedEvent()?.observe(this, Observer {
                if (isFinish()) {
                    return@Observer
                }
                dismissSelf()
        })
        //startActivity
        viewModel?.getUi()?.getStartActivityEvent()?.observe(this, Observer{ params ->
            val cls = params.get(BaseViewModel.ParameterField.CLASS) as Class<*>
            val bundle = params.get(BaseViewModel.ParameterField.BUNDLE) as Bundle
            startActivity(cls, bundle)
        })
        viewModel?.getUi()?.getStartActivityForResultEvent()?.observe(this, Observer{ params ->
            val cls = params.get(BaseViewModel.ParameterField.CLASS) as Class<*>
            val bundle = params.get(BaseViewModel.ParameterField.BUNDLE) as Bundle
            val requestCode = params.get(BaseViewModel.ParameterField.REQUESTCODE) as Int
            startActivityForResult(cls, bundle, requestCode)
        })
        //显示loading dialog
        viewModel?.getUi()?.getshowLoadingEvent()
            ?.observe(this, Observer{ s -> getActivityWithinHost().showLoadingDialog() })
        //hide dialog
        viewModel?.getUi()?.getHideLoadingEvent()
            ?.observe(this, Observer{ aVoid -> getActivityWithinHost().hideDialog() })
//        mViewModel.getUi().getAddFragment().observe(
//            this,
//            { baseFragment ->
//                FragmentController.addFragment(
//                    getFragmentManagerWithinHost(),
//                    baseFragment,
//                    baseFragment.TAG
//                )
//            })
//        mViewModel.getUi().getReplaceFragment().observe(
//            this,
//            { baseFragment ->
//                FragmentController.replaceFragment(
//                    getFragmentManagerWithinHost(),
//                    baseFragment,
//                    baseFragment.TAG
//                )
//            })
    }


    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    override fun initViewObservable() {

    }

    /**
     * 有轮询任务的时候复写
     */
    fun refreshByPoll() {

    }

    /**
     * 跳转页面
     *
     * @param clz 所跳转的目的Activity类
     */
    override fun startActivity(clz: Class<*>) {
        startActivity(Intent(activity, clz))
    }

    /**
     * 跳转页面
     *
     * @param clz    所跳转的目的Activity类
     * @param bundle 跳转所携带的信息
     */
    override fun startActivity(clz: Class<*>, bundle: Bundle?) {
        val intent = Intent(activity, clz)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivity(intent)
    }

    /**
     * 打开Activity for result
     *
     * @param cls
     * @param bundle
     */
    override fun startActivityForResult(cls: Class<*>, bundle: Bundle?, requestCode: Int) {
        val intent = Intent(activity, cls)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivityForResult(intent, requestCode)
    }

    /**
     * 判断Fragment attach的activity是否已经onDestroy销毁
     */
    fun isFinish(): Boolean {
        return if (activity == null || this!!.activity!!.isDestroyed()) {
            true
        } else false
    }

    fun onKeyBack(): Boolean {
        return false
    }


    override fun getFragmentManagerWithinHost(): FragmentManager {
        return this!!.fragmentManager!!
    }


    override fun getStringWithinHost(@StringRes resId: Int): String {
        return getString(resId)
    }

    override fun getResourcesWithinHost(): Resources {
        return resources
    }

    override fun getActivityWithinHost(): BaseActivity<*,*> {
        return activity as BaseActivity<*,*>
    }

    override fun getContextWithinHost(): Context {
        return this!!.requireActivity()
    }

    fun callTarget() {
        val targetFragment = targetFragment
        if (targetFragment != null && targetRequestCode !== -1) {
            targetFragment.onActivityResult(targetRequestCode, Activity.RESULT_OK, null)
        }
    }

    fun dismissSelf() {
        callTarget()
//        FragmentController.removeFragment(fragmentManager, this)
    }

    /**
     * 判断Fragment是否存活
     *
     * @return boolean
     */
    fun isFragmentAlive(): Boolean {
        return isAdded && !isDetached && !isRemoving && getFragmentManagerWithinHost() != null && !getFragmentManagerWithinHost()!!.isDestroyed
    }

    override fun isAlive(): Boolean {
        return isFragmentAlive()
    }

    override fun initData() {

    }

    override fun initParams() {

    }

    /**
     * 状态栏
     */
    protected fun setStatusBar() {}

    @Subscribe
    fun onMainEvent(event: NULLEvent) {
        //空方法，不能删除，适配EventBus
    }

    fun setBackgroundResource(): Int {
        return -1
    }
    /**
     * 初始化根布局
     *
     * @return 布局layout的id
     */
    abstract fun getContentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): Int

    /**
     * 初始化ViewModel的id
     *
     * @return BR的id
     */
    abstract fun getViewModelId(): Int

    /**
     * 初始化ViewModel
     *
     * @return 继承BaseViewModel的ViewModel
     */
    abstract fun getCreateViewModel(): VM

    //刷新布局
    fun refreshLayout() {
        if (viewModel != null) {
            binding?.setVariable(viewModelId, viewModel)
        }
    }

    /**
     * 创建ViewModel
     *
     * @param cls
     * @param <T>
     * @return
    </T> */
    fun <T : BaseViewModel> createViewModel(fragment: BaseFragment<*,*>, cls: Class<T>): T {
        return ViewModelProvider(fragment).get(cls)
    }
}