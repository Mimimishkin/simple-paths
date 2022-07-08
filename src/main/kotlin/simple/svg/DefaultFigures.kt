package simple.svg

fun rect(x: Float, y: Float, width: Float, height: Float, rx: Float = 0f, ry: Float = 0f) = emptyPath
    .moveTo(x + rx, y)
    .horizontalLineToRelative(width - rx * 2)
    .arcToRelative(rx, ry, 0f, false, true, rx, ry)
    .verticalLineToRelative(height - ry * 2)
    .arcToRelative(rx, ry, 0f, false, true, -rx, ry)
    .horizontalLineToRelative(-(width - rx * 2))
    .arcToRelative(rx, ry, 0f, false, true, -rx, -ry)
    .verticalLineToRelative(-(height - ry * 2))
    .arcToRelative(rx, ry, 0f, false, true, rx, -ry)
    .close()
    .done()

fun circle(cx: Float, cy: Float, r: Float) = ellipse(cx, cy, r, r)

fun ellipse(cx: Float, cy: Float, rx: Float, ry: Float) = emptyPath
    .moveTo(cx - rx, cy)
    .arcToRelative(rx, ry, 0f, false, false, rx * 2, 0f)
    .arcToRelative(rx, ry, 0f, false, false, -rx * 2, 0f)
    .done()

fun line(x1: Float, y1: Float, x2: Float, y2: Float) = emptyPath
    .moveTo(x1, y1)
    .lineTo(x2, y2)
    .done()

fun polyline(vararg points: Pair<Float, Float>) = emptyPath
    .also { points.forEach { (x, y) -> it.lineOrMoveTo(x, y) } }
    .done()

fun polygon(vararg points: Pair<Float, Float>) = emptyPath
    .also { points.forEach { (x, y) -> it.lineOrMoveTo(x, y) } }
    .close()
    .done()

fun path(d: String) = parsePath(d)