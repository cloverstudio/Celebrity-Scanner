package com.clover.celebrities.fragments

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.clover.celebrities.R
import kotlinx.android.synthetic.main.activity_main.*


class SplashScreenFragment : Fragment() {

    val splashScreenDuration = 2000L //in ms

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.hide()
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash_screen, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar?.visibility = View.GONE
        Handler().postDelayed({
            val navController = Navigation.findNavController(activity!!, R.id.nav_host)
            navController.navigate(R.id.nextPage)
        }, splashScreenDuration)
    }

}
