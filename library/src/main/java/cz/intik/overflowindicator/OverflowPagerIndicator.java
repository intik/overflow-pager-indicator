package cz.intik.overflowindicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.SparseIntArray;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;

import com.transitionseverywhere.ChangeBounds;
import com.transitionseverywhere.Fade;
import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Pager indicator widget
 * <p>
 * - attach to recyclerView with {@link #attachToRecyclerView(RecyclerView)}
 * - add page selecting behavior with {@link SimpleSnapHelper} or custom {@link PagerSnapHelper}
 * or with custom logic which calls {@link #onPageSelected(int)}
 *
 * @author Petr Introvic <introvic.petr@gmail.com>
 * created 07.06.2017.
 */
public class OverflowPagerIndicator extends LinearLayout {
    private static final int MAX_INDICATORS       = 9;
    private static final int INDICATOR_SIZE_DIP   = 12;
    private static final int INDICATOR_MARGIN_DIP = 2;

    // State also represents indicator scale factor
    private static final float STATE_GONE     = 0;
    private static final float STATE_SMALLEST = 0.2f;
    private static final float STATE_SMALL    = 0.4f;
    private static final float STATE_NORMAL   = 0.6f;
    private static final float STATE_SELECTED = 1.0f;

    private int mIndicatorCount;
    private int mLastSelected;
    private int mIndicatorSize;
    private int mIndicatorMargin;

    private RecyclerView         mRecyclerView;
    private OverflowDataObserver mDataObserver;

    private SparseIntArray mapDrawablesIndicator = new SparseIntArray();
    private int dotStrokeColor;
    private int dotFillColor;

    public OverflowPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);

        DisplayMetrics dm = getResources().getDisplayMetrics();
        mIndicatorSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, INDICATOR_SIZE_DIP, dm);
        mIndicatorMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, INDICATOR_MARGIN_DIP, dm);

        mDataObserver = new OverflowDataObserver(this);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        dotFillColor = ContextCompat.getColor(context, R.color.dot_fill);
        dotStrokeColor = ContextCompat.getColor(context, R.color.dot_stroke);

        if (attrs != null) {
            TypedArray attributeArray = context.obtainStyledAttributes(attrs, R.styleable.OverflowPagerIndicator);

            if (attributeArray != null) {
                if (attributeArray.hasValue(R.styleable.OverflowPagerIndicator_dotFillColor)) {
                    dotFillColor = attributeArray.getColor(R.styleable.OverflowPagerIndicator_dotFillColor, dotFillColor);
                }

                if (attributeArray.hasValue(R.styleable.OverflowPagerIndicator_dotStrokeColor)) {
                    dotStrokeColor = attributeArray.getColor(R.styleable.OverflowPagerIndicator_dotStrokeColor, dotStrokeColor);
                }

                attributeArray.recycle();
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        if (mRecyclerView != null) {
            try {
                mRecyclerView.getAdapter().unregisterAdapterDataObserver(mDataObserver);
            } catch (IllegalStateException ise) {
                // Do nothing
            }
        }

        super.onDetachedFromWindow();
    }

    /**
     * @param position Page to be selected
     */
    public void onPageSelected(int position) {
        if (mIndicatorCount > MAX_INDICATORS) {
            updateOverflowState(position);
        } else {
            updateSimpleState(position);
        }
    }

    /**
     * @param recyclerView Target recycler view
     */
    public void attachToRecyclerView(final RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        mRecyclerView.getAdapter().registerAdapterDataObserver(mDataObserver);

        initIndicators();
    }

    public void registerDrawableIndicator(@DrawableRes Integer imageRes, int viewType) {
        mapDrawablesIndicator.put(viewType, imageRes);
    }

    void updateIndicatorsCount() {
        if (mIndicatorCount != mRecyclerView.getAdapter().getItemCount()) {
            initIndicators();
        }
    }

    private void initIndicators() {
        mLastSelected = -1;
        mIndicatorCount = mRecyclerView.getAdapter().getItemCount();
        createIndicators(mIndicatorSize, mIndicatorMargin);
        onPageSelected(0);
    }

    private void updateSimpleState(int position) {
        if (mLastSelected != -1) {
            animateViewScale(getChildAt(mLastSelected), STATE_NORMAL);
        }

        animateViewScale(getChildAt(position), STATE_SELECTED);

        mLastSelected = position;
    }

    private void updateOverflowState(int position) {
        if (mIndicatorCount == 0) {
            return;
        }

        if (position < 0 || position > mIndicatorCount) {
            return;
        }

        Transition transition = new TransitionSet()
                .setOrdering(TransitionSet.ORDERING_TOGETHER)
                .addTransition(new ChangeBounds())
                .addTransition(new Fade());

        TransitionManager.beginDelayedTransition(this, transition);

        float[] positionStates = new float[mIndicatorCount + 1];
        Arrays.fill(positionStates, STATE_GONE);

        int start = position - MAX_INDICATORS + 4;
        int realStart = Math.max(0, start);

        if (realStart + MAX_INDICATORS > mIndicatorCount) {
            realStart = mIndicatorCount - MAX_INDICATORS;
            positionStates[mIndicatorCount - 1] = STATE_NORMAL;
            positionStates[mIndicatorCount - 2] = STATE_NORMAL;
        } else {
            if (realStart + MAX_INDICATORS - 2 < mIndicatorCount) {
                positionStates[realStart + MAX_INDICATORS - 2] = STATE_SMALL;
            }
            if (realStart + MAX_INDICATORS - 1 < mIndicatorCount) {
                positionStates[realStart + MAX_INDICATORS - 1] = STATE_SMALLEST;
            }
        }

        for (int i = realStart; i < realStart + MAX_INDICATORS - 2; i++) {
            positionStates[i] = STATE_NORMAL;
        }

        if (position > 5) {
            positionStates[realStart] = STATE_SMALLEST;
            positionStates[realStart + 1] = STATE_SMALL;
        } else if (position == 5) {
            positionStates[realStart] = STATE_SMALL;
        }

        positionStates[position] = STATE_SELECTED;

        updateIndicators(positionStates);

        mLastSelected = position;
    }

    private void updateIndicators(float[] positionStates) {
        for (int i = 0; i < mIndicatorCount; i++) {
            View v = getChildAt(i);
            float state = positionStates[i];

            if (state == STATE_GONE) {
                v.setVisibility(GONE);

            } else {
                v.setVisibility(VISIBLE);
                animateViewScale(v, state);
            }

        }
    }

    private void createIndicators(int indicatorSize, int margin) {
        removeAllViews();

        if (mIndicatorCount <= 1) {
            return;
        }

        for (int i = 0; i < mIndicatorCount; i++) {
            addIndicator(mIndicatorCount > MAX_INDICATORS, indicatorSize, margin, i);
        }
    }

    private void addIndicator(boolean isOverflowState, int indicatorSize, int margin, int position) {
        View view = new View(getContext());
        int viewType = -1;
        if (mRecyclerView.getAdapter() != null){
            viewType = mRecyclerView.getAdapter().getItemViewType(position);
        }

        if (mapDrawablesIndicator.indexOfKey(viewType) > -1) {
            int res = mapDrawablesIndicator.get(viewType);
            view.setBackground(ContextCompat.getDrawable(getContext(), res));
        }else {
            view.setBackground(getDotDrawable());
        }
        if (isOverflowState) {
            animateViewScale(view, STATE_SMALLEST);
        } else {
            animateViewScale(view, STATE_NORMAL);
        }

        MarginLayoutParams params = new MarginLayoutParams(indicatorSize, indicatorSize);
        params.leftMargin = margin;
        params.rightMargin = margin;

        addView(view, params);
    }

    private Drawable getDotDrawable() {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.OVAL);
        drawable.setColor(dotFillColor);
        drawable.setStroke(Util.dpToPx(0.5d), dotStrokeColor);

        return drawable;
    }

    private void animateViewScale(@Nullable View view, float scale) {
        if (view == null) {
            return;
        }

        view.animate()
                .scaleX(scale)
                .scaleY(scale);
    }

}
