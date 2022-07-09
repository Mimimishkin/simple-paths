package simple.svg

import java.awt.Shape
import java.awt.geom.*
import java.awt.geom.PathIterator.*

fun Path.asShape() = object : Shape {
    override fun getBounds2D() = this@asShape.bounds.run { Rectangle2D.Float(x, y, w, h) }

    override fun getBounds() = bounds2D.bounds

    override fun contains(x: Double, y: Double) = contains(x.toFloat(), y.toFloat())

    override fun contains(p: Point2D) = contains(p.x, p.y)

    override fun contains(x: Double, y: Double, w: Double, h: Double) = contains(x.toFloat(), y.toFloat(), w.toFloat(), h.toFloat())

    override fun contains(r: Rectangle2D) = contains(r.x, r.y, r.width, r.height)

    override fun intersects(x: Double, y: Double, w: Double, h: Double) = intersects(x.toFloat(), y.toFloat(), w.toFloat(), h.toFloat())

    override fun intersects(r: Rectangle2D) = intersects(r.x, r.y, r.width, r.height)

    override fun getPathIterator(at: AffineTransform?) = TransformedPathIterator(Iter(this@asShape), at)

    override fun getPathIterator(at: AffineTransform?, flatness: Double) = FlatteningPathIterator(getPathIterator(at), flatness)
}

fun fromShape(shape: Shape) = fromPathIterator(shape.getPathIterator(null))

fun fromPathIterator(iterator: PathIterator): Path {
    val path = emptyPath()

    val args = floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f)
    while (!iterator.isDone) {
        when(iterator.currentSegment(args)) {
            SEG_MOVETO -> path.moveTo(args[0], args[1])
            SEG_LINETO -> path.lineTo(args[0], args[1])
            SEG_QUADTO -> path.quadTo(args[0], args[1], args[2], args[3])
            SEG_CUBICTO -> path.cubicTo(args[0], args[1], args[2], args[3], args[4], args[5])
            SEG_CLOSE -> path.close()
        }
        iterator.next()
    }

    return path.done()
}

private class TransformedPathIterator(
    private val iterator: PathIterator,
    private val transform: AffineTransform?
) : PathIterator {
    override fun getWindingRule() = iterator.windingRule

    override fun isDone() = iterator.isDone

    override fun next() = iterator.next()

    override fun currentSegment(coords: FloatArray) = iterator.currentSegment(coords)
        .also { transform?.transform(coords, 0, coords, 0, coords.size / 2) }

    override fun currentSegment(coords: DoubleArray) = iterator.currentSegment(coords)
        .also { transform?.transform(coords, 0, coords, 0, coords.size / 2) }
}

private class Iter(path: Path) : PathIterator {
    val iterator = path.simplified.iterator()
    var current: Command? = null

    init {
        next()
    }

    override fun getWindingRule() = WIND_EVEN_ODD

    override fun isDone() = current == null

    override fun next() {
        current = try { iterator.next() } catch (_: NoSuchElementException) { null }
    }

    override fun currentSegment(coords: FloatArray): Int {
        current!!.arguments.forEachIndexed { i, arg -> coords[i] = arg }
        return when (current!!.type) {
            CommandType.LineTo -> SEG_LINETO
            CommandType.MoveTo -> SEG_MOVETO
            CommandType.QuadTo -> SEG_QUADTO
            CommandType.CubicTo -> SEG_CUBICTO
            CommandType.Close -> SEG_CUBICTO
            else -> throw RuntimeException()
        }
    }

    override fun currentSegment(coords: DoubleArray): Int {
        current!!.arguments.forEachIndexed { i, arg -> coords[i] = arg.toDouble() }
        return when (current!!.type) {
            CommandType.LineTo -> SEG_LINETO
            CommandType.MoveTo -> SEG_MOVETO
            CommandType.QuadTo -> SEG_QUADTO
            CommandType.CubicTo -> SEG_CUBICTO
            CommandType.Close -> SEG_CUBICTO
            else -> throw RuntimeException()
        }
    }
}