package hohserg.elegant.networking.impl;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = "elegant_networking", name = "ElegantNetworking")
public class Main {

    private static Logger log;
    public static Config config;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) throws ClassNotFoundException {
        log = event.getModLog();
        config = Init.initConfig(event.getModConfigurationDirectory());
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        Init.initPackets(log::info, log::error, Network.getNetwork()::registerChannel);

        /*
        //write
        {
            ByteBuf acc = null;
            byte[] data = null;
            if (data.length <= Short.MAX_VALUE) {
                acc.writeShort(data.length);
                acc.writeBytes(data);
            } else {
                acc.writeInt(updatedBitInt(data.length, 31, 1));
                acc.writeBytes(data);
            }
        }
        net.minecraft.network.PacketBuffer
        //read
        {
            ByteBuf acc = null;
            byte[] data;
            short firstShort = acc.readShort();
            if (firstShort<0) { //optimization of getBitShort(firstShort, 15) == 1
                acc.readerIndex(acc.readerIndex() - 2);
                int bigSize = updatedBitInt(acc.readInt(), 31, 0);
                data = new byte[bigSize];
            } else
                data = new byte[firstShort];

            acc.readBytes(data);

        }*/

    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    }
}
