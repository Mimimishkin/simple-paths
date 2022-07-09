package simple.svg

import simple.svg.Command.*

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

    return flatMap {
        val simplified = it.simplified(lastX, lastY)

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

val Path.cleared: Path get() {
    var moveToX: Float? = null
    var moveToY: Float? = null
    var lastX: Float? = null
    var lastY: Float? = null

    return mapNotNull { command ->
        val prevX = lastX
        val prevY = lastY

        lastCoordinates(command, lastY ?: 0f, lastX ?: 0f, moveToX ?: 0f, moveToY ?: 0f).apply {
            lastX = first
            lastY = second
        }

        if (command is MoveTo) {
            moveToX = lastX
            moveToY = lastY
        }

        command.takeIf { prevX != lastX || prevY != lastY }
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

val Path.reversed: Path get() {
    val originalPaths = simplified.subPaths

    val reversedPaths = originalPaths.asReversed().map { commands ->
        val allArguments = commands.flatMap { it.arguments }.asReversed()
        val  types = commands
            .map { it.type }
            .splitInclusive { it == CommandType.MoveTo }
            .asReversed()
            .map { it.asReversed() }
            .map { if (it[0] == CommandType.Close) it.toMutableList().apply { add(lastIndex, CommandType.Close) }.drop(1) else it }
            .flatMap { listOf(CommandType.MoveTo) + it.dropLast(1) }

        var used = 0
        types.map { type ->
            val range = used until  (used + type.argumentsCount)
            used += type.argumentsCount
            type.makeCommand(allArguments.slice(range))
        }
    }

    return reversedPaths.fullPath
}

val Path.svgPath get() = joinToString(" ") { it.svgPath }