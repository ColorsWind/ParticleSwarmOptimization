package net.colors_wind.particleswarmoptimization

import net.colors_wind.particleswarmoptimization.Vector.Companion.times
import kotlin.random.Random

class Particles(val question: Question) {

    var iterations: Int = 0
    val particles: Array<Particle> = Array(question.N){ Particle(this) }
    var gBest = particles.maxByOrNull { it.fitness }!!.location
    var gBestFitness = particles.maxByOrNull { it.fitness }!!.fitness

    fun iterate() {
        particles.forEach { particle ->
            particle.update()
            iterations++
        }
    }

}

data class Particle(private val particles: Particles) {
    private var velocity = Vector(DoubleArray(particles.question.dimension){
        val bound = particles.question.bounds[it]
        Random.nextDouble(bound.lowerBound * 0.2, bound.upperBound * 0.2)
    }, particles.question.bounds)
    var location = Vector(particles.question.bounds)
    var fitness: Double = particles.question.fit(location)

    private var pBest = location
    private var pBestFitness = fitness

    fun update() {
        velocity = (particles.question.omega(particles) * velocity
                + particles.question.c1 * Random.nextDouble() * (pBest - location)
                + particles.question.c2 * Random.nextDouble() * (particles.gBest - location))
        location = location + velocity
        fitness = particles.question.fit(location)
        if (fitness < pBestFitness) {
            pBest = location
            pBestFitness = fitness
        }
        if (pBestFitness < particles.gBestFitness) {
            particles.gBest = pBest
            particles.gBestFitness = pBestFitness
        }
    }



    fun getMessage(): String {
        return "fitness=$fitness, location=$location."
    }
}