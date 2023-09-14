package com.example.orderleapp.fragment

import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.orderleapp.R
import com.example.orderleapp.api.CMSApi
import com.example.orderleapp.databinding.FragmentAboutUsBinding
import com.example.orderleapp.util.Config
import com.example.orderleapp.util.Pref


class AboutUsFragment : Fragment() {
    lateinit var binding: FragmentAboutUsBinding
    var page_id:Int = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAboutUsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        binding.relativeLayout.visibility = View.VISIBLE
        fetchCMSContent()
    }
    private fun fetchCMSContent() {
        val userId = Pref.getValue(requireContext(), Config.PREF_USERID, "").toString()
        val code = Pref.getValue(requireContext(), Config.PREF_CODE, "")
        val loginAccessToken = Pref.getValue(requireContext(), Config.PREF_LOGIN_ACCESS_TOKEN, "")
        val pageId = page_id

        val cmsApi = CMSApi(
            context = requireContext(),
            onSuccess = { cmsApiResponse ->
               binding.textView.text =Html.fromHtml(cmsApiResponse[0].pageContent.trim())
                binding.relativeLayout.visibility = View.GONE
            },
        )
        cmsApi.getCMSData(userId, code, loginAccessToken, pageId)
    }
    override fun setArguments(args: Bundle?) {
        super.setArguments(args)
        if (args!!.containsKey("page_id")) {
            page_id = args.getInt("page_id")
        }
    }

}