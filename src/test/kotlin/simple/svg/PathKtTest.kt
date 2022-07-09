package simple.svg

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class PathKtTest {
    private val path = path("M 21 26 A 1 1 0 0 0 28 26 l 9 -13 V 35 h -22 Q 10 24 13 13 c 5 3 2 10 7 11 Z")
    private val relative = path("m 21 26 a 1 1 0 0 0 7 0 l 9 -13 v 22 h -22 q -5 -11 -2 -22 c 5 3 2 10 7 11 z")
    private val absolute = path("M 21 26 A 1 1 0 0 0 28 26 L 37 13 V 35 H 15 Q 10 24 13 13 C 18 16 15 23 20 24 Z")
    private val simplified = path("M 21 26 C 21 27.932997 22.567003 29.5 24.5 29.5 C 26.432997 29.5 28 27.932997 28 26 L 37 13 L 37 35 L 15 35 Q 10 24 13 13 C 18 16 15 23 20 24 Z")

    private val dirty = path("M 8 27 L 15 10 A 1 1 0 0 0 15 10 Z Q 8 27 8 27 Z")
    private val cleared = path("M 8 27 L 15 10 Z")

    private val complexPath = path("M 8 19 L 12 10 L 20 18 Z M 32 13 L 42 28 L 29 23 Z M 23 26 L 30 33 L 17 35 Z")
    private val subPath1 = path("M 8 19 L 12 10 L 20 18 Z")
    private val subPath2 = path("M 32 13 L 42 28 L 29 23 Z")
    private val subPath3 = path("M 23 26 L 30 33 L 17 35 Z")

    private val simpleSvgPath = "M 25.0 24.0 H 13.0 V 13.0 Z"
    private val simplePath = emptyPath()
        .moveTo(25f, 24f)
        .horizontalLineTo(13f)
        .verticalLineTo(13f)
        .close()
        .done()

    @Test
    fun getAbsolute() {
        assertEquals(absolute, path.absolute)
    }

    @Test
    fun getRelative() {
        assertEquals(relative, path.relative)
    }

    @Test
    fun getSimplified() {
        assertEquals(simplified, path.simplified)
    }

    @Test
    fun getCleared() {
        assertEquals(cleared, dirty.cleared)
    }

    @Test
    fun getSubPaths() {
        assertEquals(complexPath.subPaths, listOf(subPath1, subPath2, subPath3))
    }

    @Test
    fun getFullPath() {
        assertEquals(complexPath, listOf(subPath1, subPath2, subPath3).fullPath)
    }

    @Test
    fun getSvgPath() {
        assertEquals(simpleSvgPath, simplePath.svgPath)
    }
}