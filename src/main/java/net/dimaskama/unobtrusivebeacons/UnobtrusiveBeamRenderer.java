package net.dimaskama.unobtrusivebeacons;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BeaconRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;

public class UnobtrusiveBeamRenderer {

    public static final RenderPipeline RENDER_PIPELINE = RenderPipelines.register(
            RenderPipeline.builder(RenderPipelines.BEACON_BEAM_SNIPPET)
                    .withLocation(UnobtrusiveBeacons.id("pipeline/unobstructive_beacon_beam"))
                    .withBlend(BlendFunction.TRANSLUCENT)
                    .build()
    );
    public static final RenderType RENDER_TYPE = RenderType.create(
            "unobstructive_beacon_beam",
            1536,
            false,
            true,
            RENDER_PIPELINE,
            RenderType.CompositeState.builder()
                    .setTextureState(new RenderStateShard.TextureStateShard(BeaconRenderer.BEAM_LOCATION, false))
                    .setOutputState(RenderType.ITEM_ENTITY_TARGET)
                    .createCompositeState(false)
    );

    public static void submitBeaconBeam(
            PoseStack poseStack,
            SubmitNodeCollector nodeCollector,
            float partialTick,
            float time,
            float startY,
            float height,
            int color,
            float bottomAlpha,
            float topAlpha,
            float innerRadius,
            float outerGlowRadius
    ) {
        float endY = startY + height;

        poseStack.pushPose();
        poseStack.translate(0.5, 0.0, 0.5);

        float animationDirection = height < 0 ? time : -time;
        float textureScroll = Mth.frac(animationDirection * 0.2F - Mth.floor(animationDirection * 0.1F));

        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(time * 2.25F - 45.0F));

        float minV = -1.0F + textureScroll;
        float maxV = height * partialTick * (0.5F / innerRadius) + minV;

        nodeCollector.submitCustomGeometry(
                poseStack,
                RENDER_TYPE,
                (pose, vertexConsumer) -> renderPart(
                        pose,
                        vertexConsumer,
                        multiplyAlpha(color, bottomAlpha),
                        multiplyAlpha(color, topAlpha),
                        startY,
                        endY,
                        0.0F, innerRadius,
                        innerRadius, 0.0F,
                        -innerRadius, 0.0F,
                        0.0F, -innerRadius,
                        0.0F, 1.0F,
                        maxV, minV
                )
        );

        poseStack.popPose();

        float outerMinV = -1.0F + textureScroll;
        float outerMaxV = height * partialTick + minV;
        int outerBaseColor = ARGB.color(32, color);

        nodeCollector.submitCustomGeometry(
                poseStack,
                RENDER_TYPE,
                (pose, vertexConsumer) -> renderPart(
                        pose,
                        vertexConsumer,
                        multiplyAlpha(outerBaseColor, bottomAlpha),
                        multiplyAlpha(outerBaseColor, topAlpha),
                        startY,
                        endY,
                        -outerGlowRadius, -outerGlowRadius,
                        outerGlowRadius, -outerGlowRadius,
                        -outerGlowRadius, outerGlowRadius,
                        outerGlowRadius, outerGlowRadius,
                        0.0F, 1.0F,
                        outerMaxV, outerMinV
                )
        );

        poseStack.popPose();
    }

    private static int multiplyAlpha(int color, float alphaMultiplier) {
        return ARGB.color(ARGB.alphaFloat(color) * alphaMultiplier, color);
    }

    private static void renderPart(
            PoseStack.Pose pose,
            VertexConsumer consumer,
            int bottomColor,
            int topColor,
            float minY, float maxY,
            float x1, float z1,
            float x2, float z2,
            float x3, float z3,
            float x4, float z4,
            float minU, float maxU,
            float minV, float maxV
    ) {
        renderQuad(pose, consumer, bottomColor, topColor, minY, maxY, x1, z1, x2, z2, minU, maxU, minV, maxV);
        renderQuad(pose, consumer, bottomColor, topColor, minY, maxY, x4, z4, x3, z3, minU, maxU, minV, maxV);
        renderQuad(pose, consumer, bottomColor, topColor, minY, maxY, x2, z2, x4, z4, minU, maxU, minV, maxV);
        renderQuad(pose, consumer, bottomColor, topColor, minY, maxY, x3, z3, x1, z1, minU, maxU, minV, maxV);
    }

    private static void renderQuad(
            PoseStack.Pose pose,
            VertexConsumer consumer,
            int bottomColor,
            int topColor,
            float minY, float maxY,
            float minX, float minZ,
            float maxX, float maxZ,
            float minU, float maxU,
            float minV, float maxV
    ) {
        addVertex(pose, consumer, topColor, maxY, minX, minZ, maxU, minV);
        addVertex(pose, consumer, bottomColor, minY, minX, minZ, maxU, maxV);
        addVertex(pose, consumer, bottomColor, minY, maxX, maxZ, minU, maxV);
        addVertex(pose, consumer, topColor, maxY, maxX, maxZ, minU, minV);
    }

    private static void addVertex(
            PoseStack.Pose pose,
            VertexConsumer consumer,
            int color,
            float y, float x, float z,
            float u, float v
    ) {
        consumer.addVertex(pose, x, y, z)
                .setColor(color)
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(0xF000F0)
                .setNormal(pose, 0.0F, 1.0F, 0.0F);
    }

}
