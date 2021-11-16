package com.frozenpriest.ui.calendar

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.frozenpriest.R
import com.frozenpriest.data.local.LocalDaySchedule
import com.frozenpriest.data.local.Record
import com.frozenpriest.databinding.CalendarFragmentBinding
import com.frozenpriest.ui.calendar.viewholder.DayAdapter
import com.frozenpriest.ui.common.BaseFragment
import com.frozenpriest.ui.common.DialogManager
import com.frozenpriest.ui.common.viewmodels.ViewModelFactory
import com.frozenpriest.utils.LinearLayoutPagerManager
import com.frozenpriest.utils.MarginItemDecoration
import com.frozenpriest.utils.formatDate
import com.frozenpriest.utils.getDateOfDayOfWeek
import com.frozenpriest.utils.getDayOfMonth
import java.time.DayOfWeek
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

    @Inject
    lateinit var dialogManager: DialogManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = CalendarFragmentBinding.bind(view)
        injector.inject(this)

        viewModel = ViewModelProvider(this, myViewModelFactory)[CalendarViewModel::class.java]

        setDayOfWeekMarks()
        binding.textViewDatePeriod.setOnClickListener {
            dialogManager.showDatePicker(resources.getString(R.string.select_week)) {
                viewModel.selectWeek(it)
            }
        }

        viewModel.currentDate.observe(viewLifecycleOwner) { date ->
            this.currentDate = date
            setWeekBorders(date)
            resetDayOfMonthHighlight(date)
        }

        viewModel.weekDatesSorted.observe(viewLifecycleOwner) { week ->
            setWeekBorders(week.first())
            viewModel.currentDate.value?.let {
                resetDayOfMonthHighlight(it)
            }
            setDayOfMonthText(week)
        }

        viewModel.schedule.observe(viewLifecycleOwner) { schedule ->
            setupRecyclerViews(schedule.daySchedules)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun setDayOfMonthText(week: List<Date>) {
        binding.apply {
            monday.textViewDate.text = week[DayOfWeek.MONDAY.ordinal].getDayOfMonth().toString()
            tuesday.textViewDate.text =
                week[DayOfWeek.TUESDAY.ordinal].getDayOfMonth().toString()
            wednesday.textViewDate.text =
                week[DayOfWeek.WEDNESDAY.ordinal].getDayOfMonth().toString()
            thursday.textViewDate.text =
                week[DayOfWeek.THURSDAY.ordinal].getDayOfMonth().toString()
            friday.textViewDate.text = week[DayOfWeek.FRIDAY.ordinal].getDayOfMonth().toString()
            saturday.textViewDate.text =
                week[DayOfWeek.SATURDAY.ordinal].getDayOfMonth().toString()
            sunday.textViewDate.text = week[DayOfWeek.SUNDAY.ordinal].getDayOfMonth().toString()
        }
    }

    private fun resetDayOfMonthHighlight(date: Date) {
        binding.apply {
            viewModel.weekDatesSorted.value?.let {
                resetDividerVisibility(it, date)
                val highlighted = ContextCompat.getColor(requireContext(), R.color.active_day)
                val notHighlighted = ContextCompat.getColor(requireContext(), R.color.black)

                resetHighlightDateText(it, date, highlighted, notHighlighted)
                resetHighlightDayOfWeekText(it, date, highlighted, notHighlighted)
            }
        }
    }

    private fun resetDividerVisibility(
        week: List<Date>,
        date: Date
    ) {
        binding.monday.dividerMarker.visibility =
            if (week[DayOfWeek.MONDAY.ordinal] == date) View.VISIBLE else View.INVISIBLE
        binding.tuesday.dividerMarker.visibility =
            if (week[DayOfWeek.TUESDAY.ordinal] == date) View.VISIBLE else View.INVISIBLE
        binding.wednesday.dividerMarker.visibility =
            if (week[DayOfWeek.WEDNESDAY.ordinal] == date) View.VISIBLE else View.INVISIBLE
        binding.thursday.dividerMarker.visibility =
            if (week[DayOfWeek.THURSDAY.ordinal] == date) View.VISIBLE else View.INVISIBLE
        binding.friday.dividerMarker.visibility =
            if (week[DayOfWeek.FRIDAY.ordinal] == date) View.VISIBLE else View.INVISIBLE
        binding.saturday.dividerMarker.visibility =
            if (week[DayOfWeek.SATURDAY.ordinal] == date) View.VISIBLE else View.INVISIBLE
        binding.sunday.dividerMarker.visibility =
            if (week[DayOfWeek.SUNDAY.ordinal] == date) View.VISIBLE else View.INVISIBLE
    }

    private fun resetHighlightDayOfWeekText(
        week: List<Date>,
        date: Date,
        highlighted: Int,
        notHighlighted: Int
    ) {
        binding.monday.textViewDayOfWeek.setTextColor(
            if (week[DayOfWeek.MONDAY.ordinal] == date) highlighted else notHighlighted
        )
        binding.tuesday.textViewDayOfWeek.setTextColor(
            if (week[DayOfWeek.TUESDAY.ordinal] == date) highlighted else notHighlighted
        )
        binding.wednesday.textViewDayOfWeek.setTextColor(
            if (week[DayOfWeek.WEDNESDAY.ordinal] == date) highlighted else notHighlighted
        )
        binding.thursday.textViewDayOfWeek.setTextColor(
            if (week[DayOfWeek.THURSDAY.ordinal] == date) highlighted else notHighlighted
        )
        binding.friday.textViewDayOfWeek.setTextColor(
            if (week[DayOfWeek.FRIDAY.ordinal] == date) highlighted else notHighlighted
        )
        binding.saturday.textViewDayOfWeek.setTextColor(
            if (week[DayOfWeek.SATURDAY.ordinal] == date) highlighted else notHighlighted
        )
        binding.sunday.textViewDayOfWeek.setTextColor(
            if (week[DayOfWeek.SUNDAY.ordinal] == date) highlighted else notHighlighted
        )
    }

    private fun resetHighlightDateText(
        it: List<Date>,
        date: Date,
        highlighted: Int,
        notHighlighted: Int
    ) {
        binding.monday.textViewDate.setTextColor(
            if (it[DayOfWeek.MONDAY.ordinal] == date) highlighted else notHighlighted
        )
        binding.tuesday.textViewDate.setTextColor(
            if (it[DayOfWeek.TUESDAY.ordinal] == date) highlighted else notHighlighted
        )
        binding.wednesday.textViewDate.setTextColor(
            if (it[DayOfWeek.WEDNESDAY.ordinal] == date) highlighted else notHighlighted
        )
        binding.thursday.textViewDate.setTextColor(
            if (it[DayOfWeek.THURSDAY.ordinal] == date) highlighted else notHighlighted
        )
        binding.friday.textViewDate.setTextColor(
            if (it[DayOfWeek.FRIDAY.ordinal] == date) highlighted else notHighlighted
        )
        binding.saturday.textViewDate.setTextColor(
            if (it[DayOfWeek.SATURDAY.ordinal] == date) highlighted else notHighlighted
        )
        binding.sunday.textViewDate.setTextColor(
            if (it[DayOfWeek.SUNDAY.ordinal] == date) highlighted else notHighlighted
        )
    }

    private fun setDayOfWeekMarks() {
        binding.apply {
            monday.textViewDayOfWeek.text = resources.getString(R.string.monday)
            tuesday.textViewDayOfWeek.text = resources.getString(R.string.tuesday)
            wednesday.textViewDayOfWeek.text = resources.getString(R.string.wednesday)
            thursday.textViewDayOfWeek.text = resources.getString(R.string.thursday)
            friday.textViewDayOfWeek.text = resources.getString(R.string.friday)
            saturday.textViewDayOfWeek.text = resources.getString(R.string.saturday)
            sunday.textViewDayOfWeek.text = resources.getString(R.string.sunday)
        }
    }

    private fun setWeekBorders(date: Date) {
        val monday = date.getDateOfDayOfWeek(Calendar.MONDAY).formatDate()
        val sunday = date.getDateOfDayOfWeek(Calendar.SUNDAY).formatDate()
        binding.textViewDatePeriod.text =
            requireContext().resources.getString(R.string.date_period, monday, sunday)
    }

    private lateinit var currentDate: Date
    private fun setupRecyclerViews(daySchedules: Map<Date, LocalDaySchedule>) {
        viewModel.weekDatesSorted.value?.let {
            setupDayRecyclerView(
                binding.monday.rvRecords,
                daySchedules[it[DayOfWeek.MONDAY.ordinal]]?.records ?: emptyList()
            )
            setupDayRecyclerView(
                binding.tuesday.rvRecords,
                daySchedules[it[DayOfWeek.TUESDAY.ordinal]]?.records ?: emptyList()
            )
            setupDayRecyclerView(
                binding.wednesday.rvRecords,
                daySchedules[it[DayOfWeek.WEDNESDAY.ordinal]]?.records ?: emptyList()
            )
            setupDayRecyclerView(
                binding.thursday.rvRecords,
                daySchedules[it[DayOfWeek.THURSDAY.ordinal]]?.records ?: emptyList()
            )
            setupDayRecyclerView(
                binding.friday.rvRecords,
                daySchedules[it[DayOfWeek.FRIDAY.ordinal]]?.records ?: emptyList()
            )
            setupDayRecyclerView(
                binding.saturday.rvRecords,
                daySchedules[it[DayOfWeek.SATURDAY.ordinal]]?.records ?: emptyList()
            )
            setupDayRecyclerView(
                binding.sunday.rvRecords,
                daySchedules[it[DayOfWeek.SUNDAY.ordinal]]?.records ?: emptyList()
            )
        }
    }

    private fun setupDayRecyclerView(
        recyclerView: RecyclerView,
        records: List<Record>,
        itemsPerPade: Int = 6
    ) {
        recyclerView.apply {
            adapter = DayAdapter(context).apply {
                setRecords(records)
            }
            layoutManager = LinearLayoutPagerManager(
                context, LinearLayoutManager.HORIZONTAL, false, itemsPerPade
            )
            addItemDecoration(
                MarginItemDecoration(
                    resources.getDimension(R.dimen.day_padding).toInt()
                )
            )
        }
    }
}
