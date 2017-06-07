package cz.intik.overflowindicator;

import android.support.annotation.NonNull;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;

/**
 * SnapHelper which allows snapping of pages, customized with notifying of {@link OverflowPagerIndicator}
 * when position is snapped
 *
 * @author Petr Introvic <introvic.petr@gmail.com>
 *         created 07.10.2017.
 */
public class SimpleSnapHelper extends PagerSnapHelper {

	private OverflowPagerIndicator mOverflowPagerIndicator;

	public SimpleSnapHelper(@NonNull OverflowPagerIndicator overflowPagerIndicator) {
		mOverflowPagerIndicator = overflowPagerIndicator;
	}

	@Override
	public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {
		int position = super.findTargetSnapPosition(layoutManager, velocityX, velocityY);

		// Notify OverflowPagerIndicator about changed page
		mOverflowPagerIndicator.onPageSelected(position);

		return position;
	}

}
