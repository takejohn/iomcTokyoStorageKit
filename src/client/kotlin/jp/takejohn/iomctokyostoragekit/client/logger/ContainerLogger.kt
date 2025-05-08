package jp.takejohn.iomctokyostoragekit.client.logger

import com.google.gson.JsonElement
import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.JsonOps
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.block.BlockState
import net.minecraft.client.MinecraftClient
import net.minecraft.client.world.ClientWorld
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import java.time.Instant

class ContainerLogger {
    data class Record(
        /** コンテナを閉じたときのエポックミリ秒 */
        val time: Long,
        val blockPos: BlockPos,
        val blockState: BlockState,
        val items: List<ItemStack>,
        val playerPos: Vec3d
    ) {
        companion object {
            val CODEC: Codec<Record> = RecordCodecBuilder.create { instance ->
                instance.group(
                    Codec.LONG.fieldOf("time").forGetter(Record::time),
                    BlockPos.CODEC.fieldOf("blockPos").forGetter(Record::blockPos),
                    BlockState.CODEC.fieldOf("blockState").forGetter(Record::blockState),
                    ItemStack.CODEC.listOf().fieldOf("items").forGetter(Record::items),
                    Vec3d.CODEC.fieldOf("playerPos").forGetter(Record::playerPos),
                ).apply(instance, ::Record)
            }
        }
    }

    data class Interaction(
        val player: PlayerEntity,
        val world: ClientWorld,
        val hand: Hand,
        val hitResult: BlockHitResult
    )

    companion object {
        private val recordsCodec: Codec<List<Record>> = Record.CODEC.listOf()
    }

    var lastInteraction: Interaction? = null

    val records: MutableList<Record> = mutableListOf()

    fun log(items: List<ItemStack>) {
        lastInteraction?.let { (player, world, hand, hitResult) ->
            val time: Long = Instant.now().toEpochMilli()
            val blockPos: BlockPos = hitResult.blockPos
            val blockState: BlockState = world.getBlockState(blockPos)
            val playerPos: Vec3d = player.pos
            val record = Record(time, blockPos, blockState, items, playerPos)
            records.add(record)
        }
    }

    fun serialize(): DataResult<JsonElement> {
        return MinecraftClient.getInstance().world?.let {
            recordsCodec.encodeStart(it.registryManager.getOps(JsonOps.INSTANCE), records)
        } ?: DataResult.error {
            "ClientWorld is null"
        }
    }
}
