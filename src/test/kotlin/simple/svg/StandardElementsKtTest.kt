package simple.svg

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class StandardElementsKtTest {
    private val rectX = 0f
    private val rectY = 0f
    private val rectW = 40f
    private val rectH = 20f
    private val rectRX = 5f
    private val rectRY = 5f
    private val rect = path("M 0 0 h 40 v 20 h -40 v -20")
    private val roundedRect = path("M 5 0 h 30 a 5 5 0 0 1 5 5 v 10 a 5 5 0 0 1 -5 5 h -30 a 5 5 0 0 1 -5 -5 v -10 a 5 5 0 0 1 5 -5")

    private val circleCX = 0f
    private val circleCY = 0f
    private val circleR = 100f
    private val circle = path("M -100 0 a 100 100 0 0 0 200 0 a 100 100 0 0 0 -200 0")

    private val ellipseCX = 0f
    private val ellipseCY = 0f
    private val ellipseRX = 100f
    private val ellipseRY = 50f
    private val ellipse = path("M -100 0 a 100 50 0 0 0 200 0 a 100 50 0 0 0 -200 0")

    private val lineX1 = 0f
    private val lineY1 = 0f
    private val lineX2 = 50f
    private val lineY2 = 50f
    private val line = path("M 0 0 L 50 50")

    private val polylinePoints = listOf(0f to 0f, 50f to 50f, 0f to 100f, -50f to 50f, 0f to 0f)
    private val polyline = path("M 0 0 L 50 50 L 0 100 L -50 50 L 0 0")

    private val polygonPoints = listOf(0f to 0f, 50f to 50f, 0f to 100f, -50f to 50f)
    private val polygon = path("M 0 0 L 50 50 L 0 100 L -50 50 Z")

    @Test
    fun rect() {
        assertEquals(rect, rect(rectX, rectY, rectW, rectH))
        assertEquals(roundedRect, rect(rectX, rectY, rectW, rectH, rectRX, rectRY))
    }

    @Test
    fun circle() {
        assertEquals(circle, circle(circleCX, circleCY, circleR))
    }

    @Test
    fun ellipse() {
        assertEquals(ellipse, ellipse(ellipseCX, ellipseCY, ellipseRX, ellipseRY))
    }

    @Test
    fun line() {
        assertEquals(line, line(lineX1, lineY1, lineX2, lineY2))
    }

    @Test
    fun polyline() {
        assertEquals(polyline, polyline(polylinePoints))
    }

    @Test
    fun polygon() {
        assertEquals(polygon, polygon(polygonPoints))
    }
}