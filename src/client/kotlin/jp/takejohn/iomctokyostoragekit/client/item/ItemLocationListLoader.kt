package jp.takejohn.iomctokyostoragekit.client.item

import jp.takejohn.iomctokyostoragekit.client.serialize.ItemTable
import kotlinx.serialization.json.Json
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils
import java.nio.charset.StandardCharsets
import java.util.concurrent.CompletableFuture

private const val JSON_URL = "https://raw.githubusercontent.com/webfullsympathy/iomctokyostorage/refs/heads/main/src/app/items/items.json"

object ItemLocationListLoader {
    fun loadItems(): CompletableFuture<ItemLocationList> {
        val client: HttpClient = HttpClients.createDefault()
        val request = HttpGet(JSON_URL)
        val future = CompletableFuture<ItemLocationList>()
        client.execute(request, { response ->
            val body: String = EntityUtils.toString(response.entity, StandardCharsets.UTF_8)
            val itemTable: ItemTable = Json.decodeFromString<ItemTable>(body)
            val itemLocationList: ItemLocationList = ItemLocationList.from(itemTable)
            future.complete(itemLocationList)
        })
        return future
    }
}
