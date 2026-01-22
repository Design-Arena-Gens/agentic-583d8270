package com.pillarsoffortine.pillar;

import org.bukkit.Material;
import org.bukkit.potion.PotionEffectType;

public record PillarProfile(
        int height,
        Material base,
        Material body,
        Material cap,
        Material accent,
        double effectRadius,
        PotionEffectType effectType,
        int effectAmplifier,
        int effectDuration
) {
}
