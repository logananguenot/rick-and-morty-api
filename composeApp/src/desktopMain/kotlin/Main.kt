import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.koin.core.context.startKoin
import org.mathieu.cleanrmapi.koin.desktopModule
import org.mathieu.cleanrmapi.ui.App

fun main() = application {
    startKoin {
        modules(desktopModule)
    }

    Window(
        onCloseRequest = ::exitApplication,
        title = "Clean RmApi UDF",
    ) {
        App()
    }
}