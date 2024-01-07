import com.github.davidkleiven.woodwork.CircleApproxResult
import com.github.davidkleiven.woodwork.calculateMaterialDimensions
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import kotlin.math.PI
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TestCircleApprox {
    @Test
    fun `test two pieces`() {
        val radius = 10.0
        val width = 2.0
        val result = calculateMaterialDimensions(2, radius, width)
        assertEquals(radius, result.pieceWidth, 1e-4)
    }

    @Test
    fun `test asymptotic behavior for large number of pieces`() {
        val radius = 10.0
        val width = 2.0
        val numElements = listOf(200, 300, 500, 1000, 10000, 50000)
        val results = numElements.map { n -> calculateMaterialDimensions(n, radius, width) }
        val innerLengths = results.map { result -> result.innerLength }
        val pieceWidths = results.map { result -> result.pieceWidth }
        val innerLengthRatios = pairWiseRatios(innerLengths)
        val outerMinusInnerRatio = pairWiseRatios(results.map { result -> result.outerLength() - result.innerLength })
        val numElementsRatio = pairWiseRatios(numElements.map { it.toDouble() })

        val tol = 1e-3
        // Inner length should be inversely proportional to the number of pieces
        assertTrue(numElementsRatio.zip(innerLengthRatios).all { abs(1.0 / it.first - it.second) < tol })

        // <Outer radius> - <required piece width> should be proportional to the number of pieces cos(alpha/2)
        // Thus for large n -> it is approximately independent of the number of pieces and equal to the circle
        // width
        assertTrue(pieceWidths.all { abs(it - width) < tol })

        // Outer length - inner length ratio should be inversely proportional the ratio of number of pieces
        assertTrue(numElementsRatio.zip(outerMinusInnerRatio).all { abs(1.0 / it.first - it.second) < tol })
    }

    @TestFactory
    fun testAngle() = listOf(Pair(2.0 * PI, 360.0), Pair(PI, 180.0), Pair(PI / 2.0, 90.0), Pair(0.0, 0.0)).map {
        DynamicTest.dynamicTest("${it.first} radians equals $it.second deg") {
            val result = CircleApproxResult(it.first, 4.0, 4.0)
            assertEquals(it.second, result.angleDeg(), 1e-8)
        }
    }
}

fun pairWiseRatios(results: List<Double>): List<Double> {
    return results.windowed(2, 1).map { result ->
        result.first() / result.last()
    }
}
