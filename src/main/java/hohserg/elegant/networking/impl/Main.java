package hohserg.elegant.networking.impl;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

import java.util.stream.Collectors;

@Mod(modid = "elegant_networking", name = "ElegantNetworking")
public class Main {

    public static Logger log;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) throws ClassNotFoundException {
        log = event.getModLog();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        Init.registerAllPackets(
                Loader.instance().getActiveModList()
                        .stream()
                        .map(mod -> new Init.ModInfo(mod.getModId(), mod.getSource()))
                        .collect(Collectors.toList()),
                log::error,
                Network.getNetwork()::registerChannel
        );
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    }
}
