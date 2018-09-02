import kotlin.js.Math

class RenderableLink(val nodeA: RenderableNode, val nodeB: RenderableNode){
    private fun behind(v: Vector3) = v.copy(z = v.z + 0.001f)
    fun line() = Line(
            Vertex3d(behind(nodeA.center()), Color(0f,0f,0f,1f)),
            Vertex3d(behind(nodeB.center()),  Color(0f,0f,0f,1f))
    )
}

class RenderableCluser(
        val nodes: List<RenderableNode>,
        var links: List<RenderableLink>
){
    init {
        links =(0..100).map{
            RenderableLink(nodes[(Math.random() * nodes.count()).toInt()], nodes[(Math.random() * nodes.count()).toInt()])
        }
    }

    fun update(delta: Double){
        nodes.forEach { it.update(delta) }
    }

    fun render(program: Program){
        Scene(links.map { it.line() } + nodes).render(program)
    }

    companion object {
//        fun clusterFrom(nodeTrees: List<model.NodeTree>): RenderableCluser{
//            val renderableNodes =  nodeTrees.flatMap {
//                it.nodes.map {
//                    RenderableNode(it,Stroke(1, Color(1f,0f,0f,1f)),Fill(Color(0f,1f,0f,1f)), initialPosition = it.)
//                }
//            }
//            return RenderableCluser(renderableNodes,
//                    links = nodeTrees.flatMap {
//                        it.links.map { link ->
//                            RenderableLink(renderableNodes.find { it.node.uuid == link.uuidA }!!, renderableNodes.find { it.node.uuid == link.uuidB }!!)
//                        }
//                    }
//            )
//        }
    }
}