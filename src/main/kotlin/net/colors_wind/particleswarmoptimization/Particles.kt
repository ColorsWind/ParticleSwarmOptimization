package net.colors_wind.particleswarmoptimization

import net.colors_wind.particleswarmoptimization.Vector.Companion.times
import kotlin.random.Random

class Particles(val question: Question) {

    var iterations: Int = 0
    val particles: Array<Particle> = Array(question.N){ Particle(this, it) }

    /** attribute **/
    var gBest = ParticleValue(particles.maxByOrNull { it.fitness }!!)

    fun iterate() {
        particles.sortedBy { it.fitness }.forEachIndexed{index,particle ->
            particle.rank = index + 1}
        particles.forEach { particle ->
            particle.update()
        }
        iterations++
    }

    operator fun get(index: Int) = particles[(index + question.N) % question.N]

}

data class Particle(private val particles: Particles, private val index: Int) {
    /** attribute **/
    var velocity = Vector(DoubleArray(particles.question.dimension){
        val bound = particles.question.bounds[it]
        Random.nextDouble(bound.lowerBound * 0.12, bound.upperBound * 0.12)
    }, particles.question.bounds)
    var location = Vector(particles.question.bounds)
    var fitness: Double = particles.question.fit(location)

    /** pBest **/
    private var pBest = ParticleValue(this)
    private fun gBest() : ParticleValue {
        val copy = particles.particles.clone()
        copy.shuffle()
        val range = copy.copyOfRange(0, particles.question.connectNum(particles))
        //println(range.size)
        return copy.copyOfRange(0, particles.question.connectNum(particles))
            .minByOrNull { it.pBest.fitness }!!
            .pBest

    }


    /** rank **/
    var rank = 0


    fun update() {
        velocity = (particles.question.omega(this) * velocity
                + particles.question.c1 * Random.nextDouble() * (pBest.location - location)
                + particles.question.c2 * Random.nextDouble() * (gBest().location - location))
        //print("$location ${location + velocity}")
        location = location + velocity
        //println(" $location")
        fitness = particles.question.fit(location)
        if (fitness < pBest.fitness) {
            pBest = ParticleValue(this)
        }
        if (pBest.fitness < particles.gBest.fitness) {
            particles.gBest = pBest
        }
        if (Random.nextDouble() < (1-particles.iterations / particles.question.Gmax) * 0.01) pBest = particles[Random.nextInt(particles.question.N)].pBest
    }

}

data class ParticleValue(val location: Vector, val velocity: Vector, val fitness: Double) {
    constructor(particle: Particle) : this(particle.location.copy(), particle.velocity.copy(), particle.fitness) {}
}