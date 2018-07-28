import kotlin.js.Math

class RenderableNode(val node: Node): Polygon(6,1f)
class RenderableLink(val a: RenderableNode, val b: RenderableNode): Polygon(3,1f)

class RenderableCluser(
        val nodes: List<RenderableNode>,
        val links: List<RenderableLink>
){
    init {
        nodes.forEach {
            val x = (Math.random() * 2 - 1).toFloat()
            val y = (Math.random() * 2 - 1).toFloat()
            it.transformation = it.transformation.translate(Vector3(x, y, 0f))
        }
    }

    fun render(program: Program){
        nodes.forEach{  it.color(it.node.fill.color) }
        Scene(links + nodes).render(program)
    }
}