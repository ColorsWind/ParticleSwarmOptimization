import net.colors_wind.particleswarmoptimization.*
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
        println("${particles.gBest.fitness}, ${particles.gBest.location}")
        writer.appendLine("${particles.gBest.fitness}, ${particles.gBest.location}")
    }
    writer.close()



}
