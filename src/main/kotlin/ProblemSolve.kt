import net.colors_wind.particleswarmoptimization.Particles
import net.colors_wind.particleswarmoptimization.Schwefel
import net.colors_wind.particleswarmoptimization.Sphere
import java.io.File
import java.io.FileWriter

fun main() {

    //val question = Sphere()
    val question = Schwefel()
    val particles = Particles(question)
    val writer = FileWriter(File("dump.log"))
    repeat(question.Gmax) {
        particles.iterate()
        writer.appendLine("${particles.gBest.fitness}, ${particles.gBest.location}")
    }
    println("${particles.gBest.fitness}, ${particles.gBest.location}")
    writer.close()
}