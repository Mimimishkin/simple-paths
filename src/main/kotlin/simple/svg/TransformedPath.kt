package simple.svg

import java.awt.geom.AffineTransform

fun Path.transformed(transform: AffineTransform): Path = simplified.map { command ->
    var list = command.arguments
    val args = FloatArray(command.type.argumentsCount) { list[it] }
    transform.transform(args, 0 , args, 0, args.size / 2)
    list = args.toList()

    command.type.makeCommand(list)
}

fun Path.translated(tx: Float = 0f, ty: Float = 0f) =
    transformed(AffineTransform.getTranslateInstance(tx.toDouble(), ty.toDouble()))

fun Path.scaled(sx: Float, sy: Float = sx) =
    transformed(AffineTransform.getScaleInstance(sx.toDouble(), sy.toDouble()))

fun Path.rotated(theta: Double, cx: Float = 0f, cy: Float = 0f) =
    transformed(AffineTransform.getRotateInstance(theta, cx.toDouble(), cy.toDouble()))

fun Path.sheared(shx: Float, shy: Float) =
    transformed(AffineTransform.getShearInstance(shx.toDouble(), shy.toDouble()))