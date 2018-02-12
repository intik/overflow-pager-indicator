# Overflow Pager Indicator widget

[![Download](https://api.bintray.com/packages/intik/cz.intik/overflowpagerindicator/images/download.svg)](https://bintray.com/intik/cz.intik/overflowpagerindicator/_latestVersion)
[![license](https://img.shields.io/badge/license-MIT%20license-blue.svg)](https://github.com/intik/overflow-pager-indicator/blob/master/LICENSE)

Simple widget for recycler view - displaying dots indicators of currently selected page - 
with some fancy animation when dataset is large.

**Large dataset** displays only fixed amount of dots centered around currently selected page 
with a nice little scaling animation of indicators on the edges. Provides fluent animation of 
selecting new page.
   
**Small dataset** displays classic dot indicators line with currently selected page highlighted.

RecyclerView should be configured with snapping to pages vie PagerSnapHelper subclass ~ simulates 
behavior of classic ViewPager
 
## Widget preview

| :heavy_check_mark: Overflowed indicators | :x: Classic confusing indicators |
| ----------------------- | ------------------------------- |
| ![Widget effect animation preview](docs/images/overflow-pager-indicator.gif "Preview of widget effect of animating dots during pages swiping")  | ![Classic ](docs/images/classic-indicators.png "Fujky")  |
 
_Disclaimer: Having too many pages in recycler means that user needs to swipe a lot. Different layout/ui may be more user friendly._ 
 
## Usage

### Gradle dependency

In your gradle add dependency:

```gradle
compile "cz.intik:overflow-pager-indicator:$latestVersion"
```

### Layout

Some layout with RecyclerView and OverflowPagerIndicator

```xml
<FrameLayout
   android:layout_width="match_parent"
   android:layout_height="wrap_content"
   >

   <android.support.v7.widget.RecyclerView
      android:id="@+id/recycler_view"
      android:layout_width="match_parent"
      android:layout_height="120dp"
      />

   <cz.intik.overflowindicator.OverflowPagerIndicator
      android:id="@+id/view_pager_indicator"
      android:layout_width="wrap_content"
      android:layout_height="wrap_content"
      android:layout_gravity="center_horizontal|bottom"
      />

</FrameLayout>
```

### Code

Attach
[OverflowPagerIndicator](https://github.com/intik/overflow-pager-indicator/blob/docs-update/library/src/main/java/cz/intik/overflowindicator/OverflowPagerIndicator.java)
(usually after LayoutManager and Adapter setup) to
[RecyclerView](https://developer.android.com/reference/android/support/v7/widget/RecyclerView.html)
\- for listening to dataset changes

```java
overflowPagerIndicator.attachToRecyclerView(recyclerView);
```

Attach
[SimpleSnapHelper](https://github.com/intik/overflow-pager-indicator/blob/docs-update/library/src/main/java/cz/intik/overflowindicator/SimpleSnapHelper.java)
to recycler view which will change selected page in indicator view as items in recycler 
view are snapped

```java  
SimpleSnapHelper snapHelper = new SimpleSnapHelper(overflowPagerIndicator);
snapHelper.attachToRecyclerView(recyclerView);
```

Or use any other implementation of
[PagerSnapHelper](https://developer.android.com/reference/android/support/v7/widget/PagerSnapHelper.html "Android Developers - docs - PagerSnapHelper")
or even some custom logic which will call:

```java
OverflowPagerIndicator#onPageSelected(int position)
```

### Changelog

1.2.1 bugfix (thanks [Sajad Abasi](https://github.com/sajadabasi))