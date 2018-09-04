class RenderableNode(val node: model.Node, val stroke: Stroke, val fill: Fill, initialPosition: Vector3): Renderable{

    private val prototype = Polygon(6, 0.8f)
    private val prototypeBorder = BorderedPolygon(6, 1.0f, 0.8f, color = stroke.color)
    var position: Vector3 = initialPosition + Vector3(0f,0f,500f)
    var velocity: Vector3 = Vector3(0f,0f,0f)

    init {
        prototype.transformation = Matrix4.translation(position)
        prototype.color(fill.color)
    }

    fun center() = prototype.center()

    fun update(delta: Double){
        position += velocity * delta.toFloat()
        dampen(delta, 0.0001f)
        prototype.transformation = Matrix4.translation(position)
        prototypeBorder.transformation = Matrix4.translation(position + Vector3(0f,0f,1f))
    }

    private fun dampen(timeDelta: Double, airResistance: Float){
        velocity *= 1-(airResistance * timeDelta.toFloat())
        if(velocity.length() <= airResistance){
            velocity *= 0f
        }
    }

    override fun render(program: Program) {
        prototype.render(program)
        prototypeBorder.render(program)

    }
}
