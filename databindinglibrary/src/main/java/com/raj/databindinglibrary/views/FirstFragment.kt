package com.raj.databindinglibrary.views


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.raj.databindinglibrary.R
import com.raj.databindinglibrary.viewmodels.DataViewModel
import com.raj.databindinglibrary.databinding.FragmentFirstBinding


class FirstFragment : Fragment() {

    val dataViewModel by lazy {
        ViewModelProvider(this).get(DataViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = FragmentFirstBinding.inflate(inflater)

        rootView.lifecycleOwner = activity

        rootView.viewModelData = dataViewModel
        setHasOptionsMenu(true)

        return rootView.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        TO navigate to the destination defined in graph
        view.findViewById<Button>(R.id.plain_lastname).setOnClickListener {

            val action = FirstFragmentDirections.actionFirstFragmentToSecondFragment()
            findNavController().navigate(action)
        }


        //This is to call required fragment with overriden data
//        val bundle = bundleOf("MyIntData" to 10)
//        plain_lastname.setOnClickListener (
//        //          Navigation.createNavigateOnClickListener(R.id.secondFragment,null)
//                    Navigation.createNavigateOnClickListener(R.id.secondFragment,bundle)
//        )
    }
}