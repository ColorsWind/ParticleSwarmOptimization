package net.colors_wind.particleswarmoptimization

import java.lang.Math.pow
import java.lang.RuntimeException
import kotlin.math.*

fun Double.square() : Double = this * this

abstract class Question {
    abstract val fit: (Vector) -> Double
    abstract val dimension: Int
    abstract val bounds: Array<Bound>
    open val N = 40
    open fun omega(particle: Particle) : Double {
        throw RuntimeException()
    }
    open fun connectNum(particles: Particles) : Int {
        return (0.4 * N * particles.iterations.toDouble() / Gmax.toDouble()).toInt() + 3
    }
    open val c1 = 1.4
    open val c2 = 1.4
    open val Gmax = 1000
}

class Sphere : Question() {
    override val dimension = 30
    val bound = Bound(-100.0, 100.0)
    override val fit: (Vector) -> Double = { vector ->
        vector.sumBy { it * it }
    }
    override val bounds = Array(dimension) { bound }
    override fun omega(particle: Particle): Double {
        return 0.2 + (0.9 - 0.2) * particle.rank.toDouble() / N.toDouble()
    }
}

class Schwefel : Question() {
    override val dimension = 30
    val bound = Bound(-500.0, 500.0)
    override val fit: (Vector) -> Double = { vector ->
        vector.sumByWithIndex { _, x -> -x * sin(sqrt(abs(x)))} + dimension * 418.9829
    }
    override val bounds = Array(dimension) { bound }
    override fun omega(particle: Particle): Double {
        return 0.4 + (0.9 - 0.4) * particle.rank.toDouble() / N.toDouble()
    }
}

class Rosenbrock : Question() {
    override val dimension = 5
    val bound = Bound(-10.0, 10.0)
    override val fit: (Vector) -> Double = { vector ->
        var sum = 0.0
        for (i in 0 until vector.dimension-1) {
            sum += 100 * (vector[i + 1] - vector[i].square()).square() + (vector[i] - 1).square()

        }
        sum
    }
    override val bounds = Array(dimension) { bound }
    override fun omega(particle: Particle): Double {
        return 0.2 + (0.9 - 0.2) * particle.rank.toDouble() / N.toDouble()
    }
}

class Easy : Question() {
    // max 0.9 min 0.2
    override fun omega(particle: Particle): Double {
        return 0.4 + (0.9 - 0.4) * particle.rank.toDouble() / N.toDouble()
    }
    override val dimension = 2
    override val fit: (Vector) -> Double = { vector ->
        -(21.5 + vector[0] * sin(4 * PI * vector[0]) + vector[1] * sin(20 * PI * vector[1]))
    }
    override val bounds = arrayOf(Bound(-3.0,12.1), Bound(4.1, 5.8))

}