package com.clover.celebrities.fragments


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.clover.celebrities.R
import kotlinx.android.synthetic.main.fragment_celebrity_info.*

class CelebrityInfoFragment : Fragment() {

    val LOG = "CelebrityInfoFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_celebrity_info, container, false)
    }
    

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureWebView()
        progressBar.visibility = View.VISIBLE
        arguments?.let {
            val celebrityUrl = CelebrityInfoFragmentArgs.fromBundle(it).celebrityUrl
            webView.loadUrl(celebrityUrl)
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun configureWebView() {
        webView.webViewClient = object: WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                progressBar?.visibility = View.GONE
            }
        }
        webView.settings.javaScriptEnabled = true
    }


}
