package net.dimaskama.unobtrusivebeacons;

import com.google.gson.FormattingStyle;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.autogen.*;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;

public class UnobtrusiveBeaconsConfig {

    public static final ConfigClassHandler<UnobtrusiveBeaconsConfig> HANDLER = ConfigClassHandler.createBuilder(UnobtrusiveBeaconsConfig.class)
            .id(UnobtrusiveBeacons.id("config"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve(UnobtrusiveBeacons.MOD_ID + ".json"))
                    .appendGsonBuilder(builder -> builder.setFormattingStyle(FormattingStyle.PRETTY))
                    .build())
            .build();
    private static final String BEAM_CATEGORY = "beam";

    @AutoGen(category = BEAM_CATEGORY)
    @FloatField(min = 0.0F, max = 2048.0F)
    @SerialEntry
    public float opaqueBeamHeight = 10.0F;

    @AutoGen(category = BEAM_CATEGORY)
    @FloatField(min = 0.0F, max = 2048.0F)
    @SerialEntry
    public float fadedBeamHeight = 30.0F;

    @AutoGen(category = BEAM_CATEGORY)
    @FloatSlider(min = 0.0F, max = 1.0F, step = 0.01F, format = "%.2f")
    @SerialEntry
    public float alphaMultiplier = 1.0F;

}
