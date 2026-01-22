package com.pillarsoffortine.listener;

import com.pillarsoffortine.PillarsOfFortinePlugin;
import com.pillarsoffortine.pillar.PillarBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class PillarTriggerListener implements Listener {
    private final PillarsOfFortinePlugin plugin;

    public PillarTriggerListener(PillarsOfFortinePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onTriggerBlockPlace(BlockPlaceEvent event) {
        Material trigger = plugin.getTriggerBlock();
        if (trigger == null || event.getBlockPlaced().getType() != trigger) {
            return;
        }

        PillarBuilder builder = plugin.getBuilder();
        if (builder == null) {
            return;
        }

        builder.buildTriggeredPillar(event.getBlockPlaced().getLocation());
        event.getPlayer().sendMessage(ChatColor.LIGHT_PURPLE + "Fortine energy surges through the pillar.");
    }
}
