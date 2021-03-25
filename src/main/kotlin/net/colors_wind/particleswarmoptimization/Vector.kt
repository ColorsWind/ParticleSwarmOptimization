package net.colors_wind.particleswarmoptimization

import kotlin.random.Random

data class Bound(val lowerBound: Double, val upperBound: Double) {
    operator fun invoke(value: Double) =
        when {
            value < lowerBound -> lowerBound
            value > upperBound -> upperBound
            else -> value
        }
}
class Vector(private val array: DoubleArray, private val bounds: Array<Bound>) {
    val dimension : Int
        get() = array.size

    constructor(bounds: Array<Bound>) : this(DoubleArray(bounds.size), bounds) {
        for ((index,bound) in array.indices zip bounds) {
            array[index] = Random.nextDouble(bound.lowerBound, bound.upperBound)
        }
    }

    operator fun get(index: Int) = array[index]
    operator fun set(index: Int, value: Double) {
        array[index] = bounds[index](value)
    }

    operator fun unaryPlus() : Vector = this.copy()
    operator fun plusAssign(other: Vector) = array.indices.forEach { this[it] =+ other[it] }
    operator fun plus(other: Vector) = this.copy().also { it += other }

    operator fun unaryMinus() = copy().also { array.indices.forEach { this[it] = -this[it] } }
    operator fun minusAssign(other: Vector) = array.indices.forEach { this[it] =- other[it] }
    operator fun minus(other: Vector) = copy().also { it-=other }

    operator fun timesAssign(k: Double) = array.indices.forEach { this[it] *= k }
    operator fun times(k: Double) = this.copy().also { it *= k }
    operator fun Double.times(vector: Vector) = vector.copy().also { it *= this }

    fun copy() : Vector = Vector(array.clone(), bounds)


}
