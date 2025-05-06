package jp.takejohn.iomctokyostoragekit.client.highlight

import net.minecraft.client.MinecraftClient
import net.minecraft.particle.ParticleTypes
import net.minecraft.util.math.Vec3d

private const val HIGHLIGHT_DURATION = 200 // 10秒間

object HighlightMarkerClient {
    private var target: Vec3d? = null
    private var ticksRemaining = 0

    fun highlightAt(pos: Vec3d) {
        target = pos
        ticksRemaining = HIGHLIGHT_DURATION
    }

    fun tick() {
        target?.let { target ->
            MinecraftClient.getInstance().world?.addParticle(
                ParticleTypes.WHITE_SMOKE,
                target.x,
                target.y,
                target.z,
                0.0,
                0.0,
                0.0,
            )
        }
        if (ticksRemaining > 0) ticksRemaining--
        if (ticksRemaining == 0) {
            target = null
        }
    }
}
