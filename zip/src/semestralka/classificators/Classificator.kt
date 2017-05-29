package semestralka.classificators

import semestralka.Utils.prepareText
import semestralka.symptoms.Selector
import java.io.File
import java.io.PrintWriter
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


    abstract fun train(trainSet: HashMap<File, TreeMap<String, Int>>)

    fun test(testSet: HashMap<File, TreeMap<String, Int>>) {
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

    abstract fun calculate(hashMap: TreeMap<String, Int>): Pair<String, Double>


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

    fun saveModel(s: String){
        File(s).printWriter().use { out ->
            out.println(selector.toString())
            out.println(toString())
            saveData(out)
        }
    }

    abstract fun saveData(out: PrintWriter)

    abstract fun loadModel(scanner: Scanner)

    fun getFullName(): String {
        when(toString()){
            "nn" -> return "1-NN"
            "mb" -> return "Naive-Bayes"
            else -> return "unknown"
        }
    }

    fun test(testText: String): String {
        val (category, pct) = calculate(prepareText(testText))
        return category
    }
}


fun HashMap<String, Int>.add(world: String, value: Int = 1) {
    this.put(world, (this[world]?:0) + value)
}
fun TreeMap<String, Int>.add(world: String, value: Int = 1) {
    this.put(world, (this[world]?:0) + value)
}
