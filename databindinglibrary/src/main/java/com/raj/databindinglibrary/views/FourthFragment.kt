package com.raj.databindinglibrary.views


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.raj.databindinglibrary.viewmodels.DataViewModel
import com.raj.databindinglibrary.R
import com.raj.databindinglibrary.databinding.FragmentFourBinding


class FourthFragment : Fragment() {

    val dataViewModel by lazy {
        ViewModelProvider(this).get(DataViewModel::class.java)
    }
    val options = navOptions {
        anim {
            enter = R.anim.slide_in_right
            exit = R.anim.slide_out_left
            popEnter = R.anim.slide_in_left
            popExit = R.anim.slide_out_right
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = FragmentFourBinding.inflate(inflater)
        rootView.lifecycleOwner = this

        rootView.viewModelData = dataViewModel
        return rootView.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        btn_forthFragment.setOnClickListener(
//            Navigation.createNavigateOnClickListener(R.id.firstFragment)
//        )
        //Navigation with Transition effects using options
        view.findViewById<Button>(R.id.btn_forthFragment).setOnClickListener {
            findNavController().navigate(R.id.secondFragment,null,options)
        }
    }
}