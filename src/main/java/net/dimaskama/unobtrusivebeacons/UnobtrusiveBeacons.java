package net.dimaskama.unobtrusivebeacons;

import net.fabricmc.api.ClientModInitializer;

import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnobtrusiveBeacons implements ClientModInitializer {

    public static final String MOD_ID = "unobtrusive-beacons";
    public static final Logger LOGGER = LoggerFactory.getLogger("Unobtrusive Beacons");

    @Override
    public void onInitializeClient() {
        UnobtrusiveBeaconsConfig.HANDLER.load();
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

}