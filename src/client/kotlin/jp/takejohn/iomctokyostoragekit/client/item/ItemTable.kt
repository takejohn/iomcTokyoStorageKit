package jp.takejohn.iomctokyostoragekit.client.item

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import java.lang.reflect.Type

class ItemTable(private val inner: Map<String, ItemRecord>) : Map<String, ItemRecord> by inner {
    object Deserializer : JsonDeserializer<ItemTable> {
        private val gson by lazy { Gson() }

        override fun deserialize(
            json: JsonElement,
            typeOfT: Type,
            context: JsonDeserializationContext
        ): ItemTable {
            val jsonObject: JsonObject = json.asJsonObject
            val result = HashMap<String, ItemRecord>()
            for ((key, value) in jsonObject.entrySet()) {
                result.put(key, gson.fromJson(value, ItemRecord::class.java))
            }
            return ItemTable(result)
        }
    }

    fun searchItems(query: String): ItemTable {
        val trimmedQuery = query.trim()
        if (trimmedQuery.isEmpty()) {
            return this
        }

        val matchedItems = this.filter { (key, value) ->
            key.contains(trimmedQuery, true) ||
                    value.name.contains(trimmedQuery, true) ||
                    value.group.contains(trimmedQuery, true)
        }
        return ItemTable(matchedItems)
    }
}
