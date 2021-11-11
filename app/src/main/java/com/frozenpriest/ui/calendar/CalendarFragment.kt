package com.frozenpriest.ui.calendar

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.frozenpriest.R
import com.frozenpriest.data.local.Record
import com.frozenpriest.data.local.RecordType
import com.frozenpriest.data.local.makeEmptyRecord
import com.frozenpriest.data.local.makeNoShiftRecord
import com.frozenpriest.databinding.CalendarFragmentBinding
import com.frozenpriest.ui.calendar.viewholder.DayAdapter
import com.frozenpriest.utils.LinearLayoutPagerManager
import com.frozenpriest.utils.MarginItemDecoration

class CalendarFragment : Fragment(R.layout.calendar_fragment) {

    companion object {
        fun newInstance() = CalendarFragment()
    }

    private lateinit var viewModel: CalendarViewModel
    private lateinit var binding: CalendarFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = CalendarFragmentBinding.bind(view)

        viewModel = ViewModelProvider(this)[CalendarViewModel::class.java]

        setupRecyclerViews()
    }
    @Suppress("MagicNumber")
    fun setupRecyclerViews() {
        val records = listOf(
            makeEmptyRecord(requireContext(), 24000, 120),
            Record("No name", 36000, 60, Color.RED, Color.GRAY, RecordType.OCCUPIED),
            Record("No name1", 36400, 20, Color.RED, Color.GRAY, RecordType.OCCUPIED),
            makeNoShiftRecord(requireContext(), 48000, 245),
            Record("No name2", 36500, 10, Color.RED, Color.GRAY, RecordType.OCCUPIED),
            Record("No name3", 36200, 70, Color.RED, Color.GRAY, RecordType.OCCUPIED),
        )

        binding.monday.rvRecords.apply {
            adapter = DayAdapter().apply {
                setRecords(records)
            }
            layoutManager = LinearLayoutPagerManager(
                context, LinearLayoutManager.HORIZONTAL, false, 6
            )
            addItemDecoration(MarginItemDecoration(resources.getDimension(R.dimen.day_padding).toInt()))
        }
    }
}
