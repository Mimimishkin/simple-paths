package simple.svg

import simple.svg.Command.*

typealias MutablePath = MutableList<Command>

val emptyPath: MutablePath get() = mutableListOf()

fun MutablePath.done(): Path = this

fun MutablePath.moveTo(x: Float, y: Float) =
    also { it += MoveTo(x, y) }

fun MutablePath.moveToRelative(dx: Float, dy: Float) =
    also { it += MoveToRelative(dx, dy) }

fun MutablePath.lineTo(x: Float, y: Float) =
    also { it += LineTo(x, y) }

fun MutablePath.lineOrMoveTo(x: Float, y: Float) =
    also { if (it.isEmpty()) it += MoveTo(x, y) else it += LineTo(x, y) }

fun MutablePath.lineToRelative(dx: Float, dy: Float) =
    also { it += LineToRelative(dx, dy) }

fun MutablePath.verticalLineTo(y: Float) =
    also { it += VerticalLineTo(y) }

fun MutablePath.verticalLineToRelative(dy: Float) =
    also { it += VerticalLineToRelative(dy) }

fun MutablePath.horizontalLineTo(x: Float) =
    also { it += HorizontalLineTo(x) }

fun MutablePath.horizontalLineToRelative(dx: Float) =
    also { it += HorizontalLineToRelative(dx) }

fun MutablePath.quadTo(x1: Float, y1: Float, x: Float, y: Float) =
    also { it += QuadTo(x1, y1, x, y) }

fun MutablePath.quadToRelative(dx1: Float, dy1: Float, dx: Float, dy: Float) =
    also { it += QuadToRelative(dx1, dy1, dx, dy) }

fun MutablePath.cubicTo(x1: Float, y1: Float, x2: Float, y2: Float, x: Float, y: Float) =
    also { it += CubicTo(x1, y1, x2, y2, x, y) }

fun MutablePath.cubicToRelative(dx1: Float, dy1: Float, dx2: Float, dy2: Float, dx: Float, dy: Float) =
    also { it += CubicToRelative(dx1, dy1, dx2, dy2, dx, dy) }

fun MutablePath.smoothQuadTo(x: Float, y: Float) =
    also { it += SmoothQuadTo(x, y) }

fun MutablePath.smoothQuadToRelative(dx: Float, dy: Float) =
    also { it += SmoothQuadToRelative(dx, dy) }

fun MutablePath.smoothCubicTo(x2: Float, y2: Float, x: Float, y: Float) =
    also { it += SmoothCubicTo(x2, y2, x, y) }

fun MutablePath.smoothCubicToRelative(dx2: Float, dy2: Float, dx: Float, dy: Float) =
    also { it += SmoothCubicToRelative(dx2, dy2, dx, dy) }

fun MutablePath.arcTo(rx: Float, ry: Float, xAxisRotation: Float, largeArcFlag: Boolean, sweepFlag: Boolean, x: Float, y: Float,) =
    also { it += ArcTo(rx, ry, xAxisRotation, largeArcFlag, sweepFlag, x, y) }

fun MutablePath.arcToRelative(rx: Float, ry: Float, xAxisRotation: Float, largeArcFlag: Boolean, sweepFlag: Boolean, dx: Float, dy: Float,) =
    also { it += ArcToRelative(rx, ry, xAxisRotation, largeArcFlag, sweepFlag, dx, dy) }

fun MutablePath.close() =
    also { it += Close }