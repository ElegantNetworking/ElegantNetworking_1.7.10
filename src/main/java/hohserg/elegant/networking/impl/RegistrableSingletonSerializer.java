package hohserg.elegant.networking.impl;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

/*
pattern:

ABC_([A-z]+)

replacement:

default void serialize_$1_Generic($1 value, ByteBuf acc) {
    RegistryHandler.instance.serializeSingleton($1.class, value, acc);
}

default $1 unserialize_$1_Generic(ByteBuf buf) {
    return RegistryHandler.instance.unserializeSingleton($1.class, buf);
}
*/
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

    default void serialize_Enchantment_Generic(Enchantment value, ByteBuf acc) {
        RegistryHandler.instance.serializeSingleton(Enchantment.class, value, acc);
    }

    default Enchantment unserialize_Enchantment_Generic(ByteBuf buf) {
        return RegistryHandler.instance.unserializeSingleton(Enchantment.class, buf);
    }

    default void serialize_Potion_Generic(Potion value, ByteBuf acc) {
        RegistryHandler.instance.serializeSingleton(Potion.class, value, acc);
    }

    default Potion unserialize_Potion_Generic(ByteBuf buf) {
        return RegistryHandler.instance.unserializeSingleton(Potion.class, buf);
    }

    enum RegistryHandler implements IRegistryHandlerBase {
        instance;

        RegistryHandler() {
            register(Item.class, Item::getIdFromItem, Item::getItemById);
            register(Block.class, Block::getIdFromBlock, Block::getBlockById);
            register(Fluid.class, FluidRegistry::getFluidID, FluidRegistry::getFluid);
            register(Enchantment.class, e -> e.effectId, id -> {
                if (id >= 0 && id < Enchantment.enchantmentsList.length)
                    return Enchantment.enchantmentsList[id];
                else
                    return null;
            });
            register(Potion.class, e -> e.id, id -> {
                if (id >= 0 && id < Potion.potionTypes.length)
                    return Potion.potionTypes[id];
                else
                    return null;
            });
        }
    }
}
