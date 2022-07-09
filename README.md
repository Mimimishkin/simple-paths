# Simple SVG
[![](https://jitpack.io/v/Mimimishkin/simple-svg.svg)](https://jitpack.io/#Mimimishkin/simple-svg)

Very simple utilities for svg path

It cannot parse the compact form of the element *a*! (e.g "A1 1 0 **00**64 39")

## Samples

#### Svg paths

    val path = emptyPath.moveTo(9f, 0f).quadTo(12f, 14f, 20f, 22f).done()
    val path2 = path("M 9 0 Q 12 14 20 22")
    println(path.svgPath == path2.svgPath)
    
#### Standard elements
    
    val circle = circle(cx = 30f, cy = 30f, r = 100f)
    val rect = rect(x = 0f, y = 0f, width = 200f, heigth = 100f)
    val roundedRect = rect(x = 0f, y = 0f, width = 200f, heigth = 100f, rx = 20f, ry = 20f)
    
#### Java compatibility
    
    val shape = path.asJavaShape()
