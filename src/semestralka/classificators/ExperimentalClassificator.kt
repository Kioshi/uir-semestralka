package semestralka.classificators

import java.io.File
import java.util.*

/**
 * Created by Štěpán Martínek on 27.05.2017.
 */

class ExperimentalClassificator : Classificator() {
    override fun train(trainSet: HashMap<File, HashMap<String, Int>>) {
        super.train(trainSet)
        data.forEach { category, hashMap ->
            hashMap.forEach { s, i ->
                totalItems.put(s, (totalItems[s] ?: 0) + i)
            }
        }
    }

    override fun calculate(hashMap: HashMap<String, Int>): Pair<String, Double> {
        val map = HashMap<String, Double>()
        data.forEach { category, hashMap ->
            var chance = 1.0
            hashMap.forEach { s, i ->
                if (hashMap[s] != null)
                {
                    chance *= Math.max(i / totalItems[s]!!.toDouble(),1/10.0)
                }
                else if (totalItems[s] != null)
                    chance *= Math.max(1 / totalItems[s]!!.toDouble(),1/1000.0)
            }
            map.put(category, chance)
        }

        var cat = "unknown"
        var pct = 0.0
        map.forEach { s, d ->
            if (d > pct)
            {
                cat = s
                pct = d
            }
        }

        return Pair(cat, pct)
    }

    val totalItems = HashMap<String, Int>()
}

