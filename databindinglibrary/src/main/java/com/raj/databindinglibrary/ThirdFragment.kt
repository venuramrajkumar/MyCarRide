package com.raj.databindinglibrary


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.navOptions
import com.raj.databindinglibrary.databinding.FragmentFourBinding
import com.raj.databindinglibrary.databinding.FragmentFirstBinding
import com.raj.databindinglibrary.databinding.FragmentThirdBinding
import kotlinx.android.synthetic.main.fragment_third.*

class ThirdFragment : Fragment() {

    val dataViewModel by lazy {
        ViewModelProvider(this).get(DataViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = FragmentThirdBinding.inflate(inflater)
        rootView.lifecycleOwner = activity

        rootView.viewModelData = dataViewModel
        return rootView.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        plain_name.text =  arguments?.getString("myarg")
        btn_thirdFragment.setOnClickListener (Navigation.createNavigateOnClickListener(R.id.dataBindingFragment))
    }
}