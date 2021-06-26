package com.raj.databindinglibrary.views


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.raj.databindinglibrary.viewmodels.DataViewModel
import com.raj.databindinglibrary.R
import com.raj.databindinglibrary.databinding.FragmentThirdBinding


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
//        view.findViewById<Button>(R.id.btn_forthFragment).setText(arguments?.getString("myarg"))

        view.findViewById<Button>(R.id.btn_thirdFragment).setOnClickListener (Navigation.createNavigateOnClickListener(R.id.dataBindingFragment))
    }
}