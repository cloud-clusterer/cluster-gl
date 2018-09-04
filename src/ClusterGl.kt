import model.Info
import org.w3c.dom.events.Event
import org.w3c.dom.events.EventTarget
import org.w3c.dom.events.MouseEvent
import react.*
import kotlin.js.Math

class ClusterGlProps(

): RProps

data class ClusterGlState(
        var cluster: RenderableCluser,
        var width: Int = 1500,
        var height: Int = 1000
): RState

class ClusterGl(props: ClusterGlProps) : RComponent<ClusterGlProps, ClusterGlState>(props) {

    private var mounted = false
    private var mouseLocation = Vector3(0f,0f,0f)
    private var viewMatrix = Matrix4.identity()
    private var inverseViewMatrix = Matrix4.identity()
    private val cursor = BorderedPolygon(20, 1f, 0.5f, color = Color(0f,1f,0f,1f))
    private val cursorb = BorderedPolygon(20, 1f, 0.5f, color = Color(1f,0f,0f,1f))
    private val line = Line(
            Vertex3d(Vector3(0f,0f,0f), Color(0f,0f,0f,1f)),
            Vertex3d(Vector3(0f,0f,1000f), Color(0f,0f,0f,1f))
    )

    override fun componentWillMount() {
        state.cluster = RenderableCluser(
                (0 .. 100).map{ RenderableNode(
                        model.Node(
                            name = "Brian",
                            info = Info("",""),
                            uuid = ""
                        ),
                        initialPosition = Vector3((Math.random() * 2 - 1).toFloat() * 20f, (Math.random() * 2 - 1).toFloat() * 20f, (Math.random() * 2 - 1).toFloat() * 200f),
                        fill = Fill(Color(it/100f, 0f, 0f, 1f)),
                        stroke = Stroke(1, Color(Math.random().toFloat(),Math.random().toFloat(), Math.random().toFloat(), 1f))
                ) }
        , emptyList())
        state.width = 1500
        state.height = 1000
    }

    override fun componentWillUnmount() {
        mounted = false
    }

    override fun componentDidMount() {
        mounted = true
    }

    fun onMouseMove(event: MouseEvent){
        val x = event.clientX - (event.target.asDynamic().offsetLeft as Float)
        val y = event.clientY - (event.target.asDynamic().offsetTop as Float)
        val screenLocation = Vector3((((2f*x)/state.width)-1f), -(((2f*y)/state.height)-1f), 0f)
        mouseLocation = inverseViewMatrix * screenLocation
        println(mouseLocation)
    }

    fun updateViewMatrix(){
        viewMatrix = aspectProjection(60f, state.width.toFloat(), state.height.toFloat())
        inverseViewMatrix = viewMatrix.inverse()
    }

    fun update(delta: Double){
        updateViewMatrix()
        state.cluster.update(delta)
        cursor.transformation = Matrix4.translation(mouseLocation)
        cursorb.transformation = Matrix4.translation(Vector3(x = mouseLocation.x * (line.b.position.z*0.01f+1), y = mouseLocation.y * (line.b.position.z*0.01f+1), z = line.b.position.z))
        line.a.position = line.a.position.copy(x = mouseLocation.x, y = mouseLocation.y)
        line.b.position = line.b.position.copy(x = mouseLocation.x * (line.b.position.z*0.01f+1), y = mouseLocation.y * (line.b.position.z*0.01f+1))
    }

    fun renderScene(program: Program){
        cursor.render(program)
        cursorb.render(program)
        line.render(program)
        state.cluster.render(program)
    }

    override fun RBuilder.render() {
        webGlCanvas(WebGlCanvasProps(
                render = {
                    program, delta ->
                    update(delta)
                    program.updateProjection(viewMatrix)
                    program.stage()
                    renderScene(program)
                },
                onMouseMove = ::onMouseMove,
                program = { projectedProgramFrom(it) }
        )){
            attrs.width = state.width
            attrs.height = state.height
        }
    }
}

fun RBuilder.clusterGl(props: ClusterGlProps) = child(ClusterGl::class){
    
}
