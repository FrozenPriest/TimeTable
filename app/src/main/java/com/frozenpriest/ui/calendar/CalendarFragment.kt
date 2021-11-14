package com.frozenpriest.ui.calendar

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.frozenpriest.R
import com.frozenpriest.data.local.Record
import com.frozenpriest.databinding.CalendarFragmentBinding
import com.frozenpriest.ui.calendar.viewholder.DayAdapter
import com.frozenpriest.ui.common.BaseFragment
import com.frozenpriest.ui.common.viewmodels.ViewModelFactory
import com.frozenpriest.utils.LinearLayoutPagerManager
import com.frozenpriest.utils.MarginItemDecoration
import javax.inject.Inject

class CalendarFragment : BaseFragment(R.layout.calendar_fragment) {

    companion object {
        fun newInstance() = CalendarFragment()
    }
    @Inject
    lateinit var myViewModelFactory: ViewModelFactory
    private lateinit var viewModel: CalendarViewModel
    private lateinit var binding: CalendarFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = CalendarFragmentBinding.bind(view)
        injector.inject(this)

        viewModel = ViewModelProvider(this, myViewModelFactory)[CalendarViewModel::class.java]

        viewModel.currentDate.observe(viewLifecycleOwner) { date ->
        }

        viewModel.schedule.observe(viewLifecycleOwner) { schedule ->
            setupRecyclerViews(emptyList())
        }
    }
    private fun setupRecyclerViews(records: List<Record>) {

        setupDayRecyclerView(binding.monday.rvRecords, records)
        setupDayRecyclerView(binding.tuesday.rvRecords, records)
        setupDayRecyclerView(binding.wednesday.rvRecords, records)
        setupDayRecyclerView(binding.thursday.rvRecords, records)
        setupDayRecyclerView(binding.friday.rvRecords, records)
        setupDayRecyclerView(binding.saturday.rvRecords, records)
        setupDayRecyclerView(binding.sunday.rvRecords, records)
    }

    private fun setupDayRecyclerView(recyclerView: RecyclerView, records: List<Record>, itemsPerPade: Int = 6) {
        recyclerView.apply {
            adapter = DayAdapter(context).apply {
                setRecords(records)
            }
            layoutManager = LinearLayoutPagerManager(
                context, LinearLayoutManager.HORIZONTAL, false, itemsPerPade
            )
            addItemDecoration(MarginItemDecoration(resources.getDimension(R.dimen.day_padding).toInt()))
        }
    }
}
