package cz.intik.overflowindicator

import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView

/**
 * SnapHelper which allows snapping of pages, customized with notifying of [OverflowPagerIndicator]
 * when position is snapped
 *
 * @author Petr Introvic <introvic.petr@gmail.com>
 * created 07.10.2017.
 */
class SimpleSnapHelper(private val overflowPagerIndicator: OverflowPagerIndicator) :
    PagerSnapHelper() {

    override fun findTargetSnapPosition(
        layoutManager: RecyclerView.LayoutManager,
        velocityX: Int,
        velocityY: Int
    ): Int {
        val position = super.findTargetSnapPosition(layoutManager, velocityX, velocityY)

        // Notify OverflowPagerIndicator about changed page
        overflowPagerIndicator.onPageSelected(position)

        return position
    }

}