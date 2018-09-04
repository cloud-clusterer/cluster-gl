import kotlin.js.Math

class RenderableLink(val nodeA: RenderableNode, val nodeB: RenderableNode, val length: Float = 1f){
    private fun behind(v: Vector3) = v.copy(z = v.z + 1f)
    fun line() = Line(
            Vertex3d(behind(nodeA.center()), Color(0f,0f,0f,0.4f)),
            Vertex3d(behind(nodeB.center()),  Color(0f,0f,0f,0.4f))
    )
}

class RenderableCluser(
        val nodes: List<RenderableNode>,
        var links: List<RenderableLink>,
        val electrostaticForce: Float = 0.8f,
        val springForce: Float = 0.05f
){

    val grid: Grid<RenderableNode> = Grid(10f)

    init {
        links =(0..100).map{
            RenderableLink(nodes[(Math.random() * nodes.count()).toInt()], nodes[(Math.random() * nodes.count()).toInt()])
        }
    }

    fun applyElectrostaticForce(){
        nodes.forEach { node ->
            node.velocity += nodes.fold(Vector3(0f,0f,0f)){
                totalForce, otherNode -> if(otherNode != node){
                    val difference = node.position - otherNode.position
                    val distance = difference.length()
                    val direction = difference.direction()
                    if(distance > 0.1f) totalForce + direction * (electrostaticForce * 0.0001f / distance) else totalForce
                } else totalForce
            }
        }
    }

    fun applySpringForces(){
        links.forEach {
            val difference = it.nodeB.position - it.nodeA.position
            val length = difference.length()
            val directionTowardB = difference.direction()
            val gap = length - it.length
            val force = gap * springForce * 0.0001f
            val directionalForce = directionTowardB * force
            val reverseForce = directionalForce * -1f
            it.nodeA.velocity += directionalForce
            it.nodeB.velocity += reverseForce
            // Dampen further if smaller than desired length
            if(length<it.length){
                val percentage = length / it.length
                it.nodeA.velocity *= 1f - (percentage*0.01f)
                it.nodeB.velocity *= 1f - (percentage*0.01f)
            }
        }
    }

    fun update(delta: Double){
        nodes.forEach { grid.register(it, it.center()) }
        applyElectrostaticForce()
        applySpringForces()
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