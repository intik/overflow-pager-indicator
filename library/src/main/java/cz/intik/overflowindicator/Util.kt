package cz.intik.overflowindicator

import android.content.res.Resources

internal object Util {
    fun dpToPx(dp: Double): Int = (dp * Resources.getSystem().displayMetrics.density).toInt()
}
