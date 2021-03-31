import net.colors_wind.particleswarmoptimization.Easy
import net.colors_wind.particleswarmoptimization.Particles
import net.colors_wind.particleswarmoptimization.Rosenbrock
import java.io.File
import java.io.FileWriter
import java.io.PrintStream

fun main(args: Array<String>) {
    //val particles = Particles(Schwefel())
    //val particles = Particles(Sphere())
    val question = Easy()
    val particles = Particles(question)
    val writer = FileWriter(File("dump.log"))
    repeat(question.Gmax) {
        particles.iterate()
        println("${particles.gBestFitness}, ${particles.gBest}")
        writer.appendLine("${particles.gBestFitness}, ${particles.gBest}")
    }
    writer.close()



}
