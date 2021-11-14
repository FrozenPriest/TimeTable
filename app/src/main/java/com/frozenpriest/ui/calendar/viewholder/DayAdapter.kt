package com.frozenpriest.ui.calendar.viewholder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.frozenpriest.R
import com.frozenpriest.data.local.Record
import com.frozenpriest.data.local.RecordType
import com.frozenpriest.utils.TextUtils
import javax.inject.Inject

class DayAdapter @Inject constructor(
    context: Context
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<Record>() {
        override fun areItemsTheSame(oldItem: Record, newItem: Record): Boolean {
            return oldItem.start == newItem.start
        }

        override fun areContentsTheSame(oldItem: Record, newItem: Record): Boolean {
            return oldItem == newItem
        }
    }
    private val differ = AsyncListDiffer(this, differCallback)

    fun setRecords(list: List<Record>) {
        differ.submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            RecordType.EMPTY.ordinal -> {
                val itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.empty_record_layout, parent, false)
                EmptyViewHolder(itemView)
            }
            RecordType.OCCUPIED.ordinal -> {
                val itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.filled_record_layout, parent, false)
                FilledViewHolder(itemView)
            }
            else -> throw NotImplementedError("View type not supported")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            RecordType.EMPTY.ordinal -> {
                (holder as EmptyViewHolder).bindView(position)
            }
            RecordType.OCCUPIED.ordinal -> {
                (holder as FilledViewHolder).bindView(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun getItemViewType(position: Int): Int {
        return differ.currentList[position].recordType.ordinal
    }

    private val emptyDividerColor = ContextCompat.getColor(context, R.color.divider_empty)
    private val noRecordColor = ContextCompat.getColor(context, R.color.no_record_background)

    inner class EmptyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val layout: ConstraintLayout = view.findViewById(R.id.constraintLayout)
        private val divider: View = view.findViewById(R.id.dividerMarker)
        private val textViewName: TextView = view.findViewById(R.id.textViewName)
        private val textViewTime: TextView = view.findViewById(R.id.textViewStartTime)

        fun bindView(position: Int) {
            differ.currentList[position].let {
                layout.setBackgroundColor(noRecordColor)
                divider.setBackgroundColor(emptyDividerColor)

                textViewName.text = it.patient?.name
                textViewTime.text = TextUtils.formatTimePeriod(it.start, it.start - it.end)
            }
        }
    }

    private val noShiftColor = ContextCompat.getColor(context, R.color.divider_no_shift)

    inner class NoShiftViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val layout: ConstraintLayout = view.findViewById(R.id.constraintLayout)
        private val divider: View = view.findViewById(R.id.dividerMarker)
        private val textViewName: TextView = view.findViewById(R.id.textViewName)

        private val textViewTime: TextView = view.findViewById(R.id.textViewStartTime)
        fun bindView(position: Int) {
            differ.currentList[position].let {
                layout.setBackgroundColor(noShiftColor)
                divider.setBackgroundColor(noShiftColor)

                textViewName.text = it.patient?.name
                textViewTime.text = TextUtils.formatTimePeriod(it.start, it.start - it.end)
            }
        }
    }

    inner class FilledViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val layout: ConstraintLayout = view.findViewById(R.id.constraintLayout)
        private val divider: View = view.findViewById(R.id.dividerMarker)
        private val textViewName: TextView = view.findViewById(R.id.textViewName)
        private val textViewTime: TextView = view.findViewById(R.id.textViewStartTime)
        private val textViewDuration: TextView = view.findViewById(R.id.textViewStartDuration)

        fun bindView(position: Int) {
            differ.currentList[position].let {
                it.backgroundColor?.let { color -> layout.setBackgroundColor(color) }
                it.dividerColor?.let { color -> divider.setBackgroundColor(color) }

                textViewName.text = it.patient?.name
                textViewTime.text = TextUtils.formatTime(it.start)

                textViewDuration.text =
                    itemView.resources.getString(R.string.duration_mins, it.start - it.end)
            }
        }
    }
}
