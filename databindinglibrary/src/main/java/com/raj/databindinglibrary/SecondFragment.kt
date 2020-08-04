package com.raj.databindinglibrary


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.raj.databindinglibrary.databinding.FragmentSecondBinding
import kotlinx.android.synthetic.main.fragment_second.*

class SecondFragment : Fragment() {

    val dataViewModel by lazy {
        ViewModelProvider(this).get(DataViewModel::class.java)
    }

    val safeVarargs : FirstFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = FragmentSecondBinding.inflate(inflater)
        rootView.lifecycleOwner = activity

        rootView.viewModelData = dataViewModel



        return rootView.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        Toast.makeText(activity,"Bundle data from first fragment " +safeVarargs.MyIntData,Toast.LENGTH_SHORT).show()

        btn_secondFragment.setOnClickListener (
            Navigation.createNavigateOnClickListener(R.id.thirdFragment,null)
            )
    }
}