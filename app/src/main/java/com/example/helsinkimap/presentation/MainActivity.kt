package com.example.helsinkimap.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.HasDefaultViewModelProviderFactory
import com.example.helsinkimap.R
import com.example.helsinkimap.domain.navigation.MainRouter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), HasDefaultViewModelProviderFactory {

    @Inject
    lateinit var viewModel: MainViewModel

    @Inject
    lateinit var mainRouter: MainRouter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        mainRouter.setNavigator(MainNavigator(this, R.id.main_container))
    }

    override fun onPause() {
        super.onPause()
        mainRouter.removeNavigator()
    }

    override fun onBackPressed() {
        viewModel.back()
    }
}
