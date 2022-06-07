package com.example.helsinkimap.presentation.arch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.HasDefaultViewModelProviderFactory
import androidx.viewbinding.ViewBinding
import com.example.helsinkimap.presentation.arch.common.NestedInflater
import com.example.helsinkimap.presentation.arch.common.terminalInflater
import com.example.helsinkimap.presentation.arch.delegate.LifecycleDelegate
import com.example.helsinkimap.presentation.arch.delegate.StateLifecycleDelegate
import com.example.helsinkimap.presentation.arch.di.BindingFactory
import com.example.helsinkimap.presentation.arch.viewmodel.MvvmViewModel
import com.example.helsinkimap.presentation.common.delegate.ViewBindingDelegate

abstract class BaseMvvmFragment() : Fragment(), HasDefaultViewModelProviderFactory, NestedInflater {

    /**
     * Constructor for cases where you don't need view binding for view interaction
     */
    constructor(bindingFactory: BindingFactory<*>) : this() {
        viewBindingDelegate.createPrimaryViewBindingHandler(bindingFactory)
    }

    /* LIFECYCLE */
    private val lifecycleDelegates: List<LifecycleDelegate> by lazy {
        mutableListOf<LifecycleDelegate>()
            .also { provideLifecycleDelegates(it) }
    }

    private val viewBindingDelegate by lazy { ViewBindingDelegate() }

    private val lifecycleState by lazy { StateLifecycleDelegate() }

    fun <Binding : ViewBinding> viewBinding(factory: BindingFactory<Binding>) =
        viewBindingDelegate.createPrimaryViewBindingHandler(factory)

    fun <Binding : ViewBinding> viewBinding(@IdRes viewId: Int, binder: (View) -> Binding) =
        viewBindingDelegate.createSecondaryViewBindingHandler {
            binder(it.findViewById(viewId))
        }

    @CallSuper
    protected open fun provideLifecycleDelegates(delegates: MutableList<in LifecycleDelegate>) {
        delegates.addAll(0, listOf(lifecycleState, viewBindingDelegate))
    }

    /* MVVM */
    protected abstract val viewModel: MvvmViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleDelegates.forEach { it.onCreate(savedInstanceState) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        view: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflateNested(
            view,
            viewBindingDelegate.terminalInflater(inflater),
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveData()
        lifecycleDelegates.forEach { it.onCreateView(view) }
    }

    override fun onStart() {
        super.onStart()
        lifecycleDelegates.forEach { it.onStart() }
    }

    override fun onResume() {
        super.onResume()
        viewModel.attach()
        lifecycleDelegates.forEach { it.onResume() }
    }

    override fun onPause() {
        super.onPause()
        lifecycleDelegates.asReversed().forEach { it.onPause() }
        viewModel.detach()
    }

    override fun onStop() {
        super.onStop()
        lifecycleDelegates.asReversed().forEach { it.onStop() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        lifecycleDelegates.asReversed().forEach { it.onDestroyView() }
    }

    override fun onDetach() {
        super.onDetach()
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycleDelegates.asReversed().forEach { it.onDestroy() }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        lifecycleDelegates.asReversed().forEach { it.onSaveInstanceState(outState) }
    }

    protected open fun observeLiveData() {}
}
