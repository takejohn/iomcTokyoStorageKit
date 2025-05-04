package jp.takejohn.iomctokyostoragekit.client.item

import com.google.gson.JsonArray
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import net.minecraft.util.math.Vec3d
import java.lang.reflect.Type

object Vec3dDeserializer : JsonDeserializer<Vec3d> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Vec3d? {
        val jsonArray: JsonArray = json.asJsonArray
        val x = jsonArray.get(0).asDouble
        val y = jsonArray.get(1).asDouble
        val z = jsonArray.get(2).asDouble
        return Vec3d(x, y, z)
    }
}
