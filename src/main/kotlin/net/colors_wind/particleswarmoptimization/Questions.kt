package net.colors_wind.particleswarmoptimization

import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.sin
import kotlin.math.sqrt

fun Double.square() : Double = this * this

abstract class Question {
    abstract val fit: (Vector) -> Double
    abstract val dimension: Int
    abstract val bounds: Array<Bound>
    open val N = 40
    open fun omega(particles: Particles) = 0.5
    open val c1 = 2.0
    open val c2 = 2.0
    open val Gmax = 2000
}

class Sphere : Question() {
    override val dimension = 30
    val bound = Bound(-100.0, 100.0)
    override val fit: (Vector) -> Double = { vector ->
        vector.sumBy { it * it }
    }
    override val bounds = Array(dimension) { bound }
}

class Schwefel : Question() {
    override val dimension = 30
    val bound = Bound(-500.0, 500.0)
    override val fit: (Vector) -> Double = { vector ->
        vector.sumByWithIndex { i, x -> -x * sin(sqrt(abs(x))) + 418.9829 * i }
    }
    override val bounds = Array(dimension) { bound }
}

class Rosenbrock : Question() {
    override val dimension = 30
    val bound = Bound(-10.0, 10.0)
    override val fit: (Vector) -> Double = { vector ->
        var sum = 0.0
        for (i in 0 until vector.dimension-1) {
            sum += 100 * (vector[i + 1] - vector[i].square()).square() + (vector[i] - 1).square()

        }
        sum
    }
    override val bounds = Array(dimension) { bound }
}

class Easy : Question() {
    override fun omega(particles: Particles): Double {
        return 0.2 + 0.5 * particles.iterations
    }
    override val dimension = 2
    override val fit: (Vector) -> Double = { vector ->
        -(21.5 + vector[0] * sin(4 * PI * vector[0]) + vector[1] * sin(20 * PI * vector[1]))
    }
    override val bounds = arrayOf(Bound(-3.0,12.1), Bound(4.1, 5.8))

}