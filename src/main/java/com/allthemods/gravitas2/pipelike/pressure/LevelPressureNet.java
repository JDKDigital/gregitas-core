package com.allthemods.gravitas2.pipelike.pressure;

import com.gregtechceu.gtceu.api.pipenet.LevelPipeNet;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;

public class LevelPressureNet extends LevelPipeNet<PressurePipeData, PressurePipeNet>
{

    public static LevelPressureNet getOrCreate(ServerLevel serverLevel) {
        return serverLevel.getDataStorage().computeIfAbsent(tag -> new LevelPressureNet(serverLevel, tag), () -> new LevelPressureNet(serverLevel), "gregitas_pressure_pipe_net");
    }

    public LevelPressureNet(ServerLevel serverLevel) {
        super(serverLevel);
    }

    public LevelPressureNet(ServerLevel serverLevel, CompoundTag tag) {
        super(serverLevel, tag);
    }

    @Override
    protected PressurePipeNet createNetInstance() {
        return new PressurePipeNet(this);
    }
}