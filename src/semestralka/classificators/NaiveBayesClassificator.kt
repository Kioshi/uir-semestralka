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
    var tableMap = HashMap<String, HashMap<String, Int>>()
    var totalFiles = 0

    override fun train(trainSet: HashMap<File, HashMap<String, Int>>) {
        super.train(trainSet)

        totalFiles = trainSet.size
        table = Array<MutableList<String?>>(trainSet.size){ArrayList()}

        trainSet.forEach { file, hashMap ->

            val category = getCategoryFromName(file.name)
            hashMap.forEach { s, count ->
                tableMap.put(category, tableMap[category] ?: HashMap())
                tableMap[category]!!.add(s)
            }

            categories.add(category)
        }
    }

    override fun calculate(hashMap: HashMap<String, Int>): Pair<String, Double> {
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

private fun HashMap<String, Int>.add(world: String) {
    this.put(world, (this[world]?:0) + 1)
}

