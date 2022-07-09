// Copied from https://github.com/mickleness/pumpernickel/blob/master/src/main/java/com/pump/geom/ShapeBounds.java
package simple.svg

import kotlin.math.sqrt

data class Bounds(
    val x: Float,
    val y: Float,
    val w: Float,
    val h: Float,
)

private class EmptyPathException : RuntimeException()

internal fun pathBounds(path: Path): Bounds {
    var egles: FloatArray? = null
    val topMaxX = 0
    val topMaxY = 1
    val rightMaxX = 2
    val rightMaxY = 3
    val bottomMaxX = 4
    val bottomMaxY = 5
    val leftMaxX = 6
    val leftMaxY = 7

    fun refreshSizes(x: Float, y: Float) {
        if (x < egles!![leftMaxX]) {
            egles!![leftMaxX] = x
            egles!![leftMaxY] = y
        }
        if (y < egles!![topMaxY]) {
            egles!![topMaxX] = x
            egles!![topMaxY] = y
        }
        if (x > egles!![rightMaxX]) {
            egles!![rightMaxX] = x
            egles!![rightMaxY] = y
        }
        if (y > egles!![bottomMaxY]) {
            egles!![bottomMaxX] = x
            egles!![bottomMaxY] = y
        }
    }

    var lastX = 0f
    var lastY = 0f

    // A, B, C, and D in the equation x = a*t^3+b*t^2+c*t+d
    // or A, B, and C in the equation x = a*t^2+b*t+c
    val xCoeff = FloatArray(4)
    val yCoeff = FloatArray(4)

    val iterator = path.iterator()
    while (iterator.hasNext()) {
        when (val command = iterator.next()) {
            is Command.MoveTo -> {
                lastX = command.x
                lastY = command.y
            }

            is Command.Close -> {}

            else -> {
                if (egles == null) {
                    egles = floatArrayOf(
                        lastX, lastY, lastX, lastY,
                        lastX, lastY, lastX, lastY
                    )
                } else {
                    refreshSizes(lastX, lastY)
                }

                when (command) {
                    is Command.LineTo -> {
                        refreshSizes(command.x, command.y)
                        lastX = command.x
                        lastY = command.y
                    }

                    is Command.QuadTo -> {
                        // check the end point
                        refreshSizes(command.x, command.y)

                        // find the extrema
                        xCoeff[0] = lastX - 2 * command.x1 + command.x
                        xCoeff[1] = -2 * lastX + 2 * command.x1
                        xCoeff[2] = lastX
                        yCoeff[0] = lastY - 2 * command.y1 + command.y
                        yCoeff[1] = -2 * lastY + 2 * command.y1
                        yCoeff[2] = lastY

                        // x = a*t^2+b*t+c
                        // dx/dt = 0 = 2*a*t+b
                        // t = -b/(2a)
                        var t = -xCoeff[1] / (2 * xCoeff[0])
                        if (t > 0 && t < 1) {
                            val x = xCoeff[0] * t * t + xCoeff[1] * t + xCoeff[2]
                            if (x < egles[leftMaxX]) {
                                egles[leftMaxX] = x
                                egles[leftMaxY] = yCoeff[0] * t * t + yCoeff[1] * t + yCoeff[2]
                            }
                            if (x > egles[rightMaxX]) {
                                egles[rightMaxX] = x
                                egles[rightMaxY] = yCoeff[0] * t * t + yCoeff[1] * t + yCoeff[2]
                            }
                        }
                        t = -yCoeff[1] / (2 * yCoeff[0])
                        if (t > 0 && t < 1) {
                            val y = yCoeff[0] * t * t + yCoeff[1] * t + yCoeff[2]
                            if (y < egles[topMaxY]) {
                                egles[topMaxX] = xCoeff[0] * t * t + xCoeff[1] * t + xCoeff[2]
                                egles[topMaxY] = y
                            }
                            if (y > egles[bottomMaxY]) {
                                egles[bottomMaxX] = xCoeff[0] * t * t + xCoeff[1] * t + xCoeff[2]
                                egles[bottomMaxY] = y
                            }
                        }

                        lastX = command.x
                        lastY = command.y
                    }

                    is Command.CubicTo -> {
                        refreshSizes(command.x, command.y)

                        xCoeff[0] = -lastX + 3 * command.x1 - 3 * command.x2 + command.x
                        xCoeff[1] = 3 * lastX - 6 * command.x1 + 3 * command.x2
                        xCoeff[2] = -3 * lastX + 3 * command.x1
                        xCoeff[3] = lastX
                        yCoeff[0] = -lastY + 3 * command.y1 - 3 * command.y2 + command.y
                        yCoeff[1] = 3 * lastY - 6 * command.y1 + 3 * command.y2
                        yCoeff[2] = -3 * lastY + 3 * command.y1
                        yCoeff[3] = lastY

                        // x = a*t*t*t+b*t*t+c*t+d
                        // dx/dt = 3*a*t*t+2*b*t+c
                        // t = [-B+-sqrt(B^2-4*A*C)]/(2A)
                        // A = 3*a
                        // B = 2*b
                        // C = c
                        // t = (-2*b+-sqrt(4*b*b-12*a*c)]/(6*a)
                        var det = (4 * xCoeff[1] * xCoeff[1] - 12 * xCoeff[0] * xCoeff[2])
                        if (det < 0) {
                            // there are no solutions! nothing to do here
                        } else if (det == 0f) {
                            // there is 1 solution
                            val t = -2 * xCoeff[1] / (6 * xCoeff[0])
                            if (t > 0 && t < 1) {
                                val x = xCoeff[0] * t * t * t + xCoeff[1] * t * t + xCoeff[2] * t + xCoeff[3]
                                if (x < egles[leftMaxX]) {
                                    egles[leftMaxX] = x
                                    egles[leftMaxY] = yCoeff[0] * t * t * t + yCoeff[1] * t * t + yCoeff[2] * t + yCoeff[3]
                                }
                                if (x > egles[rightMaxX]) {
                                    egles[rightMaxX] = x
                                    egles[rightMaxY] = (yCoeff[0] * t * t * t) + yCoeff[1] * t * t + yCoeff[2] * t + yCoeff[3]
                                }
                            }
                        } else {
                            // there are 2 solutions:
                            det = sqrt(det.toDouble()).toFloat()
                            var t = (-2 * xCoeff[1] + det) / (6 * xCoeff[0])
                            if (t > 0 && t < 1) {
                                val x = xCoeff[0] * t * t * t + xCoeff[1] * t * t + xCoeff[2] * t + xCoeff[3]
                                if (x < egles[leftMaxX]) {
                                    egles[leftMaxX] = x
                                    egles[leftMaxY] = yCoeff[0] * t * t * t + yCoeff[1] * t * t + yCoeff[2] * t + yCoeff[3]
                                }
                                if (x > egles[rightMaxX]) {
                                    egles[rightMaxX] = x
                                    egles[rightMaxY] = (yCoeff[0] * t * t * t) + yCoeff[1] * t * t + yCoeff[2] * t + yCoeff[3]
                                }
                            }
                            t = (-2 * xCoeff[1] - det) / (6 * xCoeff[0])
                            if (t > 0 && t < 1) {
                                val x = xCoeff[0] * t * t * t + xCoeff[1] * t * t + xCoeff[2] * t + xCoeff[3]
                                if (x < egles[leftMaxX]) {
                                    egles[leftMaxX] = x
                                    egles[leftMaxY] = yCoeff[0] * t * t * t + yCoeff[1] * t * t + yCoeff[2] * t + yCoeff[3]
                                }
                                if (x > egles[rightMaxX]) {
                                    egles[rightMaxX] = x
                                    egles[rightMaxY] = (yCoeff[0] * t * t * t) + yCoeff[1] * t * t + yCoeff[2] * t + yCoeff[3]
                                }
                            }
                        }
                        det = (4 * yCoeff[1] * yCoeff[1] - 12 * yCoeff[0] * yCoeff[2])
                        if (det < 0) {
                            // there are no solutions! nothing to do here
                        } else if (det == 0f) {
                            // there is 1 solution
                            val t = -2 * yCoeff[1] / (6 * yCoeff[0])
                            if (t > 0 && t < 1) {
                                val y = yCoeff[0] * t * t * t + yCoeff[1] * t * t + yCoeff[2] * t + yCoeff[3]
                                if (y < egles[topMaxY]) {
                                    egles[topMaxX] = xCoeff[0] * t * t * t + xCoeff[1] * t * t + xCoeff[2] * t + xCoeff[3]
                                    egles[topMaxY] = y
                                }
                                if (y > egles[bottomMaxY]) {
                                    egles[bottomMaxX] = (xCoeff[0] * t * t * t) + xCoeff[1] * t * t + xCoeff[2] * t + xCoeff[3]
                                    egles[bottomMaxY] = y
                                }
                            }
                        } else {
                            // there are 2 solutions:
                            det = sqrt(det.toDouble()).toFloat()
                            var t = (-2 * yCoeff[1] + det) / (6 * yCoeff[0])
                            if (t > 0 && t < 1) {
                                val y = yCoeff[0] * t * t * t + yCoeff[1] * t * t + yCoeff[2] * t + yCoeff[3]
                                if (y < egles[topMaxY]) {
                                    egles[topMaxX] = xCoeff[0] * t * t * t + xCoeff[1] * t * t + xCoeff[2] * t + xCoeff[3]
                                    egles[topMaxY] = y
                                }
                                if (y > egles[bottomMaxY]) {
                                    egles[bottomMaxX] = (xCoeff[0] * t * t * t) + xCoeff[1] * t * t + xCoeff[2] * t + xCoeff[3]
                                    egles[bottomMaxY] = y
                                }
                            }
                            t = (-2 * yCoeff[1] - det) / (6 * yCoeff[0])
                            if (t > 0 && t < 1) {
                                val y = yCoeff[0] * t * t * t + yCoeff[1] * t * t + yCoeff[2] * t + yCoeff[3]
                                if (y < egles[topMaxY]) {
                                    egles[topMaxX] = xCoeff[0] * t * t * t + xCoeff[1] * t * t + xCoeff[2] * t + xCoeff[3]
                                    egles[topMaxY] = y
                                }
                                if (y > egles[bottomMaxY]) {
                                    egles[bottomMaxX] = (xCoeff[0] * t * t * t) + xCoeff[1] * t * t + xCoeff[2] * t + xCoeff[3]
                                    egles[bottomMaxY] = y
                                }
                            }
                        }

                        lastX = command.x
                        lastY = command.y
                    }

                    else -> throw RuntimeException()
                }
            }
        }
    }

    val points = egles ?: throw EmptyPathException()
    return Bounds(
        x = points[leftMaxX],
        y = points[topMaxY],
        w = points[rightMaxX] - points[leftMaxX],
        h = points[bottomMaxY] - points[topMaxY]
    )
}