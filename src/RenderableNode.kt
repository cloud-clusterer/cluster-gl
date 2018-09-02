class RenderableNode(val node: model.Node, val stroke: Stroke, val fill: Fill, initialPosition: Vector3): Renderable{

    private val prototype: Polygon = Polygon(6, 1.0f)
    var position: Vector3 = initialPosition
    var velocity: Vector3 = Vector3(0f,0f,0.1f)

    init {
        prototype.transformation = Matrix4.translation(position)
        prototype.color(fill.color)
    }

    fun center() = prototype.center()

    fun update(delta: Double){
        position += velocity * delta.toFloat()
        dampen(delta, 0.0001f)
        prototype.transformation = Matrix4.translation(position)
    }

    private fun dampen(timeDelta: Double, airResistance: Float){
        velocity *= 1-(airResistance * timeDelta.toFloat())
        if(velocity.length() <= airResistance){
            velocity *= 0f
        }
    }

    override fun render(program: Program) {
        prototype.render(program)
    }
}
