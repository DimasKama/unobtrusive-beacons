package net.dimaskama.unobtrusivebeacons.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.dimaskama.unobtrusivebeacons.UnobtrusiveBeaconsConfig;
import net.dimaskama.unobtrusivebeacons.UnobtrusiveBeamRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BeaconRenderer;
import net.minecraft.client.renderer.blockentity.state.BeaconRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(BeaconRenderer.class)
abstract class BeaconRendererMixin {

    /**
     * @author DimasKama
     * @reason custom beam rendering
     */
    @Overwrite
    public void submit(BeaconRenderState beaconRenderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        UnobtrusiveBeaconsConfig config = UnobtrusiveBeaconsConfig.HANDLER.instance();

        float opaqueMaxY = config.opaqueBeamHeight;
        float fadedMaxY = opaqueMaxY + config.fadedBeamHeight;
        float alphaMultiplier = config.alphaMultiplier;

        float y = 0;

        for (int j = 0; j < beaconRenderState.sections.size(); j++) {
            BeaconRenderState.Section section = beaconRenderState.sections.get(j);
            float sectionHeight = j == beaconRenderState.sections.size() - 1 ? 2048 : section.height();

            float sectionStart = y;
            float sectionEnd = y + sectionHeight;

            float opaqueStart = sectionStart;
            float opaqueEnd = Math.min(sectionEnd, opaqueMaxY);

            if (opaqueEnd > opaqueStart) {
                UnobtrusiveBeamRenderer.submitBeaconBeam(
                        poseStack,
                        submitNodeCollector,
                        1.0F,
                        beaconRenderState.animationTime,
                        opaqueStart,
                        opaqueEnd - opaqueStart,
                        section.color(),
                        alphaMultiplier,
                        alphaMultiplier,
                        BeaconRenderer.SOLID_BEAM_RADIUS * beaconRenderState.beamRadiusScale,
                        BeaconRenderer.BEAM_GLOW_RADIUS * beaconRenderState.beamRadiusScale
                );
            }

            float fadeStart = Math.max(sectionStart, opaqueMaxY);
            float fadeEnd = Math.min(sectionEnd, fadedMaxY);

            if (fadeEnd > fadeStart) {
                float bottomAlpha = alphaMultiplier * (1.0F - ((fadeStart - opaqueMaxY) / config.fadedBeamHeight));
                float topAlpha = alphaMultiplier * (1.0F - ((fadeEnd - opaqueMaxY) / config.fadedBeamHeight));

                UnobtrusiveBeamRenderer.submitBeaconBeam(
                        poseStack,
                        submitNodeCollector,
                        1.0F,
                        beaconRenderState.animationTime,
                        fadeStart,
                        fadeEnd - fadeStart,
                        section.color(),
                        bottomAlpha,
                        topAlpha,
                        BeaconRenderer.SOLID_BEAM_RADIUS * beaconRenderState.beamRadiusScale,
                        BeaconRenderer.BEAM_GLOW_RADIUS * beaconRenderState.beamRadiusScale
                );
            }

            y += sectionHeight;
        }
    }

}
