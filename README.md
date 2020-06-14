# Overflow Pager Indicator widget

[![Download](https://jitpack.io/v/intik/overflow-pager-indicator.svg)](https://jitpack.io/#intik/overflow-pager-indicator)
[![](https://jitci.com/gh/intik/overflow-pager-indicator/svg)](https://jitci.com/gh/intik/overflow-pager-indicator)
[![license](https://img.shields.io/badge/license-MIT%20license-blue.svg)](LICENSE)

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
| ![Widget effect animation preview](docs/images/overflow-pager-indicator.gif "Preview of widget effect of animating dots during pages swiping")  | ![Classic ](docs/images/classic-indicators.png "Confusing")  |
 
_Disclaimer: Having too many pages in recycler means that user needs to swipe a lot. Different layout/ui may be more user friendly._ 
 
## Usage

### Migration to 3.1 from 3.0

`dotFillColor` and `dotStrokeColor` were renamed to `indicatorFillColor` and `indicatorStrokeColor`

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
implementation "cz.intik:overflow-pager-indicator:$latestVersion" // 3.1.0
```

### Layout

Some layout with RecyclerView and OverflowPagerIndicator

```xml
<FrameLayout
   android:layout_width="match_parent"
   android:layout_height="wrap_content"
   >

   <androidx.recyclerview.widget.RecyclerView
      android:id="@+id/recyclerView"
      android:layout_width="match_parent"
      android:layout_height="120dp"
      />

   <cz.intik.overflowindicator.OverflowPagerIndicator
      android:id="@+id/viewOverflowPagerIndicator"
      android:layout_width="wrap_content"
      android:layout_height="wrap_content"
      android:layout_gravity="center_horizontal|bottom"
      app:indicatorFillColor="#FF0000"
      app:indicatorStrokeColor="#0000FF"
      />

</FrameLayout>
```

### Code

Attach
[OverflowPagerIndicator](overflow-library/src/main/java/cz/intik/overflowindicator/OverflowPagerIndicator.kt)
(usually after LayoutManager and Adapter setup) to
[RecyclerView](https://developer.android.com/reference/androidx/recyclerview/widget/RecyclerView)
\- for listening to dataset changes:

```kotlin
viewOverflowPagerIndicator.attachToRecyclerView(recyclerView)
```

Attach
[SimpleSnapHelper](overflow-library/src/main/java/cz/intik/overflowindicator/SimpleSnapHelper.kt)
to recycler view which will change selected page in indicator view as items in recycler 
view are snapped:

```kotlin
val snapHelper = SimpleSnapHelper(viewOverflowPagerIndicator)
snapHelper.attachToRecyclerView(recyclerView)
```

Or use any other implementation of
[PagerSnapHelper](https://developer.android.com/reference/androidx/recyclerview/widget/PagerSnapHelper "Android Developers - docs - PagerSnapHelper")
or even some custom logic which will call:

```kotlin
viewOverflowPagerIndicator.onPageSelected(position)
```

### Customization

You can easily change dot fill color and dot stroke color via xml attributes like this:
```xml
<cz.intik.overflowindicator.OverflowPagerIndicator
    app:indicatorFillColor="#FF0000"
    app:indicatorStrokeColor="@color/heavenlyBlue"
    app:indicatorMargin="6dp"
    app:indicatorSize="22dp"
    />
``` 

### Changelog

3.1.0 Add customization of indicator size and margin. Unify naming of attributes to "indicatorXxx" instead of "dotXxx"

3.0.1 Remove library from Bintray, use simply Jitpack. Convert library to Kotlin and update to TransitionsEverywhere 2.0 (which uses androidx.transition.X heavily)

2.0.0 Migrate to AndroidX, add color customization options (big thanks [Javi Chaqués](https://github.com/javichaques))

1.2.1 bugfix (thanks [Sajad Abasi](https://github.com/sajadabasi))