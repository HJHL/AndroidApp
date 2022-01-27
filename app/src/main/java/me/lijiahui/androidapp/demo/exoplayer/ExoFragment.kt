package me.lijiahui.androidapp.demo.exoplayer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import me.lijiahui.androidapp.databinding.FragmentExoBinding

class ExoFragment : Fragment() {

    private var _binding: FragmentExoBinding? = null
    private val mBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExoBinding.inflate(inflater, container, false)
        initView()
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initView() {
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}