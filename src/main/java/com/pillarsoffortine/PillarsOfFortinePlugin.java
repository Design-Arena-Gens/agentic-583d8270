package com.pillarsoffortine;

import com.pillarsoffortine.command.FortineCommand;
import com.pillarsoffortine.pillar.PillarBuilder;
import com.pillarsoffortine.pillar.PillarProfile;
import org.bukkit.Material;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

import java.util.Locale;

public final class PillarsOfFortinePlugin extends JavaPlugin {
    private PillarProfile profile;
    private Material triggerBlock;
    private int commandRadius;
    private PillarBuilder builder;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        reloadProfile();

        builder = new PillarBuilder(profile);
        FortineCommand command = new FortineCommand(this, builder);

        PluginCommand pluginCommand = getCommand("fortine");
        if (pluginCommand != null) {
            pluginCommand.setExecutor(command);
            pluginCommand.setTabCompleter(command);
        } else {
            getLogger().severe("Command 'fortine' is not defined in plugin.yml");
        }

        getServer().getPluginManager().registerEvents(new com.pillarsoffortine.listener.PillarTriggerListener(this), this);

        getLogger().info("Pillars Of Fortine ready with pillar height " + profile.height());
    }

    public void reloadProfile() {
        FileConfiguration config = getConfig();

        int height = Math.max(3, config.getInt("pillar.height", 6));
        Material base = Material.matchMaterial(config.getString("pillar.materials.base", "POLISHED_DEEPSLATE"));
        Material body = Material.matchMaterial(config.getString("pillar.materials.body", "DEEPSLATE_BRICKS"));
        Material cap = Material.matchMaterial(config.getString("pillar.materials.cap", "GLOWSTONE"));
        Material accent = Material.matchMaterial(config.getString("pillar.materials.accent", "CRYING_OBSIDIAN"));

        if (base == null || body == null || cap == null || accent == null) {
            getLogger().warning("Invalid pillar material configuration, reverting to defaults.");
            base = Material.POLISHED_DEEPSLATE;
            body = Material.DEEPSLATE_BRICKS;
            cap = Material.GLOWSTONE;
            accent = Material.CRYING_OBSIDIAN;
        }

        triggerBlock = Material.matchMaterial(config.getString("trigger-block", "NETHERITE_BLOCK"));
        if (triggerBlock == null) {
            getLogger().warning("Invalid trigger block, defaulting to NETHERITE_BLOCK.");
            triggerBlock = Material.NETHERITE_BLOCK;
        }

        commandRadius = Math.max(3, config.getInt("command.radius", 5));

        String effectTypeName = config.getString("effect.potion.type", "RESISTANCE");
        PotionEffectType effectType = null;
        if (effectTypeName != null) {
            effectType = PotionEffectType.getByName(effectTypeName.toUpperCase(Locale.ROOT));
        }
        if (effectType == null) {
            getLogger().warning("Invalid potion type '" + effectTypeName + "', disabling aura.");
        }

        profile = new PillarProfile(
                height,
                base,
                body,
                cap,
                accent,
                config.getDouble("effect.radius", 6.0),
                effectType,
                config.getInt("effect.potion.amplifier", 1),
                config.getInt("effect.potion.duration", 200)
        );
        if (builder != null) {
            builder.updateProfile(profile);
        }
    }

    public int getCommandRadius() {
        return commandRadius;
    }

    public PillarProfile getProfile() {
        return profile;
    }

    public Material getTriggerBlock() {
        return triggerBlock;
    }

    public PillarBuilder getBuilder() {
        return builder;
    }
}
