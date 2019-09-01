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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initRecyclerWithIndicator(R.id.recycler_view, R.id.view_pager_indicator, 20, 1000)
        initRecyclerWithIndicator(
            R.id.recycler_view_simple, R.id.view_pager_indicator_simple, 5, 2000
        )
    }

    private fun initRecyclerWithIndicator(
        recyclerViewId: Int,
        indicatorViewId: Int,
        itemCount: Int,
        delay: Int
    ) {
        val recyclerView = findViewById<RecyclerView>(recyclerViewId)
        val overflowPagerIndicator = findViewById<OverflowPagerIndicator>(indicatorViewId)
        val adapter = Adapter()

        recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adapter

        overflowPagerIndicator.attachToRecyclerView(recyclerView)

        SimpleSnapHelper(overflowPagerIndicator).attachToRecyclerView(recyclerView)

        recyclerView.postDelayed({ adapter.updateItemCount(itemCount) }, delay.toLong())
    }

    private class Adapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        // Some material colors for recycler view items background
        private val colors =
            arrayOf("#2196F3", "#00BCD4", "#4CAF50", "#CDDC39", "#FFC107", "#FF5722")
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
