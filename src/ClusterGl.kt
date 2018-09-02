import model.Info
import org.w3c.dom.Element
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.findDOMNode
import kotlin.browser.document
import kotlin.browser.window
import kotlin.js.Math

class ClusterGlProps(

): RProps

class ClusterGlState(
        var cluster: RenderableCluser
): RState

class ClusterGl(props: ClusterGlProps) : RComponent<ClusterGlProps, ClusterGlState>(props) {

    override fun componentWillMount() {
        state.cluster = RenderableCluser(
                (0 .. 100).map{ RenderableNode(
                        model.Node(
                            name = "Brian",
                            info = Info("",""),
                            uuid = ""
                        ),
                        initialPosition = Vector3((Math.random() * 2 - 1).toFloat() * 20f, (Math.random() * 2 - 1).toFloat() * 20f, 0f),
                        fill = Fill(Color(it/100f, 0f, 0f, 1f)),
                        stroke = Stroke(1, Color(it/100f,0f, 0f, 1f))
                ) }
        , emptyList())
    }

    fun update(delta: Double){
        state.cluster.update(delta)
        println("${(1000/delta)} fps")
    }


    fun canvas(): WebGlCanvasProps {
        val canvas: Element? = findDOMNode(this)
        return WebGlCanvasProps(
                render = {
                    program, delta ->
                    program.updateProjection(aspectProjection(60f, 1000f, 500f))
                    program.stage()
                    state.cluster.render(program)
                    update(delta)
                },
                program = { projectedProgramFrom(it) }
        )
    }

    override fun RBuilder.render() {
        webGlCanvas(canvas()){
            attrs.width = 1000
            attrs.height = 500
        }
    }
}

fun RBuilder.clusterGl(props: ClusterGlProps) = child(ClusterGl::class){
    
}
