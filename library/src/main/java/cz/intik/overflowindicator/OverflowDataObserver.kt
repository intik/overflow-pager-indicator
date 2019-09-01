package cz.intik.overflowindicator

import androidx.recyclerview.widget.RecyclerView

/**
 * Data observer which notifies [OverflowPagerIndicator] of changed data
 *
 * @author Petr Introvic <introvic.petr@gmail.com>
 * created 03.10.2017.
 */

internal class OverflowDataObserver(private val overflowPagerIndicator: OverflowPagerIndicator) :
    RecyclerView.AdapterDataObserver() {

    override fun onChanged() = overflowPagerIndicator.updateIndicatorsCount()

    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) =
        overflowPagerIndicator.updateIndicatorsCount()

    override fun onItemRangeChanged(positionStart: Int, itemCount: Int) =
        overflowPagerIndicator.updateIndicatorsCount()

    override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) =
        overflowPagerIndicator.updateIndicatorsCount()

    override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) =
        overflowPagerIndicator.updateIndicatorsCount()

    override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) =
        overflowPagerIndicator.updateIndicatorsCount()
}
