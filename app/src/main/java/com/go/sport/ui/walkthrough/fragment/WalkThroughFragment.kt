package com.go.sport.ui.walkthrough.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.go.sport.R
import kotlinx.android.synthetic.main.fragment_walk_through.view.*

/**
 * A simple [Fragment] subclass.
 */
class WalkThroughFragment : Fragment() {

    private var position = 0
    private lateinit var rootView: View

    companion object {
        fun getInstance(position: Int) = WalkThroughFragment().apply {
            arguments = Bundle().apply {
                putInt("position", position)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        arguments?.getInt("position")?.let {
            position = it
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_walk_through, container, false)

        initViews()

        return rootView
    }

    private fun initViews() {
        when (position) {
            0 -> {
                rootView.tv_title.text = "PLAY"
                rootView.iv.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.walk_through_1
                    )
                )
            }
            1 -> {
                rootView.tv_title.text = "BOOK"
                rootView.iv.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.walk_through_2
                    )
                )
            }
            2 -> {
                rootView.tv_title.text = "TRAIN"
                rootView.iv.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.walk_through_3
                    )
                )
            }
        }
    }
}