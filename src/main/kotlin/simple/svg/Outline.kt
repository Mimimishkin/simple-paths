package simple.svg

import simple.svg.CapMode.*
import simple.svg.JoinMode.*
import java.awt.BasicStroke
import java.awt.BasicStroke.*
import java.awt.Stroke

enum class CapMode {
    Butt, Round, Square
}

enum class JoinMode {
    Miter, Round, Bevel
}

fun BasicStroke(
    width: Float,
    cap: CapMode = Square,
    join: JoinMode = Miter,
    miterLimit: Float = 10f,
    dash: List<Float>? = null,
    dashPhase: Float = 0f,
): Stroke = BasicStroke(
    width,
    when (cap) {
        Butt -> CAP_BUTT
        CapMode.Round -> CAP_ROUND
        Square -> CAP_SQUARE
    },
    when (join) {
        Miter -> JOIN_MITER
        JoinMode.Round -> JOIN_ROUND
        Bevel -> JOIN_BEVEL
    },
    miterLimit,
    dash?.let { FloatArray(dash.size) { dash[it] } },
    dashPhase
)

fun Path.outline(stroke: Stroke) = fromShape(stroke.createStrokedShape(asShape()))

