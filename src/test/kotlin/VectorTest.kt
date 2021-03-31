import net.colors_wind.particleswarmoptimization.Bound
import net.colors_wind.particleswarmoptimization.Vector
import org.junit.jupiter.api.Test

class VectorTest {

    @Test
    fun test() {
        val bound = Bound(-5.0, 5.0)
        val bounds = arrayOf(bound, bound)
        val vector1 = Vector(doubleArrayOf(1.0, 1.0), bounds)
        val vector2 = vector1.copy()
        val vector3 = Vector(doubleArrayOf(2.0, 2.0), bounds)
        val vector4 = Vector(doubleArrayOf(5.0, 5.0), bounds)
        assert(vector1 == vector2)
        assert(vector1 + vector2 == vector3)
        assert(vector1 * 10.0 == vector4)

    }
}