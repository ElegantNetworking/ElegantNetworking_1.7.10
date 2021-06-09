package hohserg.elegant.networking.impl;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public interface RegistrableSingletonSerializer {

    default void serialize_Item_Generic(Item value, ByteBuf acc) {
        RegistryHandler.instance.serializeSingleton(Item.class, value, acc);
    }

    default Item unserialize_Item_Generic(ByteBuf buf) {
        return RegistryHandler.instance.unserializeSingleton(Item.class, buf);
    }

    default void serialize_Block_Generic(Block value, ByteBuf acc) {
        RegistryHandler.instance.serializeSingleton(Block.class, value, acc);
    }

    default Block unserialize_Block_Generic(ByteBuf buf) {
        return RegistryHandler.instance.unserializeSingleton(Block.class, buf);
    }

    default void serialize_Fluid_Generic(Fluid value, ByteBuf acc) {
        RegistryHandler.instance.serializeSingleton(Fluid.class, value, acc);
    }

    default Fluid unserialize_Fluid_Generic(ByteBuf buf) {
        return RegistryHandler.instance.unserializeSingleton(Fluid.class, buf);
    }

    enum RegistryHandler implements IRegistryHandlerBase {
        instance;

        RegistryHandler() {
            register(Item.class, Item::getIdFromItem, Item::getItemById);
            register(Block.class, Block::getIdFromBlock, Block::getBlockById);
            register(Fluid.class, FluidRegistry::getFluidID, FluidRegistry::getFluid);
        }
    }
}
