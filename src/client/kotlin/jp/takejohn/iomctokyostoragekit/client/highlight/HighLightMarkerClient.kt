package jp.takejohn.iomctokyostoragekit.client.highlight

import com.mojang.blaze3d.systems.RenderSystem
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext
import net.minecraft.client.render.BufferBuilder
import net.minecraft.client.render.BufferRenderer
import net.minecraft.client.render.GameRenderer
import net.minecraft.client.render.Tessellator
import net.minecraft.client.render.VertexFormat
import net.minecraft.client.render.VertexFormats
import net.minecraft.util.Identifier
import net.minecraft.util.math.Vec3d

private const val HIGHLIGHT_DURATION = 200 // 10秒間

object HighlightMarkerClient {
    private val markerTexture = Identifier.of("iomctokyostoragekit", "textures/gui/marker.png")

    private var target: Vec3d? = null
    private var ticksRemaining = 0

    fun highlightAt(pos: Vec3d) {
        target = pos
        ticksRemaining = HIGHLIGHT_DURATION
    }

    fun renderWorld(context: WorldRenderContext) {
        target?.let { target ->
            context.matrixStack()?.let { matrices ->
                val cameraPos = context.camera().pos
                val camera = context.camera()

                val x = target.x - cameraPos.x
                val y = target.y - cameraPos.y
                val z = target.z - cameraPos.z

                matrices.push()

                matrices.translate(x, y + 1.0, z)

                // カメラに向けてビルボードとして描画
                matrices.multiply(camera.rotation)

                matrices.scale(-0.25f, -0.25f, -0.25f)

                RenderSystem.enableBlend()
                // Zバッファを無効にして最前面に描画
                RenderSystem.disableDepthTest()
                RenderSystem.setShader(GameRenderer::getPositionTexProgram)
                RenderSystem.setShaderTexture(0, markerTexture)
                RenderSystem.setShaderColor(1f, 1f, 1f, 1f)

                val builder: BufferBuilder = Tessellator.getInstance().begin(
                    VertexFormat.DrawMode.QUADS,
                    VertexFormats.POSITION_TEXTURE
                )
                val matrix = matrices.peek().positionMatrix

                builder
                    .vertex(matrix, -1f, -1f, 0f).texture(1f, 0f)
                    .vertex(matrix,  1f, -1f, 0f).texture(0f, 0f)
                    .vertex(matrix,  1f,  1f, 0f).texture(0f, 1f)
                    .vertex(matrix, -1f,  1f, 0f).texture(1f, 1f)

                BufferRenderer.drawWithGlobalProgram(builder.end())

                RenderSystem.enableDepthTest()
                RenderSystem.disableBlend()
                matrices.pop()
            }
        }
    }

    fun tick() {
        if (ticksRemaining > 0) ticksRemaining--
        if (ticksRemaining == 0) {
            target = null
        }
    }
}
