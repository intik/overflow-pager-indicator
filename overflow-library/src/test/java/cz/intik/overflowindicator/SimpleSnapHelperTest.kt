package cz.intik.overflowindicator

import androidx.recyclerview.widget.RecyclerView
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

/*
* @author Petr Introvic <introvic.petr@gmail.com>
* created 31.5.2020
*/
class SimpleSnapHelperTest {

    private lateinit var mockedLayoutManager: RecyclerView.LayoutManager
    private lateinit var mockedOverflowPagerIndicator: OverflowPagerIndicator
    private lateinit var testedSimpleSnapHelper: SimpleSnapHelper

    @Before
    fun setup() {
        mockedOverflowPagerIndicator = mockk(relaxUnitFun = true)
        mockedLayoutManager = mockk()

        testedSimpleSnapHelper = SimpleSnapHelper(mockedOverflowPagerIndicator)
    }

    @Test
    fun `test findTargetSnapPosition calls onPageSelected`() {
        every { mockedLayoutManager.itemCount } returns 0

        testedSimpleSnapHelper.findTargetSnapPosition(mockedLayoutManager, 0, 0)

        verify { mockedOverflowPagerIndicator.onPageSelected(any()) }
    }

}