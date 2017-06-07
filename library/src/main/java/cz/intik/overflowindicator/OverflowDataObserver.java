package cz.intik.overflowindicator;

import android.support.v7.widget.RecyclerView;

/**
 * Data observer which notifies {@link OverflowPagerIndicator} of changed data
 *
 * @author Petr Introvic <introvic.petr@gmail.com>
 *         created 03.10.2017.
 */

class OverflowDataObserver extends RecyclerView.AdapterDataObserver {

	private OverflowPagerIndicator mOverflowPagerIndicator;

	OverflowDataObserver(OverflowPagerIndicator overflowPagerIndicator) {
		mOverflowPagerIndicator = overflowPagerIndicator;
	}

	@Override
	public void onChanged() {
		mOverflowPagerIndicator.updateIndicatorsCount();
	}

	@Override
	public void onItemRangeInserted(int positionStart, int itemCount) {
		mOverflowPagerIndicator.updateIndicatorsCount();
	}

	@Override
	public void onItemRangeChanged(int positionStart, int itemCount) {
		mOverflowPagerIndicator.updateIndicatorsCount();
	}

	@Override
	public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
		mOverflowPagerIndicator.updateIndicatorsCount();
	}

	@Override
	public void onItemRangeRemoved(int positionStart, int itemCount) {
		mOverflowPagerIndicator.updateIndicatorsCount();
	}

	@Override
	public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
		mOverflowPagerIndicator.updateIndicatorsCount();
	}
}
