package com.pillarsoffortine.command;

import com.pillarsoffortine.PillarsOfFortinePlugin;
import com.pillarsoffortine.pillar.PillarBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FortineCommand implements CommandExecutor, TabCompleter {
    private final PillarsOfFortinePlugin plugin;
    private final PillarBuilder builder;

    public FortineCommand(PillarsOfFortinePlugin plugin, PillarBuilder builder) {
        this.plugin = plugin;
        this.builder = builder;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("pillarsoffortine.reload")) {
                sender.sendMessage(ChatColor.RED + "You lack permission to reload Pillars Of Fortine.");
                return true;
            }
            plugin.reloadConfig();
            plugin.reloadProfile();
            sender.sendMessage(ChatColor.GREEN + "Pillars Of Fortine configuration reloaded.");
            return true;
        }

        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can conjure Fortine structures.");
            return true;
        }

        if (!player.hasPermission("pillarsoffortine.use")) {
            player.sendMessage(ChatColor.RED + "You are not attuned to Fortine magic.");
            return true;
        }

        if (args.length == 0) {
            builder.buildFormation(player.getLocation(), plugin.getCommandRadius());
            player.sendMessage(ChatColor.AQUA + "Four Fortine pillars rise to guard your position.");
            return true;
        }

        if (args[0].equalsIgnoreCase("pillar")) {
            Location baseLocation = player.getLocation().clone().add(0, -1, 0);
            builder.buildTriggeredPillar(baseLocation);
            player.sendMessage(ChatColor.GOLD + "A Fortine pillar emerges beneath you.");
            return true;
        }

        try {
            int radius = Math.max(3, Integer.parseInt(args[0]));
            builder.buildFormation(player.getLocation(), radius);
            player.sendMessage(ChatColor.DARK_AQUA + "Fortine pillars reposition to radius " + radius + ".");
        } catch (NumberFormatException exception) {
            sender.sendMessage(ChatColor.RED + "Usage: /fortine [pillar|reload|radius]");
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            List<String> completions = new ArrayList<>();
            completions.add("pillar");
            if (sender.hasPermission("pillarsoffortine.reload")) {
                completions.add("reload");
            }
            return completions;
        }
        return Collections.emptyList();
    }
}
