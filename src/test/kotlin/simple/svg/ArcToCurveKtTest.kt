package simple.svg

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import simple.svg.Command.*

internal class ArcToCurveKtTest {
    val arc = ArcTo(1f, 1f, 0f, false, false, 10f, 10f)
    val curves = listOf(
        CubicTo(x1 = -1.786328f, y1 = 1.786328f, x2 = -2.4839685f, y2 = 4.389958f, x = -1.830127f, y = 6.8301272f),
        CubicTo(x1 = -1.1762856f, y1 = 9.270296f, x2 = 0.72970366f, y2 = 11.176286f, x = 3.169873f, y = 11.830127f),
        CubicTo(x1 = 5.6100426f, y1 = 12.483969f, x2 = 8.213673f, y2 = 11.786328f, x = 10.0f, y = 10.0f )
    )

    @Test
    fun arcToCurves() {
        assertEquals(curves, arc.curves(0f, 0f))
    }
}