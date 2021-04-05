import net.colors_wind.particleswarmoptimization.Particles
import net.colors_wind.particleswarmoptimization.Question
import net.colors_wind.particleswarmoptimization.Schwefel
import java.io.File
import java.io.FileWriter
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.CountDownLatch

fun main() {
    val omegas = 0.4.rangeTo(0.9).step(0.1).toList()
    val ccs = 0.5.rangeTo(3.5).step(0.5).toList()
    val queue = ConcurrentLinkedQueue<Question>()
    val result = Vector<String>()
    for (cc in ccs) for (omega in omegas) {
        queue.add(Schwefel(omega, cc, cc))
    }
    val latch = CountDownLatch(Runtime.getRuntime().availableProcessors())
    repeat(Runtime.getRuntime().availableProcessors()) {
        Thread(WorkThread(it, queue, result, latch)).start()
    }
    latch.await()
    val writer = FileWriter(File("result.csv").apply { takeUnless { it.exists() }?.createNewFile() })
    writer.appendLine("omega,cc,avg,worst,best")
    result.forEach { writer.appendLine(it) }
    writer.flush()
    writer.close()
    println("Write completed!")

}

const val TIMES = 30

class WorkThread(
    private val index: Int,
    private val queue: Queue<Question>,
    val result: Vector<String>,
    val latch: CountDownLatch
) :
    Runnable {
    override fun run() {
        println("Thread #$index Start")
        while (true) queue.poll()?.let { question ->
            val multi = Array(TIMES) {
                val particles = Particles(question)
                repeat(question.Gmax) {
                    particles.iterate()
                }
                particles
            }
            result.add(
                "${question.omega}," +
                "${question.c1}," +
                "${multi.sumByDouble { it.gBest.fitness } / TIMES}," +
                "${multi.maxByOrNull { it.gBest.fitness }!!.gBest.fitness}," +
                "${multi.minByOrNull { it.gBest.fitness }!!.gBest.fitness}"
            )
            println("Rest task: ${queue.size}")
        } ?: break
        latch.countDown()
        println("Thread #$index Stop")
    }

}

infix fun ClosedRange<Double>.step(step: Double): Iterable<Double> {
    require(start.isFinite())
    require(endInclusive.isFinite())
    require(step > 0.0) { "Step must be positive, was: $step." }
    val sequence = generateSequence(start) { previous ->
        if (previous == Double.POSITIVE_INFINITY) return@generateSequence null
        val next = previous + step
        if (next > endInclusive) null else next
    }
    return sequence.asIterable()
}
