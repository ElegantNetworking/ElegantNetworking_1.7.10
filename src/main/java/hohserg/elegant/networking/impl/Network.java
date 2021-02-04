package hohserg.elegant.networking.impl;

import cpw.mods.fml.common.Loader;
import hohserg.elegant.networking.api.ClientToServerPacket;
import hohserg.elegant.networking.api.ServerToClientPacket;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;

public interface Network<PacketRepresentation> {

    Network defaultImpl = Loader.isModLoaded("codechickenlib") ? new CCLNetworkImpl() : new ForgeNetworkImpl();

    static Network getNetwork() {
        return defaultImpl;
    }

    void sendToPlayer(ServerToClientPacket serverToClientPacket, EntityPlayerMP player);

    void sendToClients(ServerToClientPacket serverToClientPacket);

    void sendPacketToAllAround(ServerToClientPacket serverToClientPacket, World world, double x, double y, double z, double range);

    void sendToDimension(ServerToClientPacket serverToClientPacket, World world);

    void sendToChunk(ServerToClientPacket serverToClientPacket, World world, int chunkX, int chunkZ);

    void sendToServer(ClientToServerPacket clientToServerPacket);

    void onReceiveClient(PacketRepresentation packetRepresent, String channel);

    void onReceiveServer(PacketRepresentation packetRepresent, EntityPlayerMP player, String channel);

    void registerChannel(String channel);

}
