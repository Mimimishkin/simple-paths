package simple.svg

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class PathKtTest {
    private val path = path("M 21 26 A 1 1 0 0 0 28 26 l 9 -13 V 35 h -22 Q 10 24 13 13 c 5 3 2 10 7 11 Z")
    private val relative = path("m 21 26 a 1 1 0 0 0 7 0 l 9 -13 v 22 h -22 q -5 -11 -2 -22 c 5 3 2 10 7 11 z")
    private val absolute = path("M 21 26 A 1 1 0 0 0 28 26 L 37 13 V 35 H 15 Q 10 24 13 13 C 18 16 15 23 20 24 Z")

    private val complex = path("M 10 19 A 1 1 0 0 0 36 26 Q 36 26 38 20 T 35 10 C 33 9 30 7 28 6 S 18 3 14 10")
    private val simplified = path("M 10 19 C 8.067003 26.1797 12.320298 33.567005 19.5 35.5 C 26.6797 37.432995 34.067005 33.179703 36 26 Q 36 26 38 20 Q 40 14 35 10 C 33 9 30 7 28 6 C 26 5 18 3 14 10")

    private val dirty = path("M 8 27 L 15 10 A 1 1 0 0 0 15 10 Z Q 8 27 8 27 Z")
    private val cleared = path("M 8 27 L 15 10 Z")

    private val bigPath = path("M 8 19 L 12 10 L 20 18 Z M 32 13 L 42 28 L 29 23 Z M 23 26 L 30 33 L 17 35 Z")
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
        assertEquals(simplified, complex.simplified)
    }

    @Test
    fun getCleared() {
        assertEquals(cleared, dirty.cleared)
    }

    @Test
    fun getSubPaths() {
        assertEquals(bigPath.subPaths, listOf(subPath1, subPath2, subPath3))
    }

    @Test
    fun getFullPath() {
        assertEquals(bigPath, listOf(subPath1, subPath2, subPath3).fullPath)
    }

    @Test
    fun getSvgPath() {
        assertEquals(simpleSvgPath, simplePath.svgPath)
    }
}