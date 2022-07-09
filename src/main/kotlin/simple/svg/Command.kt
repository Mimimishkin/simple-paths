package simple.svg

import simple.svg.Command.*

sealed class Command(val type: CommandType) {
    data class MoveTo(
        val x: Float,
        val y: Float
    ) : Command(CommandType.MoveTo)

    data class MoveToRelative(
        val dx: Float,
        val dy: Float
    ) : Command(CommandType.MoveToRelative)

    data class LineTo(
        val x: Float,
        val y: Float
    ) : Command(CommandType.LineTo)

    data class LineToRelative(
        val dx: Float,
        val dy: Float
    ) : Command(CommandType.LineToRelative)

    data class VerticalLineTo(
        val y: Float
    ) : Command(CommandType.VerticalLineTo)

    data class VerticalLineToRelative(
        val dy: Float
    ) : Command(CommandType.VerticalLineToRelative)

    data class HorizontalLineTo(
        val x: Float
    ) : Command(CommandType.HorizontalLineTo)

    data class HorizontalLineToRelative(
        val dx: Float
    ) : Command(CommandType.HorizontalLineToRelative)

    data class QuadTo(
        val x1: Float,
        val y1: Float,
        val x: Float,
        val y: Float,
    ) : Command(CommandType.QuadTo)

    data class QuadToRelative(
        val dx1: Float,
        val dy1: Float,
        val dx: Float,
        val dy: Float,
    ) : Command(CommandType.QuadToRelative)

    data class CubicTo(
        val x1: Float,
        val y1: Float,
        val x2: Float,
        val y2: Float,
        val x: Float,
        val y: Float,
    ) : Command(CommandType.CubicTo)

    data class CubicToRelative(
        val dx1: Float,
        val dy1: Float,
        val dx2: Float,
        val dy2: Float,
        val dx: Float,
        val dy: Float,
    ) : Command(CommandType.CubicToRelative)

    data class SmoothQuadTo(
        val x: Float,
        val y: Float,
    ) : Command(CommandType.SmoothQuadTo)

    data class SmoothQuadToRelative(
        val dx: Float,
        val dy: Float,
    ) : Command(CommandType.SmoothQuadToRelative)

    data class SmoothCubicTo(
        val x2: Float,
        val y2: Float,
        val x: Float,
        val y: Float,
    ) : Command(CommandType.SmoothCubicTo)

    data class SmoothCubicToRelative(
        val dx2: Float,
        val dy2: Float,
        val dx: Float,
        val dy: Float,
    ) : Command(CommandType.SmoothCubicToRelative)

    data class ArcTo(
        val rx: Float,
        val ry: Float,
        val xAxisRotation: Float,
        val largeArcFlag: Boolean,
        val sweepFlag: Boolean,
        val x: Float,
        val y: Float,
    ) : Command(CommandType.ArcTo)

    data class ArcToRelative(
        val rx: Float,
        val ry: Float,
        val xAxisRotation: Float,
        val largeArcFlag: Boolean,
        val sweepFlag: Boolean,
        val dx: Float,
        val dy: Float,
    ) : Command(CommandType.ArcToRelative)

    object Close : Command(CommandType.Close)
}

