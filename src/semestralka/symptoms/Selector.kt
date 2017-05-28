package semestralka.symptoms

import java.io.File
import java.util.*



/**
 * Created by Štěpán Martínek on 27.05.2017.
 */

interface Selector
{
    fun filterByDocuments(fileMap: HashMap<File, HashMap<String, Int>>){

    }

    fun filterByClass(classMap: HashMap<String, HashMap<String, Int>>){

    }
}

object DocumentFrequency : Selector
{
    override fun filterByDocuments(fileMap: HashMap<File, HashMap<String, Int>>) {
        super.filterByDocuments(fileMap)

        val map = HashMap<String, Int>()
        fileMap.forEach { file, hashMap ->
            hashMap.forEach { s, i ->
                map.put(s, (map[s] ?: 0) + 1)
            }
        }
        var total = 0
        map.forEach { s, i ->  total+= i}
        print("Avg: ${total/map.size.toDouble()}")

        val sorted = TreeMap<String,Int>(ValueComparator(map))
        val threshold = fileMap.size / 3
        println("${fileMap.size} $threshold")

        sorted.forEach { s, i ->
            if (i < threshold)
                return@forEach
            map.remove(s)
        }
    }
}

object ClassFrequency : Selector
{
    override fun filterByClass(classMap: HashMap<String, HashMap<String, Int>>) {
        super.filterByClass(classMap)

        val map = HashMap<String, Int>()
        classMap.forEach { file, hashMap ->
            hashMap.forEach { s, i ->
                map.put(s, (map[s] ?: 0) + 1)
            }
        }
        var total = 0
        map.forEach { s, i ->  total+= i}
        var avg = total/map.size + 1

        val sorted = TreeMap<String,Int>(ValueComparator(map))
        val threshold = classMap.size / 3
        println("${classMap.size} $threshold")

        sorted.forEach { s, i ->
            if (i < threshold)
                return@forEach
            map.remove(s)
        }
    }
}

object MySelector : Selector
{
    override fun filterByDocuments(fileMap: HashMap<File, HashMap<String, Int>>) {
        super.filterByDocuments(fileMap)
        fileMap.forEach { file, hashMap ->
            val toRemove = ArrayList<String>()
            hashMap.forEach { s, i ->
                try {
                    s.toInt()
                    toRemove.add(s)
                }
                catch (ex: NumberFormatException) {}

                if (s.length < 4)
                    toRemove.add(s)
            }
            toRemove.forEach { hashMap.remove(it) }
        }

        DocumentFrequency.filterByDocuments(fileMap)
    }

    override fun filterByClass(classMap: HashMap<String, HashMap<String, Int>>) {
        super.filterByClass(classMap)
        ClassFrequency.filterByClass(classMap)
    }
}


fun getSelector(type: String) : Selector
{
    when(type)
    {
        "df" -> return DocumentFrequency
        "cf" -> return ClassFrequency
        else -> return MySelector
    }

}


internal class ValueComparator(var base: Map<String, Int>) : Comparator<String>
{
    override fun compare(a: String, b: String): Int {
        if (base[a]!! >= base[b]!!)
            return -1
        else
            return 1
    }
}
