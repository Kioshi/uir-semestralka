package semestralka

import semestralka.classificators.Classificator
import semestralka.classificators.NaiveBayesClassificator
import semestralka.classificators.oneNNClasificator
import semestralka.symptoms.ClassFrequency
import semestralka.symptoms.DocumentFrequency
import semestralka.symptoms.KombinedSelector
import semestralka.symptoms.Selector
import java.io.File
import java.util.*

/**
 * Created by Štěpán Martínek on 28.05.2017.
 */
object Utils {

    lateinit var classificator: Classificator

    fun getClassificator(name: String) : Classificator
    {
        when(name) {
            "nb" -> {
                println("Naive-Bayes classificator selected...")
                return NaiveBayesClassificator()
            }
            "nn" -> {
                println("1-nn classificator selected...")
                return oneNNClasificator()
            }
            else -> throw IllegalArgumentException("Please select correct classificator (nb/nn)")
        }
    }


    fun getSelector(type: String) : Selector
    {
        when(type)
        {
            "df" -> {
                println("Document Frequency selector selected...")
                return DocumentFrequency
            }
            "cf" -> {
                println("Class Frequency selector selected...")
                return ClassFrequency
            }
            "kf" -> {
                println("Combined Frequency selector selected...")
                return KombinedSelector
            }
            else -> throw IllegalArgumentException("Please select correct selector (df/cf/kf)")
        }

    }

    fun loadModel(path: String)
    {
        val scanner = Scanner(File(path))
        val selector = getSelector(scanner.nextLine())
        classificator = getClassificator(scanner.nextLine())
        classificator.addSelector(selector)
        classificator.loadModel(scanner)
    }

    fun prepareText(str: String): TreeMap<String, Int>
    {
        var prepared = str
        prepared = prepared.replace(Regex("-\n"),"")
        prepared = prepared.replace(Regex("\\'"),"")
        prepared = prepared.replace(Regex("[°\\-#/\\*\\+%\\\\\\\",\\.?\\:!;\\}\\]\\[\\{\\(\\)]")," ")
        prepared = prepared.replace(Regex("\\s+")," ")

        val tokens = prepared.toLowerCase().split(" ")
        val map = TreeMap<String, Int>()
        tokens.forEach { string ->
            if(string.isNotEmpty())
                map.put(string, (map[string] ?: 0) + 1)
        }

        return map
    }

}