enum class CommandType(
    val argumentsCount: Int,
    val symbol: Char,
    val next: CommandType,
    val makeCommand: (List<Float>) -> Command
) {
    LineTo(2, 'L', LineTo, { LineTo(it[0], it[1]) }),

    LineToRelative(2, 'l', LineToRelative, { LineToRelative(it[0], it[1]) }),

    MoveTo(2, 'M', LineTo, { MoveTo(it[0], it[1]) }),

    MoveToRelative(2, 'm', LineToRelative, { MoveToRelative(it[0], it[1]) }),

    VerticalLineTo(1, 'V', VerticalLineTo, { VerticalLineTo(it[0]) }),

    VerticalLineToRelative(1, 'v', VerticalLineToRelative, { VerticalLineToRelative(it[0]) }),

    HorizontalLineTo(1, 'H', HorizontalLineTo, { HorizontalLineTo(it[0]) }),

    HorizontalLineToRelative(1, 'h', HorizontalLineToRelative, { HorizontalLineToRelative(it[0]) }),

    QuadTo(4, 'Q', QuadTo, { QuadTo(it[0], it[1], it[2], it[3]) }),

    QuadToRelative(4, 'q', QuadToRelative, { QuadToRelative(it[0], it[1], it[2], it[3]) }),

    CubicTo(6, 'C', CubicTo, { CubicTo(it[0], it[1], it[2], it[3], it[4], it[5]) }),

    CubicToRelative(6, 'c', CubicToRelative, { CubicToRelative(it[0], it[1], it[2], it[3], it[4], it[5]) }),

    SmoothQuadTo(2, 'T', SmoothQuadTo, { SmoothQuadTo(it[0], it[1]) }),

    SmoothQuadToRelative(2, 't', SmoothQuadToRelative, { SmoothQuadToRelative(it[0], it[1]) }),

    SmoothCubicTo(4, 'S', SmoothCubicTo, { SmoothCubicTo(it[0], it[1], it[2], it[3]) }),

    SmoothCubicToRelative(4, 's', SmoothCubicToRelative, {
        SmoothCubicToRelative(
            it[0],
            it[1],
            it[2],
            it[3]
        )
    }),

    ArcTo(7, 'A', ArcTo, { ArcTo(it[0], it[1], it[2], it[3] == 1f, it[4] == 1f, it[5], it[6]) }),

    ArcToRelative(7, 'a', ArcToRelative, {
        ArcToRelative(
            it[0],
            it[1],
            it[2],
            it[3] == 1f,
            it[4] == 1f,
            it[5],
            it[6]
        )
    }),

    Close(0, 'Z', MoveTo, { Command.Close }),
}

val Command.arguments: List<Float> get() = when(this) {
    is ArcTo -> listOf(rx, ry, xAxisRotation, if (largeArcFlag) 1f else 0f, if (sweepFlag) 1f else 0f, x, y)
    is ArcToRelative -> listOf(rx, ry, xAxisRotation, if (largeArcFlag) 1f else 0f, if (sweepFlag) 1f else 0f, dx, dy)
    is Close -> listOf()
    is CubicTo -> listOf(x1, y1, x2, y2, x, y)
    is CubicToRelative -> listOf(dx1, dy1, dx2, dy2, dx, dy)
    is HorizontalLineTo -> listOf(x)
    is HorizontalLineToRelative -> listOf(dx)
    is LineTo -> listOf(x, y)
    is LineToRelative -> listOf(dx, dy)
    is MoveTo -> listOf(x, y)
    is MoveToRelative -> listOf(dx, dy)
    is QuadTo -> listOf(x1, y1, x, y)
    is QuadToRelative -> listOf(dx1, dy1, dx, dy)
    is SmoothCubicTo -> listOf(x2, y2, x, y)
    is SmoothCubicToRelative -> listOf(dx2, dy2, dx, dy)
    is SmoothQuadTo -> listOf(x, y)
    is SmoothQuadToRelative -> listOf(dx, dy)
    is VerticalLineTo -> listOf(y)
    is VerticalLineToRelative -> listOf(dy)
}

fun ArcTo.curves(lastX: Float, lastY: Float) = arcToCurves(
    p1 = Vec2(lastX.toDouble(), lastY.toDouble()),
    p2 = Vec2(x.toDouble(), y.toDouble()),
    r = Vec2(rx.toDouble(), ry.toDouble()),
    phi = xAxisRotation.toDouble(),
    largeArc = largeArcFlag,
    sweep = sweepFlag
)

