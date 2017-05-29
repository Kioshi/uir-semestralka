package semestralka.gui

import javafx.application.Application
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextArea
import javafx.scene.layout.BorderPane
import javafx.stage.Stage
import semestralka.Utils.classificator

/**
 * Created by Štěpán Martínek on 28.05.2017.
 */

class App : Application()
{
    override fun start(primaryStage: Stage) {
        primaryStage.setTitle("Classificator GUI");
        //The BorderPane has the same areas laid out as the
        //BorderLayout layout manager
        val componentLayout = BorderPane()
        componentLayout.padding = Insets(5.0, 5.0, 5.0, 5.0)

        val topBP = BorderPane()
        topBP.padding = Insets(5.0, 5.0, 5.0, 5.0)
        topBP.left = Label("Selector: ${classificator.selector.getFullName()}")
        topBP.right = Label("Classificator: ${classificator.getFullName()}")

        componentLayout.top = topBP

        val ta = TextArea("")
        ta.promptText = "Enter text for classification"
        componentLayout.center = ta


        val botBP = BorderPane()
        botBP.padding = Insets(5.0, 5.0, 5.0, 5.0)
        val label = Label("Category: ")
        val button = Button("Classify")
        botBP.left = label
        botBP.right = button

        button.setOnAction {
            label.text = "Category: ${classificator.test(ta.text.toString())}"
        }

        componentLayout.bottom = botBP
        //Add the BorderPane to the Scene
        val appScene = Scene(componentLayout, 500.0, 500.0)

        //Add the Scene to the Stage
        primaryStage.setScene(appScene)
        primaryStage.show()
    }

}