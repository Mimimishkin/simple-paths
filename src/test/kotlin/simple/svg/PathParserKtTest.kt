package simple.svg

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class PathParserKtTest {
    private val path = path("M-.46.618a1 1 0 1 0 .936-.023L.837-.329c1.059-.609-2.109-.936-1.241-.192Q-.753-.183-.46.257z")
    private val expected = emptyPath()
        .moveTo(x = -0.46f, y = 0.618f)
        .arcToRelative(rx = 1f, ry = 1f, xAxisRotation = 0f, largeArc = true, sweep = false, dx = 0.936f, dy = -0.023f)
        .lineTo(x = 0.837f, y = -0.329f)
        .cubicToRelative(dx1 = 1.059f, dy1 = -0.609f, dx2 = -2.109f, dy2 = -0.936f, dx = -1.241f, dy = -0.192f)
        .quadTo(x1 = -0.753f, y1 = -0.183f, x = -0.46f, y = 0.257f)
        .close()
        .done()

    @Test
    fun parsePath() {
        assertEquals(expected, path)
    }
}