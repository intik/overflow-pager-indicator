package cz.intik.overflowindicator;

import android.content.res.Resources;

class Util {
    static int dpToPx(double dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
}
