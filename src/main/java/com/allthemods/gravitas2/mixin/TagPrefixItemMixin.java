package com.allthemods.gravitas2.mixin;

import com.allthemods.gravitas2.util.GregitasUtil;
import com.allthemods.gravitas2.util.WrappingHeatHandler;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.item.TagPrefixItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.extensions.IForgeItem;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = TagPrefixItem.class, remap = false)
public abstract class TagPrefixItemMixin implements IForgeItem {

    @Shadow @Final public TagPrefix tagPrefix;

    @Shadow @Final public Material material;

    /**
     * @author Screret
     * @reason no hurty bad ingot!
     */
    @Overwrite
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (slotId < 0) return;
        if (level.isClientSide()) return;

        ItemStack newStack = GregitasUtil.tickItemHeat(stack);
        if (newStack != stack) {
            entity.getSlot(slotId).set(newStack);
        }
    }

    @Override
    public net.minecraftforge.common.capabilities.ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        if (this.tagPrefix == TagPrefix.ingotHot) {
            long materialAmount = tagPrefix.getMaterialAmount(material);
            int blastTemp = material.getBlastTemperature();
            return new WrappingHeatHandler(stack, 1 / ((this.material.getMass() * 0.0012F) * ((float) materialAmount / GTValues.M)), (blastTemp - 273.15F) * 0.6F, (blastTemp - 273.15F) * 0.8F);
        }
        return null;
    }
}