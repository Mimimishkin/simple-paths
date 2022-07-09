package simple.svg

import simple.svg.Command.*
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

val Path.bounds get() =
    asShape().bounds2D

fun Path.contains(x: Float, y: Float) =
    asShape().contains(x.toDouble(), y.toDouble())

fun Path.contains(x: Float, y: Float, w: Float, h: Float) =
    asShape().contains(x.toDouble(), y.toDouble(), w.toDouble(), h.toDouble())

fun Path.intersects(x: Float, y: Float, w: Float, h: Float) =
    asShape().intersects(x.toDouble(), y.toDouble(), w.toDouble(), h.toDouble())