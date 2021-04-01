package net.colors_wind.particleswarmoptimization

import net.colors_wind.particleswarmoptimization.Vector.Companion.times
import javax.xml.stream.Location
import kotlin.random.Random

class Particles(val question: Question) {

    var iterations: Int = 0
    val particles: Array<Particle> = Array(question.N){ Particle(this) }

    /** attribute **/
    var gBest = ParticleValue(particles.maxByOrNull { it.fitness }!!)

    fun iterate() {
        particles.sortedBy { it.fitness }.forEachIndexed{index,particle ->
            particle.rank = index + 1}
        particles.forEach { particle ->
            particle.update()
            iterations++
        }
    }

}

data class Particle(private val particles: Particles) {
    /** attribute **/
    var velocity = Vector(DoubleArray(particles.question.dimension){
        val bound = particles.question.bounds[it]
        Random.nextDouble(bound.lowerBound * 0.12, bound.upperBound * 0.12)
    }, particles.question.bounds)
    var location = Vector(particles.question.bounds)
    var fitness: Double = particles.question.fit(location)

    /** pBest **/
    private var pBest = ParticleValue(this)
    var pBestUpdate = false

    /** rank **/
    var rank = 0


    fun update() {
        velocity = (particles.question.omega(this) * velocity
                + particles.question.c1 * Random.nextDouble() * (pBest.location - location)
                + particles.question.c2 * Random.nextDouble() * (particles.gBest.location - location))
        //print("$location ${location + velocity}")
        location = location + velocity
        //println(" $location")
        fitness = particles.question.fit(location)
        if (fitness < pBest.fitness) {
            pBest = ParticleValue(this)
            pBestUpdate = true
        } else pBestUpdate = false
        if (pBest.fitness < particles.gBest.fitness) {
            particles.gBest = pBest
        }
    }

}

data class ParticleValue(val location: Vector, val velocity: Vector, val fitness: Double) {
    constructor(particle: Particle) : this(particle.location.copy(), particle.velocity.copy(), particle.fitness) {}
}