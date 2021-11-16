package com.frozenpriest.ui.calendar

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.frozenpriest.R
import com.frozenpriest.data.local.LocalDaySchedule
import com.frozenpriest.data.local.Record
import com.frozenpriest.databinding.CalendarFragmentBinding
import com.frozenpriest.ui.calendar.viewholder.DayAdapter
import com.frozenpriest.ui.common.BaseFragment
import com.frozenpriest.ui.common.viewmodels.ViewModelFactory
import com.frozenpriest.utils.LinearLayoutPagerManager
import com.frozenpriest.utils.MarginItemDecoration
import com.frozenpriest.utils.formatDate
import com.frozenpriest.utils.getDateOfDayOfWeek
import java.util.Calendar
import java.util.Date
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
            this.currentDate = date
            val monday = date.getDateOfDayOfWeek(Calendar.MONDAY).formatDate()
            val sunday = date.getDateOfDayOfWeek(Calendar.SUNDAY).formatDate()
            binding.textViewDatePeriod.text = requireContext().resources.getString(R.string.date_period, monday, sunday)
        }

        viewModel.schedule.observe(viewLifecycleOwner) { schedule ->
            setupRecyclerViews(schedule.daySchedules)
        }
    }

    lateinit var currentDate: Date
    private fun setupRecyclerViews(daySchedules: Map<Date, LocalDaySchedule>) {
        setupDayRecyclerView(
            binding.monday.rvRecords,
            daySchedules[currentDate.getDateOfDayOfWeek(Calendar.MONDAY)]?.records ?: emptyList()
        )
        setupDayRecyclerView(
            binding.tuesday.rvRecords,
            daySchedules[currentDate.getDateOfDayOfWeek(Calendar.TUESDAY)]?.records ?: emptyList()
        )
        setupDayRecyclerView(
            binding.wednesday.rvRecords,
            daySchedules[currentDate.getDateOfDayOfWeek(Calendar.WEDNESDAY)]?.records ?: emptyList()
        )
        setupDayRecyclerView(
            binding.thursday.rvRecords,
            daySchedules[currentDate.getDateOfDayOfWeek(Calendar.THURSDAY)]?.records ?: emptyList()
        )
        setupDayRecyclerView(
            binding.friday.rvRecords,
            daySchedules[currentDate.getDateOfDayOfWeek(Calendar.FRIDAY)]?.records ?: emptyList()
        )
        setupDayRecyclerView(
            binding.saturday.rvRecords,
            daySchedules[currentDate.getDateOfDayOfWeek(Calendar.SATURDAY)]?.records ?: emptyList()
        )
        setupDayRecyclerView(
            binding.sunday.rvRecords,
            daySchedules[currentDate.getDateOfDayOfWeek(Calendar.SUNDAY)]?.records ?: emptyList()
        )
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
