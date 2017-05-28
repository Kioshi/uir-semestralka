package semestralka

import semestralka.classificators.getClassificator
import semestralka.symptoms.getSelector
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.util.*

/**
 * Created by Štěpán Martínek on 26.05.2017.
 */


fun main(args : Array<String>) {

    if (args.isEmpty())
        println("Not enough arguments!")
    else if (args.size < 5)
        startGUI(args)
    else
        learn(args)
}

fun startGUI(args: Array<String>) {
    throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
}

fun learn(args: Array<String>) {
    val trainSet = loadFiles(args[0])
    val testSet = loadFiles(args[1])
    val selector = getSelector(args[2])
    val classificator = getClassificator(args[3])

    classificator.addSelector(selector)
    classificator.train(trainSet)
    classificator.test(testSet)
    classificator.saveModel(args[4])
}

fun loadFiles(dir: String): HashMap<File, HashMap<String, Int>> {
    val folder = File(dir)
    val listOfFiles = folder.listFiles()

    val tokenizedFiles = HashMap<File,HashMap<String,Int>>()

    listOfFiles.forEach {
        val sb = StringBuilder()
        val br = BufferedReader(FileReader(it))

        br.forEachLine { line -> sb.append(line); sb.append("\n") }

        var prepared = sb.toString()
        prepared = prepared.replace(Regex("-\n"),"")
        prepared = prepared.replace(Regex("\\'"),"")
        prepared = prepared.replace(Regex("[\\\",\\.?\\:!;\\}\\]\\[\\{\\(\\)]")," ")
        prepared = prepared.replace(Regex("\\s+")," ")

        val tokens = prepared.toLowerCase().split(" ")
        val map = HashMap<String, Int>()
        tokens.forEach { string ->
            if(string.isNotEmpty())
                map.put(string, (map[string] ?: 0) + 1)
        }
        tokenizedFiles.put(it, map)
    }

    return tokenizedFiles
}