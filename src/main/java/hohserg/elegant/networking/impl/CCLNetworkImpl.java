package hohserg.elegant.networking.impl;

import codechicken.lib.packet.PacketCustom;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import hohserg.elegant.networking.api.ClientToServerPacket;
import hohserg.elegant.networking.api.IByteBufSerializable;
import hohserg.elegant.networking.api.ServerToClientPacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;

public class CCLNetworkImpl implements Network<PacketCustom> {
    @Override
    public void sendToPlayer(ServerToClientPacket serverToClientPacket, EntityPlayerMP player) {
        preparePacket(serverToClientPacket).sendToPlayer(player);
    }

    @Override
    public void sendToClients(ServerToClientPacket serverToClientPacket) {
        preparePacket(serverToClientPacket).sendToClients();
    }

    @Override
    public void sendPacketToAllAround(ServerToClientPacket serverToClientPacket, World world, double x, double y, double z, double range) {
        preparePacket(serverToClientPacket).sendPacketToAllAround(x, y, z, range, world.provider.dimensionId);
    }

    @Override
    public void sendToDimension(ServerToClientPacket serverToClientPacket, World world) {
        preparePacket(serverToClientPacket).sendToDimension(world.provider.dimensionId);
    }

    @Override
    public void sendToChunk(ServerToClientPacket serverToClientPacket, World world, int chunkX, int chunkZ) {
        preparePacket(serverToClientPacket).sendToChunk(world, chunkX, chunkZ);
    }

    @Override
    public void sendToServer(ClientToServerPacket packet) {
        preparePacket(packet).sendToServer();
    }

    private PacketCustom preparePacket(IByteBufSerializable packet) {
        checkSendingSide(packet);

        String packetClassName = packet.getClass().getName();
        ISerializerBase serializer = Registry.getSerializer(packetClassName);
        String channel = Registry.getChannelForPacket(packetClassName);
        Integer id = Registry.getPacketId(packetClassName);
        PacketCustom packetCustom = new PacketCustom(channel, id);

        ByteBuf buffer = Unpooled.buffer();
        serializer.serialize(packet, buffer);
        packetCustom.writeShort(buffer.readableBytes());
        packetCustom.writeByteArray(buffer.array());

        return packetCustom;
    }

    @Override
    public void onReceiveClient(PacketCustom packetRepresent, String channel) {
        this.<ServerToClientPacket>readObjectFromPacket(packetRepresent,channel)
                .onReceive(Minecraft.getMinecraft());
    }

    @Override
    public void onReceiveServer(PacketCustom packetRepresent, EntityPlayerMP player, String channel) {
        this.<ClientToServerPacket>readObjectFromPacket(packetRepresent, channel)
                .onReceive(player);
    }

    private <A> A readObjectFromPacket(PacketCustom packetRepresent, String channel) {
        int size = packetRepresent.readShort();
        ByteBuf buffer = Unpooled.buffer(size);
        buffer.writeBytes(packetRepresent.readByteArray(size));
        return (A) Registry.getSerializer(Registry.getPacketName(channel, packetRepresent.getType())).unserialize(buffer);
    }

    @Override
    public void registerChannel(String channel) {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
            PacketCustom.assignHandler(channel, (PacketCustom.IClientPacketHandler) (packet, mc, handler) -> onReceiveClient(packet,channel));
        PacketCustom.assignHandler(channel, (PacketCustom.IServerPacketHandler) (packet, sender, handler) -> onReceiveServer(packet, sender,channel));
    }
}
