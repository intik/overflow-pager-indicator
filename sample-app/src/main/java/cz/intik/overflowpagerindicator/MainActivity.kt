package cz.intik.overflowpagerindicator

import android.graphics.Color
import android.os.Bundle
import android.view.ViewGroup
import android.widget.FrameLayout

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cz.intik.overflowindicator.OverflowPagerIndicator
import cz.intik.overflowindicator.SimpleSnapHelper

/**
 * @author Petr Introvic <introvic.petr@gmail.com>
 * created 03.10.2017.
 */
class MainActivity : AppCompatActivity() {

    data class Data(
        val recyclerViewId: Int,
        val indicatorViewId: Int,
        val itemCount: Int,
        val delay: Long
    )

    private val data = listOf(
        Data(R.id.recyclerView, R.id.viewPagerIndicator, 20, 500L),
        Data(R.id.recyclerViewColors, R.id.viewPagerIndicatorColors, 9, 1_000L),
        Data(R.id.recyclerViewSizes, R.id.viewPagerIndicatorSizes, 5, 2_000L)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        data.forEach(::initRecyclerWithIndicator)
    }

    private fun initRecyclerWithIndicator(data: Data) {
        val (recyclerViewId, indicatorViewId, itemCount, delay) = data

        val recyclerView = findViewById<RecyclerView>(recyclerViewId)
        val overflowPagerIndicator = findViewById<OverflowPagerIndicator>(indicatorViewId)
        val adapter = Adapter()

        recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adapter

        overflowPagerIndicator.attachToRecyclerView(recyclerView)

        SimpleSnapHelper(overflowPagerIndicator).attachToRecyclerView(recyclerView)

        recyclerView.postDelayed({ adapter.updateItemCount(itemCount) }, delay)
    }

    private class Adapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        // Some material colors for recycler view items background
        private val colors =
            mutableListOf("#2196F3", "#00BCD4", "#4CAF50", "#CDDC39", "#FFC107", "#FF5722")
                .apply { shuffle() }

        private var itemCount = 0

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val view = FrameLayout(parent.context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }

            return object : RecyclerView.ViewHolder(view) {

            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val color = Color.parseColor(colors[position % colors.size])

            holder.itemView.setBackgroundColor(color)
        }

        override fun getItemCount(): Int = itemCount

        internal fun updateItemCount(newCount: Int) {
            itemCount = newCount

            notifyDataSetChanged()
        }
    }

}
