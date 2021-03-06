package net.colors_wind.particleswarmoptimization


import java.lang.IllegalArgumentException
import kotlin.math.abs
import kotlin.random.Random

data class Bound(val lowerBound: Double, val upperBound: Double) {
    operator fun invoke(value: Double) =
        when {
//            value < lowerBound -> lowerBound
//            value > upperBound -> upperBound
            value < lowerBound -> Random.nextDouble(lowerBound, upperBound)
            value > upperBound -> Random.nextDouble(lowerBound, upperBound)
            else -> value
        }
}

class Vector(private val array: DoubleArray, private val bounds: Array<Bound>) {
    val dimension: Int
        get() = array.size

    constructor(bounds: Array<Bound>) : this(DoubleArray(bounds.size), bounds) {
        for ((index, bound) in array.indices zip bounds) {
            array[index] = Random.nextDouble(bound.lowerBound, bound.upperBound)
        }
    }


    operator fun get(index: Int) = array[index]
    operator fun set(index: Int, value: Double) {
        array[index] = bounds[index](value)
    }

    operator fun unaryPlus(): Vector = this.copy()
    operator fun plusAssign(other: Vector) = array.indices.forEach { this[it] = this[it] + other[it] }
    operator fun plus(other: Vector) = this.copy().also { it += other }

    operator fun unaryMinus() = copy().also { array.indices.forEach { this[it] = -this[it] } }
    operator fun minusAssign(other: Vector) = array.indices.forEach { this[it] = this[it] -other[it] }
    operator fun minus(other: Vector) = copy().also { it -= other }

    operator fun timesAssign(k: Double) = array.indices.forEach { this[it] = this[it] * k }
    operator fun times(k: Double) = this.copy().also { it *= k }

    operator fun iterator() = array.iterator()

    fun sumBy(func: (Double) -> Double) = array.sumByDouble(func)
    fun sumByWithIndex(func: (Int, Double) -> Double): Double {
        var sum = 0.0
        array.forEachIndexed { index, value ->
            sum += func(index + 1, value)
        }
        return sum
    }

    fun checkDimension(vector: Vector): Vector {
        if (vector.dimension != this.dimension) throw IllegalArgumentException()
        return this
    }

    fun copy(): Vector = Vector(array.clone(), bounds)

    override fun toString(): String {
        return array.contentToString()
    }

    fun toCSVLikeString(): String {
        return array.joinToString(",")
    }

    override operator fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Vector
        if (array.size != other.array.size) return false
        for ((x, y) in array zip other.array) {
            if (abs(x - y) > 0.001) return false
        }
        return true
    }

    override fun hashCode(): Int {
        return array.contentHashCode()
    }


    companion object {

        operator fun Double.times(vector: Vector) = vector * this
    }


}