fun Command.absolute(lastX: Float, lastY: Float) = when(this) {
    is ArcToRelative -> ArcTo(rx, ry, xAxisRotation, largeArcFlag, sweepFlag, lastX + dx, lastY + dy)
    is CubicToRelative -> CubicTo(lastX + dx1, lastY + dy1, lastX + dx2, lastY + dy2, lastX + dx, lastY + dy)
    is HorizontalLineToRelative -> HorizontalLineTo(lastX + dx)
    is LineToRelative -> LineTo(lastX + dx, lastY + dy)
    is MoveToRelative -> MoveTo(lastX + dx, lastY + dy)
    is QuadToRelative -> QuadTo(lastX + dx1, lastY + dy1, lastX + dx, lastY + dy)
    is SmoothCubicToRelative -> SmoothCubicTo(lastX + dx2, lastY + dy2, lastX + dx, lastY + dy)
    is SmoothQuadToRelative -> SmoothQuadTo(lastX + dx, lastY + dy)
    is VerticalLineToRelative -> VerticalLineTo(lastY + dy)
    else -> this
}

fun Command.relative(lastX: Float, lastY: Float) = when(this) {
    is ArcTo -> ArcToRelative(rx, ry, xAxisRotation, largeArcFlag, sweepFlag, x - lastX, y - lastY)
    is CubicTo -> CubicToRelative(x1 - lastX, y1 - lastY, x2 - lastX, y2 - lastY, x - lastX, y - lastY)
    is HorizontalLineTo -> HorizontalLineToRelative(x - lastX)
    is LineTo -> LineToRelative(x - lastX, y - lastY)
    is MoveTo -> MoveToRelative(x - lastX, y - lastY)
    is QuadTo -> QuadToRelative(x1 - lastX, y1 - lastY, x - lastX, y - lastY)
    is SmoothCubicTo -> SmoothCubicToRelative(x2 - lastX, y2 - lastY, x - lastX, y - lastY)
    is SmoothQuadTo -> SmoothQuadToRelative(x - lastX, y - lastY)
    is VerticalLineTo -> VerticalLineToRelative(y - lastY)
    else -> this
}

fun Command.simplified(lastX: Float, lastY: Float): List<Command> =
    absolute(lastX, lastY).let {
        when(it) {
            is VerticalLineTo -> listOf(LineTo(lastX, it.y))
            is HorizontalLineTo -> listOf(LineTo(it.x, lastY))
            is SmoothQuadTo -> listOf(QuadTo(lastX, lastY, it.x, it.y))
            is SmoothCubicTo -> listOf(CubicTo(lastX, lastY, it.x2, it.y2, it.x, it.y))
            is ArcTo -> it.curves(lastX, lastY)
            else -> listOf(this)
        }
    }

val Command.svgPath get() = when (this) {
    is ArcTo -> "A $rx $ry $xAxisRotation ${if (largeArcFlag) 1 else 0} ${if (sweepFlag) 1 else 0} $x $y"
    is ArcToRelative -> "a $rx $ry $xAxisRotation ${if (largeArcFlag) 1 else 0} ${if (sweepFlag) 1 else 0} $dx $dy"
    is Close -> "Z"
    is CubicTo -> "C $x1 $y1 $x2 $y2 $x $y"
    is CubicToRelative -> "c $dx1 $dy1 $dx2 $dy2 $dx $dy"
    is HorizontalLineTo -> "H $x"
    is HorizontalLineToRelative -> "h $dx"
    is LineTo -> "L $x $y"
    is LineToRelative -> "l $dx $dy"
    is MoveTo -> "M $x $y"
    is MoveToRelative -> "m $dx $dy"
    is QuadTo -> "Q $x1 $y1 $x $y"
    is QuadToRelative -> "q $dx1 $dy1 $dx $dy"
    is SmoothCubicTo -> "S $x2 $y2 $x $y"
    is SmoothCubicToRelative -> "s $dx2 $dy2 $dx $dy"
    is SmoothQuadTo -> "T $x $y"
    is SmoothQuadToRelative -> "t $dx $dy"
    is VerticalLineTo -> "V $y"
    is VerticalLineToRelative -> "v $dy"
}