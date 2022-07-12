# Simple Paths
[![](https://jitpack.io/v/Mimimishkin/simple-svg.svg)](https://jitpack.io/#Mimimishkin/simple-svg)

Very simple utilities for svg path

It cannot parse the compact form of the element *a*! (e.g "A1 1 0 **00**64 39")

## Samples

#### Svg paths

    val path = emptyPath.moveTo(9f, 0f).quadTo(12f, 14f, 20f, 22f).done()
    val path2 = path("M 9 0 Q 12 14 20 22")
    println(path.svgPath == path2.svgPath)

#### Operations with path

    val path = path("M18 32q-4-24 14-16L55 31q-2 13-16 12T30 54c6 8-8 15-16 6S-1 40 10 41a1 1 0 0012-6Z")
    val relative = path.relative // relative path
    val absolute = path.absolute // absolute path
    val simplified = path.simplified // path with only "M", "L", "Q", "C", "Z"
    val cleared = path.cleared // path without redundant elements (e.g "M 0 0 L 1 1 L 0 0 Z" - Z is unnecessary)
    val reversed = path.reversed // reversed path
    
#### Standard elements
    
    val circle = circle(cx = 30f, cy = 30f, r = 100f)
    val rect = rect(x = 0f, y = 0f, width = 200f, heigth = 100f)
    val roundedRect = rect(x = 0f, y = 0f, width = 200f, heigth = 100f, rx = 20f, ry = 20f)
    
#### Java compatibility
    
    val shape = path.asShape()
    val path = fromShape(shape)
