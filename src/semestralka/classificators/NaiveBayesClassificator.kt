package semestralka.classificators

import java.io.File
import java.util.*

/**
 * Created by Štěpán Martínek on 28.05.2017.
 */

class NaiveBayesClassificator : Classificator()
{
    lateinit var table: Array<MutableList<String?>>
    val categories = HashMap<String, Int>()
    var tableMap = HashMap<String, TreeMap<String, Int>>()
    var totalFiles = 0

    override fun saveModel(s: String) {
        TODO("implement")
    }

    override fun train(trainSet: HashMap<File, TreeMap<String, Int>>) {

        totalFiles = trainSet.size
        table = Array<MutableList<String?>>(trainSet.size){ArrayList()}

        selector.filterByDocuments(trainSet)
        trainSet.forEach { file, hashMap ->

            val category = getCategoryFromName(file.name)
            hashMap.forEach { s, count ->
                tableMap.put(category, tableMap[category] ?: TreeMap())
                tableMap[category]!!.add(s)
            }

            categories.add(category)
        }
        selector.filterByClass(tableMap)
    }

    override fun calculate(hashMap: TreeMap<String, Int>): Pair<String, Double> {
        val chances = HashMap<String,Double>()

        hashMap.forEach { world, `_` ->
            categories.forEach iner@ { category, count ->
                var chance = chances[category] ?: (count / totalFiles.toDouble())
                chance *= (tableMap[category]!![world] ?: 1 ) / count.toDouble()
                chances.put(category, chance)
            }
        }

        var cat = "unknown"
        var pct = 0.0
        chances.forEach { c, d ->
            if (d > pct)
            {
                cat = c
                pct = d
            }
        }

        return Pair(cat, pct)
    }
}

