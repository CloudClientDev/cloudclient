/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

package dev.cloudmc;

import dev.cloudmc.config.ConfigLoader;
import dev.cloudmc.config.ConfigSaver;
import dev.cloudmc.feature.mod.ModManager;
import dev.cloudmc.feature.option.OptionManager;
import dev.cloudmc.feature.setting.SettingManager;
import dev.cloudmc.gui.hudeditor.impl.HudEditor;
import dev.cloudmc.gui.hudeditor.HudEditorLoader;
import dev.cloudmc.helpers.CpsHelper;
import dev.cloudmc.helpers.font.FontHelper;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.lwjgl.opengl.Display;

import java.io.FileNotFoundException;
import java.io.IOException;

@Mod(
        modid = Cloud.modID,
        name = Cloud.modName,
        version = Cloud.modVersion,
        acceptedMinecraftVersions = "[1.8.9]"
)
public class Cloud {

    @Mod.Instance()
    public static Cloud INSTANCE;

    public static final String modID = "cloudmc";
    public static final String modName = "Cloud";
    public static final String modVersion = "1.3.3 [1.8.9]";

    public Minecraft mc = Minecraft.getMinecraft();

    public ModManager modManager;
    public SettingManager settingManager;
    public HudEditorLoader hudEditorLoader;
    public HudEditor hudEditor;
    public OptionManager optionManager;
    public FontHelper fontHelper;
    public CpsHelper cpsHelper;

    /**
     * Initializes the client
     */
    @EventHandler
    public void init(FMLInitializationEvent event) throws IOException {
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register((cpsHelper = new CpsHelper()));
        Display.setTitle(Cloud.modName + " Client " + Cloud.modVersion);

        settingManager = new SettingManager();
        modManager = new ModManager();
        optionManager = new OptionManager();
        hudEditorLoader = new HudEditorLoader();
        hudEditor = new HudEditor();

        if (!ConfigSaver.configExists()) {
            ConfigSaver.saveConfig();
        }
        try {
            ConfigLoader.loadConfig();
        }
        catch (Exception ignored) {}

        fontHelper = new FontHelper();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                ConfigSaver.saveConfig();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }));
    }
}