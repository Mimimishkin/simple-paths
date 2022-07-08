package simple.svg

import simple.svg.Command.*
import java.util.Collections

typealias Path = List<Command>

val Path.absolute: Path get() {
    var moveToX = 0f
    var moveToY = 0f
    var lastX = 0f
    var lastY = 0f

    return map {
        val absolute = it.absolute(lastX, lastY)

        lastCoordinates(it, lastY, lastX, moveToX, moveToY).apply {
            lastX = first
            lastY = second
        }

        if (it is MoveTo || it is MoveToRelative) {
            moveToX = lastX
            moveToY = lastY
        }

        absolute
    }
}

val Path.relative: Path get() {
    var moveToX = 0f
    var moveToY = 0f
    var lastX = 0f
    var lastY = 0f

    return map {
        val relative = it.relative(lastX, lastY)

        lastCoordinates(it, lastY, lastX, moveToX, moveToY).apply {
            lastX = first
            lastY = second
        }

        if (it is MoveTo || it is MoveToRelative) {
            moveToX = lastX
            moveToY = lastY
        }

        relative
    }
}

val Path.simplified: Path get() {
    var moveToX = 0f
    var moveToY = 0f
    var lastX = 0f
    var lastY = 0f

    return absolute.flatMap {
        val simplified = with(it) { when(this) {
            is VerticalLineTo -> listOf(LineTo(lastX, y))
            is HorizontalLineTo -> listOf(LineTo(x, lastY))
            is SmoothQuadTo -> listOf(QuadTo(lastX, lastY, x, y))
            is SmoothCubicTo -> listOf(CubicTo(lastX, lastY, x2, y2, x, y))
            is ArcTo -> curves(lastX, lastY)
            else -> listOf(this)
        } }

        lastCoordinates(it, lastY, lastX, moveToX, moveToY).apply {
            lastX = first
            lastY = second
        }

        if (it is MoveTo) {
            moveToX = lastX
            moveToY = lastY
        }

        simplified
    }
}

private fun lastCoordinates(
    it: Command,
    lastY: Float,
    lastX: Float,
    moveToX: Float,
    moveToY: Float
) = when (it) {
    is MoveTo -> it.x to it.y
    is MoveToRelative -> it.dx to it.dy
    is ArcTo -> it.x to it.y
    is ArcToRelative -> it.dx to it.dy
    is CubicTo -> it.x to it.y
    is CubicToRelative -> it.dx to it.dy
    is HorizontalLineTo -> it.x to lastY
    is HorizontalLineToRelative -> it.dx to lastY
    is LineTo -> it.x to it.y
    is LineToRelative -> it.dx to it.dy
    is QuadTo -> it.x to it.y
    is QuadToRelative -> it.dx to it.dy
    is SmoothCubicTo -> it.x to it.y
    is SmoothCubicToRelative -> it.dx to it.dy
    is SmoothQuadTo -> it.x to it.y
    is SmoothQuadToRelative -> it.dx to it.dy
    is VerticalLineTo -> lastX to it.y
    is VerticalLineToRelative -> lastX to it.dy
    is Close -> moveToX to moveToY
}

val Path.subPaths get() = splitInclusive(addToNext = false) { it == Close }

val List<Path>.fullPath get() = flatten()

val Path.reversed: Path get() {
    val originalPaths = simplified.subPaths

    val reversedPaths = originalPaths.asReversed().map { commands ->
        val allArguments = commands.flatMap { it.arguments }.asReversed()
        val  types = commands
            .map { it.type }
            .splitInclusive { it == CommandType.MoveTo }
            .asReversed()
            .map { it.asReversed() }
            .map { if (it[0] == CommandType.Close) it.drop(1) + CommandType.Close else it }
            .flatMap { listOf(CommandType.MoveTo) + it.dropLast(1) }

        var used = 0
        types.map { type ->
            val range = used.. (used + type.argumentsCount)
            used += type.argumentsCount
            type.makeCommand(allArguments.slice(range))
        }
    }

    return reversedPaths.fullPath
}

val Path.svgPath get() = joinToString(" ") { it.svgPath }