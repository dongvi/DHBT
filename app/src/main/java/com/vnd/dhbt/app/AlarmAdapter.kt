package com.vnd.dhbt.app

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vnd.dhbt.R
import com.vnd.dhbt.base.customviews.DaysOfWeekView
import com.vnd.dhbt.base.models.TimeAlarm
import com.vnd.dhbt.databinding.ItemAlarmBinding

class AlarmAdapter : RecyclerView.Adapter<AlarmAdapter.ViewHolder>() {
    private var dataList = mutableListOf<TimeAlarm>()
    private lateinit var listener: AlarmListener

    inner class ViewHolder(
        private val binding: ItemAlarmBinding,
        private val context: Context
    ) : RecyclerView.ViewHolder(binding.root) {
        fun setDataForItemView(data: TimeAlarm) {
            binding.apply {
                root.setOnLongClickListener {
                    listener.onClickRemove(dataList.indexOf(data))
                    true
                }

                alarmText.text = context.getString(R.string.time, data.hour, data.minute)

                alarmDaysText.text = DaysOfWeekView.DayOfWeek.toDayString(data.dayOfWeek)

                alarmSwitch.apply {
                    isChecked = data.status
                    setOnCheckedChangeListener { _, isChecked ->
                        listener.onSwitched(dataList.indexOf(data) ,isChecked)
                    }
                }
            }
        }
    }

    interface AlarmListener {
        fun onClickRemove(position: Int)
        fun onSwitched(position: Int, value: Boolean)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            ItemAlarmBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            parent.context
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setDataForItemView(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: MutableList<TimeAlarm>) {
        notifyDataSetChanged()
        dataList = data
    }

    fun setListener(listener: AlarmListener) {
        this.listener = listener
    }
}
