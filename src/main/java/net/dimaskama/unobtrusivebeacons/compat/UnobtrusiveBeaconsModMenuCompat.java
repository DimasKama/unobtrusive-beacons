package net.dimaskama.unobtrusivebeacons.compat;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.dimaskama.unobtrusivebeacons.UnobtrusiveBeaconsConfig;

public class UnobtrusiveBeaconsModMenuCompat implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parenctScreen -> UnobtrusiveBeaconsConfig.HANDLER.generateGui().generateScreen(parenctScreen);
    }

}
