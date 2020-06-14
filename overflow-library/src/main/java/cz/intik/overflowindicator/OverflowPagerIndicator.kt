package cz.intik.overflowindicator

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.ChangeBounds
import androidx.transition.Fade
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import java.util.*
import kotlin.math.max

/**
 * Pager indicator widget
 *
 * - attach to recyclerView with [.attachToRecyclerView]
 * - add page selecting behavior with [SimpleSnapHelper] or custom [PagerSnapHelper]
 * or with custom logic which calls [.onPageSelected]
 *
 * @author Petr Introvic <introvic.petr@gmail.com>
 * created 07.06.2017.
 */
class OverflowPagerIndicator(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    var indicatorSize: Int = -1
        private set

    var indicatorMargin: Int = -1
        private set

    var dotStrokeColor: Int = -1
        private set

    var dotFillColor: Int = -1
        private set

    private val dataObserver: OverflowDataObserver
    private var indicatorCount: Int = 0
    private var lastSelected: Int = 0
    private var recyclerView: RecyclerView? = null

    private val dotDrawable: Drawable
        get() = GradientDrawable().apply {
            shape = GradientDrawable.OVAL
            setColor(dotFillColor)
            setStroke(Util.dpToPx(0.5), dotStrokeColor)
        }

    init {
        initAttrs(context, attrs)

        dataObserver = OverflowDataObserver(this)
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        dotFillColor = ContextCompat.getColor(context, R.color.dot_fill)
        dotStrokeColor = ContextCompat.getColor(context, R.color.dot_stroke)
        indicatorSize = context.resources.getDimensionPixelSize(R.dimen.indicator_size)
        indicatorMargin = context.resources.getDimensionPixelSize(R.dimen.indicator_margin)

        if (attrs == null) return

        val attributeArray =
            context.obtainStyledAttributes(attrs, R.styleable.OverflowPagerIndicator)

        try {
            dotFillColor = attributeArray
                .getColor(R.styleable.OverflowPagerIndicator_dotFillColor, dotFillColor)

            dotStrokeColor = attributeArray
                .getColor(R.styleable.OverflowPagerIndicator_dotStrokeColor, dotStrokeColor)

            indicatorSize = attributeArray
                .getDimensionPixelSize(
                    R.styleable.OverflowPagerIndicator_indicatorSize, indicatorSize
                )

            indicatorMargin = attributeArray
                .getDimensionPixelSize(
                    R.styleable.OverflowPagerIndicator_indicatorMargin, indicatorMargin
                )
        } finally {
            attributeArray.recycle()
        }
    }

    override fun onDetachedFromWindow() {
        try {
            recyclerView?.adapter?.unregisterAdapterDataObserver(dataObserver)
        } catch (ise: IllegalStateException) {
            // Do nothing
        }

        super.onDetachedFromWindow()
    }

    /**
     * @param position Page to be selected
     */
    fun onPageSelected(position: Int) = when {
        indicatorCount > MAX_INDICATORS -> updateOverflowState(position)
        else -> updateSimpleState(position)
    }

    /**
     * @param recyclerView Target recycler view
     */
    fun attachToRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView
        recyclerView.adapter?.registerAdapterDataObserver(dataObserver)

        initIndicators()
    }

    fun updateIndicatorsCount() {
        if (indicatorCount != recyclerView?.adapter?.itemCount) {
            initIndicators()
        }
    }

    private fun initIndicators() {
        lastSelected = -1
        indicatorCount = recyclerView?.adapter?.itemCount ?: 0
        createIndicators(indicatorSize, indicatorMargin)
        onPageSelected(0)
    }

    private fun updateSimpleState(position: Int) {
        if (lastSelected != -1) {
            animateViewScale(getChildAt(lastSelected), STATE_NORMAL)
        }

        animateViewScale(getChildAt(position), STATE_SELECTED)

        lastSelected = position
    }

    private fun updateOverflowState(position: Int) {
        if (indicatorCount == 0) return
        if (position < 0 || position > indicatorCount) return

        val transition = TransitionSet()
            .setOrdering(TransitionSet.ORDERING_TOGETHER)
            .addTransition(ChangeBounds())
            .addTransition(Fade())

        TransitionManager.beginDelayedTransition(this, transition)

        val positionStates = FloatArray(indicatorCount + 1)
        Arrays.fill(positionStates, STATE_GONE)

        val start = position - MAX_INDICATORS + 4
        var realStart = max(0, start)

        if (realStart + MAX_INDICATORS > indicatorCount) {
            realStart = indicatorCount - MAX_INDICATORS
            positionStates[indicatorCount - 1] = STATE_NORMAL
            positionStates[indicatorCount - 2] = STATE_NORMAL
        } else {
            if (realStart + MAX_INDICATORS - 2 < indicatorCount) {
                positionStates[realStart + MAX_INDICATORS - 2] = STATE_SMALL
            }
            if (realStart + MAX_INDICATORS - 1 < indicatorCount) {
                positionStates[realStart + MAX_INDICATORS - 1] = STATE_SMALLEST
            }
        }

        for (i in realStart until realStart + MAX_INDICATORS - 2) {
            positionStates[i] = STATE_NORMAL
        }

        if (position > 5) {
            positionStates[realStart] = STATE_SMALLEST
            positionStates[realStart + 1] = STATE_SMALL
        } else if (position == 5) {
            positionStates[realStart] = STATE_SMALL
        }

        positionStates[position] = STATE_SELECTED

        updateIndicators(positionStates)

        lastSelected = position
    }

    private fun updateIndicators(positionStates: FloatArray) {
        for (i in 0 until indicatorCount) {
            val v = getChildAt(i)

            when (val state = positionStates[i]) {
                STATE_GONE -> v.visibility = View.GONE
                else -> {
                    v.visibility = View.VISIBLE
                    animateViewScale(v, state)
                }
            }

        }
    }

    private fun createIndicators(indicatorSize: Int, margin: Int) {
        removeAllViews()

        if (indicatorCount <= 1) return

        for (i in 0 until indicatorCount) {
            addIndicator(indicatorCount > MAX_INDICATORS, indicatorSize, margin)
        }
    }

    private fun addIndicator(isOverflowState: Boolean, indicatorSize: Int, margin: Int) {
        val view = View(context)
        view.background = dotDrawable

        animateViewScale(view, if (isOverflowState) STATE_SMALLEST else STATE_NORMAL)

        val params = MarginLayoutParams(indicatorSize, indicatorSize).apply {
            leftMargin = margin
            rightMargin = margin
        }

        addView(view, params)
    }

    private fun animateViewScale(view: View?, scale: Float) {
        if (view == null) return

        view.animate()
            .scaleX(scale)
            .scaleY(scale)
    }

    companion object {
        const val MAX_INDICATORS = 9

        // State also represents indicator scale factor
        const val STATE_GONE = 0f
        const val STATE_SMALLEST = 0.2f
        const val STATE_SMALL = 0.4f
        const val STATE_NORMAL = 0.6f
        const val STATE_SELECTED = 1.0f
    }

}
