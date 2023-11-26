package com.allthemods.gravitas2.mixin;

import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.CoilWorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.common.data.GTRecipeModifiers;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.dries007.tfc.common.capabilities.heat.HeatCapability;
import net.dries007.tfc.common.capabilities.heat.IHeatBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import javax.annotation.Nonnull;

@Mixin(value = GTRecipeModifiers.class, remap = false)
public class GTRecipeModifiersMixin {

    // make EBF need heating up, instead of immediately being at target temperature.
    // god this is evil. I love it.
    @ModifyExpressionValue(method = "ebfOverclock", at = @At(value = "INVOKE", target = "Lcom/gregtechceu/gtceu/api/block/ICoilType;getCoilTemperature()I"))
    private static int gregitas$modifyEbfHeatValue(int original, MetaMachine machine, @Nonnull GTRecipe recipe) {
        int heat = recipe.data.contains("ebf_temp") ? recipe.data.getInt("ebf_temp") : 0;
        int coilTier = 1;
        if (machine instanceof CoilWorkableElectricMultiblockMachine coilMachine) {
            heat = coilMachine.getCoilType().getCoilTemperature();
            coilTier = coilMachine.getCoilTier() + 1;
        }
        if (machine instanceof IHeatBlock heatBlock) {
            float currentTemp = heatBlock.getTemperature();
            heatBlock.setTemperature(HeatCapability.adjustTempTowards(currentTemp, heat, coilTier / 1.5f));
            return Math.round(heatBlock.getTemperature() + 273.15F);
        }
        return original;
    }
}
