package simple.svg

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class PathUtilsKtTest {
    private val rect = rect(0f, 0f, 10f, 10f)
    private val bounds = Bounds(0f, 0f, 10f, 10f)

    private val pointIn = listOf(5f, 5f)
    private val pointOut = listOf(15f, 15f)

    private val boxIn = listOf(3.5f, 3.5f, 1f, 1f)
    private val boxOut = listOf(15f, 15f, 5f, 5f)
    private val boxIntersects = listOf(5f, 5f, 10f, 10f)

    @Test
    fun getBounds() {
        assertEquals(bounds, rect.bounds)
    }

    @Test
    fun contains() {
        assert(rect.contains(pointIn[0], pointIn[1]))
        assert(!rect.contains(pointOut[0], pointOut[1]))

        assert(rect.contains(boxIn[0], boxIn[1], boxIn[2], boxIn[3]))
        assert(!rect.contains(boxOut[0], boxOut[1], boxOut[2], boxOut[3]))
        assert(!rect.contains(boxIntersects[0], boxIntersects[1], boxIntersects[2], boxIntersects[3]))
    }

    @Test
    fun intersects() {
        assert(rect.intersects(boxIn[0], boxIn[1], boxIn[2], boxIn[3]))
        assert(!rect.intersects(boxOut[0], boxOut[1], boxOut[2], boxOut[3]))
        assert(rect.intersects(boxIntersects[0], boxIntersects[1], boxIntersects[2], boxIntersects[3]))
    }
}