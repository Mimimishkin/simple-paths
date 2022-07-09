package simple.svg

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.lang.Math.toRadians

internal class TransformedPathKtTest {
    private val rect = rect(0f, 0f, 10f, 10f)

    private val scaleX = 2f
    private val scaledRect = rect(0f, 0f, 20f, 20f)

    private val translatedDistance = 10f
    private val translatedRect = rect(10f, 10f, 10f, 10f)

    private val theta = toRadians(180.0)
    private val rotatedRect = rect(-10f, -10f, 10f, 10f)

    private val shiftX = 2f
    private val shiftY = 4f
    private val shearedRect = path("M 0 0 L 10 40 L 30 50 L 20 10 L 0 0")

    @Test
    fun translated() {
        assertEquals(
            translatedRect.bounds,
            rect.translated(translatedDistance, translatedDistance).bounds
        )
    }

    @Test
    fun scaled() {
        assertEquals(
            scaledRect.bounds,
            rect.scaled(scaleX).bounds
        )
    }

    @Test
    fun rotated() {
        assertEquals(
            rotatedRect.bounds,
            rect.rotated(theta).bounds
        )
    }

    @Test
    fun sheared() {
        assertEquals(
            shearedRect.bounds,
            rect.sheared(shiftX, shiftY).bounds
        )
    }
}