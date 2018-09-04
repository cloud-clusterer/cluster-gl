
import kotlinx.html.js.onMouseDownFunction
import kotlinx.html.js.onMouseMoveFunction
import kotlinx.html.js.onMouseUpFunction
import org.khronos.webgl.WebGLRenderingContext
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.events.Event
import org.w3c.dom.events.MouseEvent
import react.*
import react.dom.*
import kotlin.browser.window

class WebGlCanvasProps(
        var onMouseUp: (event: MouseEvent) -> Unit = {},
        var onMouseDown: (event: MouseEvent) -> Unit = {},
        var onMouseMove: (event: MouseEvent) -> Unit = {},
        var classes: String = "",
        var render: (program: Program, delta: Double) -> Unit,
        var program: (glContext: WebGLRenderingContext) -> Program = { simpleProgramFrom(it) },
        var width: Int = 500,
        var height: Int = 500
): RProps

class WebGlCanvasState(
        var program: Program
): RState

class WebGlCanvas(props: WebGlCanvasProps) : RComponent<WebGlCanvasProps, WebGlCanvasState>(props) {

    private var previousTime = 0.0
    private var totalTime = 0.0

    fun onMouseUp(event: Event) = props.onMouseUp(event.asDynamic().nativeEvent as MouseEvent)
    fun onMouseDown(event: Event) = props.onMouseDown(event.asDynamic().nativeEvent as MouseEvent)
    fun onMouseMove(event: Event) =  props.onMouseMove(event.asDynamic().nativeEvent as MouseEvent)

    override fun componentDidMount() {
        state.program = props.program(glContext())
        renderLoop(0.0)
    }
    private fun glContext() = canvas().getContext("webgl") as? WebGLRenderingContext ?: throw IllegalArgumentException()
    private fun canvas() = findDOMNode(this) as HTMLCanvasElement

//    fun updateView(){
//        val canvas = canvas()
//        canvas.width = props.width
//        canvas.height = props.height
//        this.state.program.updateProjection(
//
//        )
//        this.aspectMatrix = Matrix3.scale(new Vector2D(this.props.scale, this.props.scale)).multiply(Matrix3.aspect(1.2, canvas.width, canvas.height))
//        this.props.viewUpdated(this.programs, this.aspectMatrix, canvas.width, canvas.height)
//        this.inverseView = this.aspectMatrix.inverse()
//    }

    fun renderLoop(delta: Double){
        props.render(state.program, delta)
        window.requestAnimationFrame{ totalTime += it; renderLoop(it - previousTime); previousTime = it }
    }

    override fun RBuilder.render() {
        canvas(props.classes){
            attrs.onMouseUpFunction = ::onMouseUp
            attrs.onMouseDownFunction = ::onMouseDown
            attrs.onMouseMoveFunction = ::onMouseMove
            attrs.width = "${props.width}px"
            attrs.height = "${props.height}px"
        }
    }
}

fun RBuilder.webGlCanvas(props: WebGlCanvasProps, handler: RHandler<WebGlCanvasProps>) = child(WebGlCanvas::class){
    attrs.onMouseUp = props.onMouseUp
    attrs.onMouseDown = props.onMouseDown
    attrs.onMouseMove = props.onMouseMove
    attrs.classes = props.classes
    attrs.render = props.render
    attrs.program = props.program
    attrs.width = props.width
    attrs.height = props.height
    handler(this)
}
