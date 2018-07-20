import org.w3c.dom.Element
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.findDOMNode

class ClusterGlProps(

): RProps

class ClusterGlState(
        var scene: Scene
): RState

class ClusterGl(props: ClusterGlProps) : RComponent<ClusterGlProps, ClusterGlState>(props) {

    override fun componentWillMount() {
        state.scene = Scene(listOf(Polygon(6, 0.2f)))
    }

    fun update(){
        state.scene.objects[0].transformation = state.scene.objects[0].transformation.translate(Vector3(0f, 0f, 1f))
    }


    fun can(): WebGlCanvasProps {
        val canvas: Element? = findDOMNode(this)
        return WebGlCanvasProps(
                render = {
                    program, delta ->
                    program.updateProjection(aspectProjection(60f, 1000f, 500f))
                    println(delta)
                    program.stage()
                    state.scene.render(program)
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
