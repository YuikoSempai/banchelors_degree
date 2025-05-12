package com.yuiko.mobile_quiz_system.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.yuiko.mobile_quiz_system.R
import com.yuiko.mobile_quiz_system.model.SimpleQuiz

// adapter/TestAdapter.kt
class TestAdapter(
    private val onTestClick: (SimpleQuiz) -> Unit
) : RecyclerView.Adapter<TestAdapter.TestViewHolder>() {

    private val data = mutableListOf<SimpleQuiz>()

    fun setData(newData: List<SimpleQuiz>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_test, parent, false)
        return TestViewHolder(v)
    }

    override fun onBindViewHolder(holder: TestViewHolder, position: Int) {
        holder.bind(data[position], onTestClick)
    }

    class TestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivIcon = itemView.findViewById<ImageView>(R.id.ivIcon)
        private val tvTitle = itemView.findViewById<TextView>(R.id.tvTestTitle)
        private val tvPageCount = itemView.findViewById<TextView>(R.id.tvPageCount)
        private val btnGo = itemView.findViewById<ImageButton>(R.id.btnGo)

        fun bind(test: SimpleQuiz, onTestClick: (SimpleQuiz) -> Unit) {
            tvTitle.text = test.name
            tvPageCount.text = "Страниц: ${test.pageCount}"
            if (test.available) {
                itemView.alpha = 1.0f
                itemView.isEnabled = true
                // Можно добавить иконку/текст "Доступно"
                btnGo.isEnabled = true
                btnGo.setImageResource(R.drawable.ic_arrow_right) // обычная иконка
//                btnGo.setColorFilter(ContextCompat.getColor(itemView.context, R.color.my_active))
            } else {
                itemView.alpha = 0.5f
                itemView.isEnabled = false
                // Можно заменить иконку/кнопку на "замок" или серую стрелку
                btnGo.isEnabled = false
                btnGo.setImageResource(R.drawable.ic_done) // ic_lock — иконка замка (добаваь через Vector Asset)
                btnGo.setColorFilter(ContextCompat.getColor(itemView.context, android.R.color.holo_green_light))
            }

            btnGo.setOnClickListener { onTestClick(test) }
            itemView.setOnClickListener { onTestClick(test) }
        }
    }
}
