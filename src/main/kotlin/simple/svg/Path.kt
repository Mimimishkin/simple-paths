package simple.svg

import simple.svg.Command.*

typealias Path = List<Command>

fun <T> Path.modified(
    pathMaker: (List<T>) -> Path,
    transform: (Command, lastX: Float?, lastY: Float?, nextX: Float, nextY: Float, moveToX: Float?, moveToY: Float?) -> T
): Path {
    var moveToX: Float? = null
    var moveToY: Float? = null
    var lastX: Float? = null
    var lastY: Float? = null

    return pathMaker(map {
        val prevX = lastX
        val prevY = lastY

        lastCoordinates(it, lastY ?: 0f, lastX ?: 0f, moveToX ?: 0f, moveToY ?: 0f).apply {
            lastX = first
            lastY = second
        }

        val modified = transform(it, prevX, prevY, lastX!!, lastY!!, moveToX, moveToY)

        if (it is MoveTo || it is MoveToRelative) {
            moveToX = lastX
            moveToY = lastY
        }

        modified
    })
}

val Path.absolute get() = modified({ it }) { c, lastX, lastY, _, _ , _, _ ->
    c.absolute(lastX ?: 0f, lastY ?: 0f)
}

val Path.relative get() = modified({ it }) { c, lastX, lastY, _, _, _, _ ->
    c.relative(lastX ?: 0f, lastY ?: 0f)
}

val Path.simplified get() = modified({ it.flatten() }) { c, lastX, lastY, _, _, _, _ ->
    c.simplified(lastX ?: 0f, lastY ?: 0f)
}

val Path.cleared get() = modified({ it.filterNotNull() }) { c, lastX, lastY, nextX, nextY, _, _ ->
    c.takeIf { lastX != nextX && lastY != nextY }
}

private fun lastCoordinates(
    it: Command,
    lastY: Float,
    lastX: Float,
    moveToX: Float,
    moveToY: Float
) = when (it) {
    is MoveTo -> it.x to it.y
    is MoveToRelative -> lastX + it.dx to lastY + it.dy
    is ArcTo -> it.x to it.y
    is ArcToRelative -> lastX + it.dx to lastY + it.dy
    is CubicTo -> it.x to it.y
    is CubicToRelative -> lastX + it.dx to lastY + it.dy
    is HorizontalLineTo -> it.x to lastY
    is HorizontalLineToRelative -> lastX + it.dx to lastY
    is LineTo -> it.x to it.y
    is LineToRelative -> lastX + it.dx to lastY + it.dy
    is QuadTo -> it.x to it.y
    is QuadToRelative -> lastX + it.dx to lastY + it.dy
    is SmoothCubicTo -> it.x to it.y
    is SmoothCubicToRelative -> lastX + it.dx to lastY + it.dy
    is SmoothQuadTo -> it.x to it.y
    is SmoothQuadToRelative -> lastX + it.dx to lastY + it.dy
    is VerticalLineTo -> lastX to it.y
    is VerticalLineToRelative -> lastX to lastY + it.dy
    is Close -> moveToX to moveToY
}

val Path.subPaths get() = splitInclusive(addToNext = false) { it == Close }

val List<Path>.fullPath get() = flatten()

val Path.svgPath get() = joinToString(" ") { it.svgPath }