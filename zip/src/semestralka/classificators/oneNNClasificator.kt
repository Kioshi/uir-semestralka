package semestralka.classificators

import java.io.File
import java.io.PrintWriter
import java.util.*

/**
 * Created by Štěpán Martínek on 27.05.2017.
 */

class oneNNClasificator : Classificator() {


    val data = HashMap<String, TreeMap<String, Int>>()


    override fun saveData(out: PrintWriter)
    {
        data.forEach { kategory, hashMap ->
            out.print("$kategory -")
            hashMap.forEach { s, i -> out.print(" $s $i") }
            out.println()
        }
    }

    override fun train(trainSet: HashMap<File, TreeMap<String, Int>>) {

        val categoryCount = HashMap<String, Int>()
        selector.filterByDocuments(trainSet)
        trainSet.forEach { file, hashMap ->
            val kategory = getCategoryFromName(file.name)
            if (kategory.isEmpty())
                return@forEach

            categoryCount.add(kategory)
            if (data[kategory] == null)
            {
                data.put(kategory, TreeMap(hashMap))
            }
            else
            {
                hashMap.forEach { key, value ->
                    data[kategory]!!.add(key, value)
                }
            }
        }
        data.forEach { s, hashMap ->
            val files = categoryCount[s]!!
            hashMap.forEach { w, count ->
                hashMap[w] = count/files
            }
        }
        selector.filterByClass(data)

    }

    override fun calculate(hashMap: TreeMap<String, Int>): Pair<String, Double> {
        val map = HashMap<String, Double>()

        data.forEach { category, treeMap ->
            var sum = 0.0
            hashMap.forEach { world, count ->
                val diff = Math.abs(count - (treeMap[world]?:0))
                sum += diff*diff
            }
            map.put(category, Math.sqrt(sum))
        }

        var cat = "unknown"
        var distance = Double.MAX_VALUE
        map.forEach { s, d ->
            if (d < distance)
            {
                cat = s
                distance = d
            }
        }

        return Pair(cat, distance)
    }

    override fun toString(): String {
        return "nn"
    }


    override fun loadModel(scanner: Scanner) {
        while(scanner.hasNextLine())
        {
            val line = scanner.nextLine()
            val sc = Scanner(line)
            val category = sc.next()
            sc.next()
            val map = TreeMap<String, Int>()
            while(sc.hasNext())
            {
                map.put(sc.next(), sc.nextInt())
            }
            data.put(category,map)

        }
    }
}

