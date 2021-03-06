package semestralka.symptoms

import semestralka.classificators.add
import java.io.File
import java.util.*



/**
 * Created by Štěpán Martínek on 27.05.2017.
 */

interface Selector
{
    fun filterByDocuments(fileMap: HashMap<File, TreeMap<String, Int>>){

    }

    fun filterByClass(classMap: HashMap<String, TreeMap<String, Int>>){

    }

    fun getFullName(): String {
        when(toString()){
            "df" -> return "Document frequency"
            "cf" -> return "Class frequency"
            "kf" -> return "Combined frequency"
            else -> return "unknown"
        }
    }
}

object DocumentFrequency : Selector
{
    override fun filterByDocuments(fileMap: HashMap<File, TreeMap<String, Int>>) {
        super.filterByDocuments(fileMap)

        val map = HashMap<String, Int>()
        fileMap.forEach { file, hashMap ->
            hashMap.forEach { s, i ->
                map.add(s)
            }
        }
        var total = 0
        map.forEach { s, i ->  total+= i}

        val threshold = fileMap.size / 3

        map.forEach { s, i ->
            if (i < threshold)
                return@forEach
            fileMap.forEach { file, treeMap ->
                treeMap.remove(s)
            }
        }
    }
    override fun toString(): String {
        return "df"
    }
}

object ClassFrequency : Selector
{
    override fun filterByClass(classMap: HashMap<String, TreeMap<String, Int>>) {
        super.filterByClass(classMap)

        val map = HashMap<String, Int>()
        classMap.forEach { clas, hashMap ->
            hashMap.forEach { s, i ->
                map.add(s)
            }
        }
        var total = 0
        map.forEach { s, i ->  total+= i}

        val threshold = classMap.size / 2

        map.forEach { s, i ->
            if (i < threshold)
                return@forEach
            classMap.forEach { klass, treeMap ->
                treeMap.remove(s)
            }
        }
    }
    override fun toString(): String {
        return "cf"
    }
}

object KombinedSelector : Selector
{
    override fun filterByDocuments(fileMap: HashMap<File, TreeMap<String, Int>>) {
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

    override fun filterByClass(classMap: HashMap<String, TreeMap<String, Int>>) {
        super.filterByClass(classMap)
        ClassFrequency.filterByClass(classMap)
    }
    override fun toString(): String {
        return "kf"
    }
}

