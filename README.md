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

### Migration to 3.0 from 2.x

Make sure you have Jitpack dependency in root gradle file.

Follow [this instructions](https://github.com/andkulikov/Transitions-Everywhere#migration-from-1x-guide) to update
TransitionsEverywhere which migrated to Jetpack androidx.transition to prevent import crashes.

### Gradle dependency

In your root gradle add dependency to Jitpack:

```gradle
buildscript {
    repositories {
        xxx
        maven { url 'https://jitpack.io' }
    }
}
```

In you module gradle add dependency to library:

```gradle
implementation "cz.intik:overflow-pager-indicator:$latestVersion" // 3.0.1
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
      app:dotFillColor="#FF0000"
      app:dotStrokeColor="#0000FF"
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

### Customization

You can easily change dot fill color and dot stroke color via xml attributes like this:
```xml
<cz.intik.overflowindicator.OverflowPagerIndicator
    app:dotFillColor="#FF0000"
    app:dotStrokeColor="@color/heavenlyBlue"
    />
``` 

### Changelog

3.0.1 Remove library from Bintray, use simply Jitpack. Convert library to Kotlin and update to TransitionsEverywhere 2.0 (which uses androidx.transition.X heavily)

2.0.0 Migrate to AndroidX, add color customization options (big thanks [Javi Chaqués](https://github.com/javichaques))

1.2.1 bugfix (thanks [Sajad Abasi](https://github.com/sajadabasi))