# Overflow Pager Indicator widget

Overflow Pager Indicator is a simple widget for recycler view - displaying dots indicators of 
currently selected page - with some fancy animation when dataset is large.

When the dataset in recycler view is large it displays only fixed amount of dots centered around 
 currently selected page with a nice little scaling animation of indicators on the edges. Provides
 fluent animation of selecting new page.
   
If dataset in recycler view is small it displays classic dot indicators line with currently selected
page highlighted.

RecyclerView should be configured with snapping to pages vie PagerSnapHelper subclass ~ simulates 
behavior of classic ViewPager
 
# Usage

## Gradle dependency

In your gradle add dependency:

```gradle
compile 'cz.intik:overflow-pager-indicator:1.1.1'
```

## Layout

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

## Code

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