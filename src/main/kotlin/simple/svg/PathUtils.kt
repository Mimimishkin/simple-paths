package simple.svg

import java.awt.geom.Path2D
import java.awt.geom.PathIterator

val Path.bounds get() = pathBounds(this)

private val Path.iter: PathIterator get() = asShape().getPathIterator(null)

fun Path.contains(x: Float, y: Float) =
    Path2D.contains(iter, x.toDouble(), y.toDouble())

fun Path.contains(x: Float, y: Float, w: Float, h: Float) =
    Path2D.contains(iter, x.toDouble(), y.toDouble(), w.toDouble(), h.toDouble())

fun Path.intersects(x: Float, y: Float, w: Float, h: Float) =
    Path2D.intersects(iter, x.toDouble(), y.toDouble(), w.toDouble(), h.toDouble())