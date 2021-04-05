package net.colors_wind.particleswarmoptimization

import java.lang.Math.pow
import java.lang.RuntimeException
import kotlin.math.*

fun Double.square() : Double = this * this

abstract class Question {
    abstract val fit: (Vector) -> Double
    abstract val dimension: Int
    abstract val bounds: Array<Bound>
    open val N = 20
    open val omega = 0.5
    open val c1 = 2.0
    open val c2 = 2.0
    open val Gmax = 2000
}

class Sphere : Question() {
    override val dimension = 5
    val bound = Bound(-100.0, 100.0)
    override val fit: (Vector) -> Double = { vector ->
        vector.sumBy { it * it }
    }
    override val bounds = Array(dimension) { bound }
}

class Schwefel(override val omega: Double=0.5, override val c1: Double=2.0, override val c2: Double=2.0) : Question() {
    override val dimension = 5
    val bound = Bound(-500.0, 500.0)
    override val fit: (Vector) -> Double = { vector ->
        vector.sumByWithIndex { _, x -> -x * sin(sqrt(abs(x)))} + dimension * 418.9829
    }
    override val bounds = Array(dimension) { bound }

}

