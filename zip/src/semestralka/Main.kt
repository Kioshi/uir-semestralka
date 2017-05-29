package semestralka

import javafx.application.Application
import semestralka.Utils.classificator
import semestralka.Utils.getClassificator
import semestralka.Utils.getSelector
import semestralka.Utils.loadModel
import semestralka.Utils.prepareText
import semestralka.gui.App
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.util.*

/**
 * Created by Štěpán Martínek on 26.05.2017.
 */


fun main(args : Array<String>) {

    try {

        if (args.isEmpty() || args.size > 5)
        {
            println("Usage:")
            println("Training mode arguments: <path to train data> <path to test data> <fetures selector (df/cf/kf)> <classificator (nb/nn)> <model name>")
            println("Testing mode arguments: <path to model>")
        }
        else if (args.size < 5)
            startGUI(args)
        else
            learn(args)
    }
    catch (ex: IllegalArgumentException)
    {
        println("Wrong arguments! ${ex.message}")
    }
    catch (ex: Exception)
    {
        println("Error: ${ex.message}")
    }
}

fun startGUI(args: Array<String>) {
    loadModel(args[0])
    Application.launch(App::class.java, args[0]);
}

fun learn(args: Array<String>) {
    println("Loading...")
    val trainSet = loadFiles(args[0])
    val testSet = loadFiles(args[1])
    val selector = getSelector(args[2])
    classificator = getClassificator(args[3])

    classificator.addSelector(selector)
    println("Training...")
    classificator.train(trainSet)
    println("Testing...")
    classificator.test(testSet)
    println("Saving...")
    classificator.saveModel(args[4])
}

fun loadFiles(dir: String): HashMap<File, TreeMap<String, Int>> {
    val folder = File(dir)
    val listOfFiles = folder.listFiles()

    val tokenizedFiles = HashMap<File,TreeMap<String,Int>>()

    listOfFiles.forEach {
        val sb = StringBuilder()
        val br = BufferedReader(FileReader(it))

        br.forEachLine { line -> sb.append(line); sb.append("\n") }

        tokenizedFiles.put(it, prepareText(sb.toString()))
    }

    return tokenizedFiles
}
