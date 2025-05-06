package jp.takejohn.iomctokyostoragekit.client.item

import jp.takejohn.iomctokyostoragekit.client.serialize.ItemTable

class ItemLocationList(private val inner: List<ItemLocation>): Iterable<ItemLocation> by inner {
    companion object {
        fun from(itemTable: ItemTable) = ItemLocationList(itemTable.entries.map { (id, record) ->
            ItemLocation(id, record)
        })
    }

    fun searchItems(query: String): ItemLocationList {
        val trimmedQuery = query.trim()
        if (trimmedQuery.isEmpty()) {
            return this
        }

        val matchedItems = inner.filter { itemLocation ->
            itemLocation.item.toString().contains(trimmedQuery, true) ||
                    itemLocation.item.name.string.contains(trimmedQuery, true) ||
                    itemLocation.category.contains(trimmedQuery, true)
        }
        return ItemLocationList(matchedItems)
    }

    fun size(): Int {
        return inner.size
    }

    operator fun get(index: Int): ItemLocation {
        return inner[index]
    }
}
