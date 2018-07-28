import org.khronos.webgl.WebGLRenderingContext
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

data class BorderedPolygon(
        val sides: Int,
        val outerRadius: Float,
        val innerRadius: Float,
        val startDirection: Vector3 = Vector3(1f,0f,0f),
        val color: Color = Color(1f, 1f, 1f, 1f)
): Object(verticesFor(sides, outerRadius, innerRadius, startDirection, color)){

    override fun optimizedType() = WebGLRenderingContext.TRIANGLES

    companion object {

        private fun Vector3.rotate(angle: Float) = Vector3(
                x = x * cos(angle) - y * sin(angle),
                y = x * sin(angle) + y * cos(angle),
                z = 0f
        )

       private fun verticesFor(sides: Int, outerRadius: Float, innerRadius: Float, startDirection: Vector3, color: Color): List<Vertex3d>{
            val angle = -(2.0 * PI / sides).toFloat()
            val outerStart = startDirection * outerRadius
            val innerStart = startDirection * innerRadius
            return (0 .. sides).fold(emptyList<Triple<Vector3,Vector3,Vector3>>()){
                triangles, side ->
                    val nextSide = if(side == sides-1) 0 else side + 1
                    val currentOuter = outerStart.rotate(angle*side)
                    val nextOuter = outerStart.rotate(angle*nextSide)
                    val currentInner = innerStart.rotate(angle*side)
                    val nextInner = innerStart.rotate(angle*nextSide)
                    triangles + listOf(
                            Triple(currentInner, currentOuter, nextOuter),
                            Triple(currentInner, nextOuter, nextInner)
                    )
            }.flatMap { it.toList() }.map { Vertex3d(it, color) }
        }
    }
}