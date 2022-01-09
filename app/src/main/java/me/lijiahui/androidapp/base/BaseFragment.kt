package me.lijiahui.androidapp.base

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

private const val TAG = "BaseFragment"

open class BaseFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate ${javaClass.simpleName}")
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView ${javaClass.simpleName}")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreated ${javaClass.simpleName}")
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        Log.d(TAG, "onStart ${javaClass.simpleName}")
        super.onStart()
    }

    override fun onResume() {
        Log.d(TAG, "onResume ${javaClass.simpleName}")
        super.onResume()
    }

    override fun onPause() {
        Log.d(TAG, "onPause ${javaClass.simpleName}")
        super.onPause()
    }

    override fun onStop() {
        Log.d(TAG, "onStop ${javaClass.simpleName}")
        super.onStop()
    }

    override fun onDestroyView() {
        Log.d(TAG, "onDestroyView ${javaClass.simpleName}")
        super.onDestroyView()
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy ${javaClass.simpleName}")
        super.onDestroy()
    }

    override fun onAttach(context: Context) {
        Log.d(TAG, "onAttach ${javaClass.simpleName}")
        super.onAttach(context)
    }

    override fun onDetach() {
        Log.d(TAG, "onDetach ${javaClass.simpleName}")
        super.onDetach()
    }
}