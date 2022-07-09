package simple.svg

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class ReversedPathKtTest {
    private val normalPath = path("M 11 20 c 0 -20 30 -20 30 0 c 0 20 -30 20 -30 0 M 16 20 c 0 13 20 13 20 0 c 0 -13 -20 -13 -20 0")
    private val reversedPath = path("M 16 20 c 0 -13 20 -13 20 0 c 0 13 -20 13 -20 0 M 11 20 c 0 20 30 20 30 0 c 0 -20 -30 -20 -30 0")

    @Test
    fun getReversed() {
        assertEquals(reversedPath, normalPath.reversed)
    }
}