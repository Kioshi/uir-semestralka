package semestralka.classificators

import semestralka.symptoms.Selector
import java.io.File
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Created by Štěpán Martínek on 27.05.2017.
 */

abstract class Classificator {

    lateinit var selector: Selector

    fun addSelector(sel: Selector) {
        selector = sel
    }


    open fun train(trainSet: HashMap<File, HashMap<String, Int>>)
    {
        selector.filterByDocuments(trainSet)
        trainSet.forEach { file, hashMap ->
            val kategory = getCategoryFromName(file.name)
            if (kategory.isEmpty())
                return@forEach

            if (data[kategory] == null)
            {
                data.put(kategory, hashMap)
            }
            else
            {
                hashMap.forEach { key, value ->
                    data[kategory]!!.put(key, (data[kategory]!![key] ?: 0) + value)
                }
            }
        }
        selector.filterByClass(data)
    }

    fun test(testSet: HashMap<File, HashMap<String, Int>>) {
        var fail = 0
        var total = 0
        testSet.forEach { file, hashMap ->
            val realCategory = getCategoryFromName(file.name)
            val (category, pct) = calculate(hashMap)
            //println("File: ${file.name} - Category: $category, Match: $pct")
            total++
            if (realCategory != category)
                fail++
        }
        println("Total $total Correct ${total-fail} Missplaced $fail")
    }

    abstract fun calculate(hashMap: HashMap<String, Int>): Pair<String, Double>


    fun getCategoryFromName(name: String): String
    {
        val p = Pattern.compile("[0-9]*_([^\\._]*).*")
        val matcher: Matcher = p.matcher(name)
        if (!matcher.find())
        {
            println("Wrong filename $name")
            return "";
        }
        else
            return matcher.group(1)
    }

    fun saveModel(s: String) {
        File(s).printWriter().use { out ->

            data.forEach { kategory, hashMap ->
                out.print("$kategory -")
                hashMap.forEach { s, i -> out.print(" $s $i") }
                out.println()
            }
        }
    }

    val data = HashMap<String, HashMap<String, Int>>()

}

fun getClassificator(name: String) : Classificator
{
    when(name) {
        "nb" -> return NaiveBayesClassificator()
        else -> return ExperimentalClassificator()
    }
}