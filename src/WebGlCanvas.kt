
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.*

class App : RComponent<RProps, RState>() {
    override fun RBuilder.render() {
        div("App-header") {
            h2 {
                +"Welcome to React with Kotlin"
            }
        }
        p("App-intro") {
            +"To get started, edit "
            code { +"app/App.kt" }
            +" and save to reload."
        }
        p("App-ticker") {
        }
    }
}

fun RBuilder.app() = child(App::class) {}
