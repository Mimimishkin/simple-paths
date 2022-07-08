package simple.svg

import java.awt.Shape
import java.awt.geom.*

fun Path.asJavaShape() = object : Shape {
    override fun getPathIterator(at: AffineTransform?) =
        TransformedPathIterator(Iter(this@asJavaShape), at)

    override fun getBounds2D() = ShapeBounds.getBounds(getPathIterator(null))

    override fun getBounds() = bounds2D.bounds

    override fun contains(x: Double, y: Double) = Path2D.contains(getPathIterator(null), x, y)

    override fun contains(p: Point2D?) = Path2D.contains(getPathIterator(null), p)

    override fun contains(x: Double, y: Double, w: Double, h: Double) = Path2D.contains(getPathIterator(null), x, y, w, h)

    override fun contains(r: Rectangle2D?) = Path2D.contains(getPathIterator(null), r)

    override fun intersects(x: Double, y: Double, w: Double, h: Double) = Path2D.intersects(getPathIterator(null), x, y, w, h)

    override fun intersects(r: Rectangle2D?) = Path2D.intersects(getPathIterator(null), r)

    override fun getPathIterator(at: AffineTransform?, flatness: Double) =
        FlatteningPathIterator(getPathIterator(at), flatness)
}

private class TransformedPathIterator(
    private val iterator: PathIterator,
    private val transform: AffineTransform?
) : PathIterator {
    override fun getWindingRule() = iterator.windingRule

    override fun isDone() = iterator.isDone

    override fun next() = iterator.next()

    override fun currentSegment(coords: FloatArray) = iterator.currentSegment(coords)
        .also { transform?.transform(coords, 0, coords, 0, coords.size) }

    override fun currentSegment(coords: DoubleArray) = iterator.currentSegment(coords)
        .also { transform?.transform(coords, 0, coords, 0, coords.size) }
}

private class Iter(path: Path) : PathIterator {
    val iterator = path.simplified.iterator()
    var current = iterator.next()

    override fun getWindingRule() = PathIterator.WIND_EVEN_ODD

    override fun isDone() = !iterator.hasNext()

    override fun next() {
        current = iterator.next()
    }

    override fun currentSegment(coords: FloatArray): Int {
        current.arguments.forEachIndexed { i, arg -> coords[i] = arg }
        return when (current.type) {
            CommandType.LineTo -> PathIterator.SEG_LINETO
            CommandType.MoveTo -> PathIterator.SEG_MOVETO
            CommandType.QuadTo -> PathIterator.SEG_QUADTO
            CommandType.CubicTo -> PathIterator.SEG_CUBICTO
            CommandType.Close -> PathIterator.SEG_CUBICTO
            else -> throw RuntimeException()
        }
    }

    override fun currentSegment(coords: DoubleArray): Int {
        current.arguments.forEachIndexed { i, arg -> coords[i] = arg.toDouble() }
        return when (current.type) {
            CommandType.LineTo -> PathIterator.SEG_LINETO
            CommandType.MoveTo -> PathIterator.SEG_MOVETO
            CommandType.QuadTo -> PathIterator.SEG_QUADTO
            CommandType.CubicTo -> PathIterator.SEG_CUBICTO
            CommandType.Close -> PathIterator.SEG_CUBICTO
            else -> throw RuntimeException()
        }
    }
}