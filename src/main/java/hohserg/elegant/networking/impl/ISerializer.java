package hohserg.elegant.networking.impl;

import com.google.common.base.Preconditions;
import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public interface ISerializer<Packet> extends ISerializerBase<Packet>, RegistrableSingletonSerializer {
    void serialize(Packet value, ByteBuf acc);

    Packet unserialize(ByteBuf buf);

    default void serialize_NBTTagCompound_Generic(NBTTagCompound value, ByteBuf acc) {
        Preconditions.checkNotNull(value);
        ByteBufUtils.writeTag(acc, value);
    }

    default NBTTagCompound unserialize_NBTTagCompound_Generic(ByteBuf buf) {
        NBTTagCompound value = ByteBufUtils.readTag(buf);
        return value != null ? value : new NBTTagCompound();
    }

    default void serialize_ItemStack_Generic(ItemStack value, ByteBuf acc) {
        ByteBufUtils.writeItemStack(acc, value);
    }

    default ItemStack unserialize_ItemStack_Generic(ByteBuf buf) {
        return ByteBufUtils.readItemStack(buf);
    }

    default void serialize_FluidStack_Generic(FluidStack value, ByteBuf acc) {
        serialize_Fluid_Generic(value.getFluid(), acc);
        acc.writeInt(value.amount);
        if (value.tag != null) {
            acc.writeByte(1);
            serialize_NBTTagCompound_Generic(value.tag, acc);
        } else
            acc.writeByte(0);
    }

    default FluidStack unserialize_FluidStack_Generic(ByteBuf buf) {
        Fluid fluid = unserialize_Fluid_Generic(buf);
        FluidStack stack = new FluidStack(fluid, buf.readInt());
        if (buf.readByte() == 1)
            stack.tag = unserialize_NBTTagCompound_Generic(buf);
        return stack;
    }

    default void serialize_ResourceLocation_Generic(ResourceLocation value, ByteBuf acc) {
        serialize_String_Generic(value.toString(), acc);
    }

    default ResourceLocation unserialize_ResourceLocation_Generic(ByteBuf buf) {
        return new ResourceLocation(unserialize_String_Generic(buf));
    }
}
