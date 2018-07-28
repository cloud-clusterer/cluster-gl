import org.w3c.dom.Element
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.findDOMNode

class ClusterGlProps(

): RProps

class ClusterGlState(
        var cluster: RenderableCluser
): RState

class ClusterGl(props: ClusterGlProps) : RComponent<ClusterGlProps, ClusterGlState>(props) {

    override fun componentWillMount() {
        state.cluster = RenderableCluser(
                (0 .. 100).map{ RenderableNode(Node(
                        name = "Brian",
                        text = "Hello",
                        fill = Fill(Color(it/100f, 0f, 0f, 1f)),
                        stroke = Stroke(1, Color(it/100f,0f, 0f, 1f))
                )) }
        , emptyList())
    }

    fun update(){

    }


    fun can(): WebGlCanvasProps {
        val canvas: Element? = findDOMNode(this)
        return WebGlCanvasProps(
                render = {
                    program, delta ->
                    program.updateProjection(aspectProjection(60f, 1000f, 500f))
                    program.stage()
                    state.cluster.render(program)
                    update()
                },
                program = { projectedProgramFrom(it) }
        )
    }

    override fun RBuilder.render() {
        webGlCanvas(can()){
            attrs.width = 1000
            attrs.height = 500
        }
    }
}

fun RBuilder.clusterGl(props: ClusterGlProps) = child(ClusterGl::class){
    
}
