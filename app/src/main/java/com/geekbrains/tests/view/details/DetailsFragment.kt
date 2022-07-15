package com.geekbrains.tests.view.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.geekbrains.tests.R
import com.geekbrains.tests.databinding.FragmentDetailsBinding
import com.geekbrains.tests.presenter.details.DetailsPresenter
import com.geekbrains.tests.presenter.details.PresenterDetailsContract
import java.util.*

class DetailsFragment : Fragment(), ViewDetailsContract {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private val presenter: PresenterDetailsContract = DetailsPresenter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUI()
    }

    private fun setUI() {
        val count = this.arguments?.getInt(DetailsActivity.TOTAL_COUNT_EXTRA, 0)
        presenter.setCounter(count ?: 0)
        setCountText(count ?: 0)
        binding.decrementButton.setOnClickListener { presenter.onDecrement() }
        binding.incrementButton.setOnClickListener { presenter.onIncrement() }
    }

    companion object {
        fun newInstance(totalCount: Int): DetailsFragment {
            val fragment = DetailsFragment()
            fragment.arguments = Bundle()
            fragment.arguments?.putInt(TOTAL_COUNT_EXTRA, totalCount)
            return fragment
        }
        private const val TOTAL_COUNT_EXTRA = "TOTAL_COUNT_EXTRA"

    }

    override fun setCount(count: Int) {
        setCountText(count)
    }

    private fun setCountText(count: Int) {
        binding.totalCountTextView.text =
            String.format(Locale.getDefault(), getString(R.string.results_count), count)
    }


}