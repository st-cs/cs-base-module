package com.cs.cs_base_module.mvvm

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cs.cs_base_module.R
import com.cs.cs_base_module.view.LoadingDialog
import com.trello.rxlifecycle4.components.support.RxAppCompatActivity
import java.lang.reflect.ParameterizedType

abstract class BaseActivity<V : ViewDataBinding, VM : BaseViewModel> : RxAppCompatActivity(),IHost {
    protected var binding: V? = null
    protected lateinit var viewModel: VM
    private var viewModelId: Int = 0
    private var isTransitionsAnim = true
    var root: ViewGroup? = null
    private var loadingDialog: LoadingDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//初始化databinding和viewModel
        initViewDataBinding(savedInstanceState)
        //注册viewModel和view的契约事件回调逻辑
        registerUIChangeLiveDateCallBack()

        //页面事件监听的方法，一般用于ViewModel层转到View层的事件注册
        initViewObservable()
        setStatusBar()
        root = findViewById(android.R.id.content)
        var backgroundResource = getBackgroundResource()
        if(backgroundResource != 0){
            root?.setBackgroundResource(backgroundResource)
        }
        initParams()
        initData()
    }

    private fun initViewDataBinding(savedInstanceState: Bundle?) {
        binding = DataBindingUtil.setContentView(this, getContentViewLayout(savedInstanceState))
        viewModelId = getViewModelId()
        viewModel = initViewModel()
        if (viewModel == null) {
            val modelClass: Class<*>
            val type = javaClass.genericSuperclass
            if (type is ParameterizedType) {
                modelClass = (type as ParameterizedType).actualTypeArguments[1] as Class<BaseViewModel>
            } else {
                //如果没有指定泛型参数，则默认使用BaseViewModel
                modelClass = BaseViewModel::class.java
            }
            viewModel = createViewModel(this, modelClass) as VM
        }
        //关联ViewModel
        binding?.setVariable(viewModelId, viewModel)
        if(viewModel != null){
            //注册ViewModel的生命周期
            lifecycle.addObserver(viewModel!!)
            //注册
            viewModel?.setLifecycleObserver(lifecycle)
            //注入RxLifecycle生命周期
            viewModel?.injectLifecycle(this)
        }
    }

    fun registerUIChangeLiveDateCallBack() {
        //finish 关闭页面
        viewModel?.getUi()?.getFinishEvent()?.observe(this, Observer { finish() })
        //onBackPressed 关闭页面
        viewModel?.getUi()?.getOnBackPressedEvent()?.observe(this, Observer { onBackPressed() })
        //startActivity
        viewModel?.getUi()?.getStartActivityEvent()?.observe(this, Observer {
            val cls = it.get(BaseViewModel.ParameterField.CLASS) as Class<*>
            val bundle = it.get(BaseViewModel.ParameterField.BUNDLE) as? Bundle
            startActivity(cls, bundle)
        })
        viewModel?.getUi()?.getStartActivityForResultEvent()?.observe(this, Observer {
            val cls = it.get(BaseViewModel.ParameterField.CLASS) as Class<*>
            val bundle = it.get(BaseViewModel.ParameterField.BUNDLE) as? Bundle
            val requestCode = it.get(BaseViewModel.ParameterField.REQUESTCODE) as? Int?:0
            startActivityForResult(cls, bundle, requestCode)
        })
        //显示loading dialog
        viewModel?.getUi()?.getshowLoadingEvent()?.observe(this, Observer { showLoadingDialog() })
        //hide dialog
        viewModel?.getUi()?.getHideLoadingEvent()?.observe(this, Observer{ hideDialog() })
//        viewModel?.getUi()?.getAddFragment()?.observe(
//            this, Observer { FragmentController.addFragment(this@VMBaseActivity, baseFragment) })
//        viewModel.getUi().getReplaceFragment().observe(this, { baseFragment ->
//            FragmentController.replaceFragment(
//                supportFragmentManager, baseFragment, baseFragment.TAG
//            )
//        })
    }

    override fun getFragmentManagerWithinHost(): FragmentManager {
        return supportFragmentManager
    }


    override fun getStringWithinHost(@StringRes resId: Int): String {
        return getString(resId)
    }

    override fun getResourcesWithinHost(): Resources {
        return resources
    }


    override fun getActivityWithinHost(): BaseActivity<*,*> {
        return this
    }

    override fun getContextWithinHost(): Context {
        return this
    }

    override fun isDestroyed(): Boolean {
        return super.isDestroyed() || isFinishing
    }

    override fun isAlive(): Boolean {
        return !isDestroyed
    }

    override fun initParams() {

    }

    /**
     * 获取页面的布局文件
     *
     * @param savedInstanceState
     * @return 布局的id
     */
    abstract fun getContentViewLayout(savedInstanceState: Bundle?): Int

    /**
     * 获取ViewModel的id
     *
     * @return 变量的id
     */
    abstract fun getViewModelId(): Int

    /**
     * 初始化ViewModel
     *
     * @return
     */
    abstract fun initViewModel(): VM

    /**
     * 创建ViewModel
     *
     * @param cls
     * @param <T>
     * @return
    </T> */
    fun <T : BaseViewModel> createViewModel(activity: FragmentActivity, cls: Class<T>): T {
        return ViewModelProvider(activity).get(cls)
    }

    /**
     * 状态栏
     */
    protected fun setStatusBar() {

    }

    /**
     * 打开Activity
     *
     * @param cls
     */
    override fun startActivity(cls: Class<*>) {
        startActivity(cls, null)
    }

    /**
     * 打开Activity
     *
     * @param cls
     * @param bundle
     */
    override fun startActivity(cls: Class<*>, bundle: Bundle?) {
        val intent = Intent(this, cls)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivity(intent)
        //动画右边进入，左边出去
//        if (isTransitionsAnim)
//            overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out)
    }

    /**
     * 打开Activity for result
     *
     * @param cls
     * @param bundle
     */
    override fun startActivityForResult(cls: Class<*>, bundle: Bundle?, requestCode: Int) {
        val intent = Intent(this, cls)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivityForResult(intent, requestCode)
//        if (isTransitionsAnim)
//            overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out)
    }

    fun showLoadingDialog() {
        if (loadingDialog == null) {
            loadingDialog = LoadingDialog(this)
        }
        if (isFinishing) {
            return
        }
        loadingDialog?.show()
    }



    fun hideDialog() {
        if (loadingDialog == null) {
            return
        }
        root?.postDelayed({ loadingDialog?.dismiss() }, 200)
    }

    fun getBackgroundResource():Int{
        return 0
    }
}