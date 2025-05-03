package jp.takejohn.iomctokyostoragekit.client.item

import com.google.gson.GsonBuilder
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils
import java.nio.charset.StandardCharsets
import java.util.concurrent.CompletableFuture

private const val JSON_URL = "https://raw.githubusercontent.com/webfullsympathy/iomctokyostorage/refs/heads/main/src/app/items/items.json"

object ItemTableLoader {
    private val gson by lazy {
        GsonBuilder()
            .registerTypeAdapter(ItemTable::class.java, ItemTable.Deserializer)
            .create()
    }

    fun loadItems(): CompletableFuture<ItemTable> {
        val client: HttpClient = HttpClients.createDefault()
        val request = HttpGet(JSON_URL)
        val future = CompletableFuture<ItemTable>()
        client.execute(request, { response ->
            val body: String = EntityUtils.toString(response.entity, StandardCharsets.UTF_8)
            val itemTable: ItemTable = gson.fromJson(body, ItemTable::class.java)
            future.complete(itemTable)
        })
        return future
    }
}
