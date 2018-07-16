
import kotlinx.html.js.onMouseDownFunction
import kotlinx.html.js.onMouseMoveFunction
import kotlinx.html.js.onMouseUpFunction
import org.khronos.webgl.WebGLProgram
import org.khronos.webgl.WebGLRenderingContext
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.events.Event
import org.w3c.dom.events.MouseEvent
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.*
import kotlin.browser.window

class WebGlCanvasProps(
        var onMouseUp: (event: MouseEvent) -> Unit,
        var onMouseDown: (event: MouseEvent) -> Unit,
        var onMouseMove: (event: MouseEvent) -> Unit,
        var render: (program: Program, delta: Double) -> Unit,
        var classes: String = ""
): RProps

class WebGlCanvasState(
        var defaultProgram: Program
): RState

class WebGlCanvas(props: WebGlCanvasProps) : RComponent<WebGlCanvasProps, WebGlCanvasState>(props) {

    fun onMouseUp(event: Event) = props.onMouseUp(event as MouseEvent)
    fun onMouseDown(event: Event) = props.onMouseDown(event as MouseEvent)
    fun onMouseMove(event: Event) = props.onMouseMove(event as MouseEvent)

    override fun componentDidMount() {
        state.defaultProgram = simpleProgramFrom(glContext())
        renderLoop(0.0)
    }
    private fun glContext() = canvas().getContext("webgl") as? WebGLRenderingContext ?: throw IllegalArgumentException()
    private fun canvas() = findDOMNode(this) as HTMLCanvasElement

    fun renderLoop(delta: Double){
        props.render(state.defaultProgram, delta)
        window.requestAnimationFrame{ renderLoop(it) }
    }

    override fun RBuilder.render() {
        canvas(props.classes){
            attrs.onMouseUpFunction = ::onMouseUp
            attrs.onMouseDownFunction = ::onMouseDown
            attrs.onMouseMoveFunction = ::onMouseMove
        }
    }
}

fun RBuilder.webGlCanvas(props: WebGlCanvasProps) = child(WebGlCanvas::class){
    attrs.onMouseUp = props.onMouseUp
    attrs.onMouseDown = props.onMouseDown
    attrs.onMouseMove = props.onMouseMove
    attrs.classes = props.classes
    attrs.render = props.render
}
