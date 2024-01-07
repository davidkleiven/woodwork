package com.github.davidkleiven.woodwork
import kotlinx.cli.ArgType
import kotlinx.cli.ExperimentalCli
import kotlinx.cli.Subcommand
import kotlin.math.PI
import kotlin.math.sin
import kotlin.math.tan

@OptIn(ExperimentalCli::class)
class Circle: Subcommand("circle", "Calculate number of straight edges and angles to required to create a circle") {
    private val radius by argument(ArgType.Double, "radius", "Outer radius of circle")
    private val numPieces by argument(ArgType.Int, "numPieces", "Number of pieces to use")
    private val circleWidth by argument(ArgType.Double, "circleWidth", "With of circle path")

    override fun execute() {
        val result = calculateMaterialDimensions(numPieces, radius, circleWidth)
        println("Outer radius of circle: $radius")
        println("Number of pieces: $numPieces")
        println("Circle edge width: $circleWidth")
        println("Cutting angle: %.2f degrees".format(result.angleDeg()))
        println("Required piece width: %.2f".format(result.pieceWidth))
        println("Inner length of straight piece: %.2f".format(result.innerLength))
        println("Outer length of straight piece: %.2f".format(result.outerLength()))
    }
}

data class CircleApproxResult(val angle: Double, val innerLength: Double, val pieceWidth: Double) {
    fun outerLength(): Double {
        return innerLength + 2.0*pieceWidth*tan(angle)
    }

    fun angleDeg(): Double  {
        return angle*180.0/PI
    }
}

fun calculateMaterialDimensions(numPieces: Int, outerRadius: Double, circleWidth: Double): CircleApproxResult {
    val angle = 2.0* PI/numPieces
    val innerLength = 2.0*(outerRadius-circleWidth)* sin(angle/2.0)
    val requiredPieceWidth = outerRadius - innerLength/(2.0*tan(angle/2.0))
    return CircleApproxResult(angle, innerLength, requiredPieceWidth)
}