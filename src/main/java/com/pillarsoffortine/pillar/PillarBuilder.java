package com.pillarsoffortine.pillar;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

public class PillarBuilder {
    private static final Set<BlockFace> CARDINAL_FACES = EnumSet.of(BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST);
    private PillarProfile profile;

    public PillarBuilder(PillarProfile profile) {
        this.profile = Objects.requireNonNull(profile, "profile");
    }

    public void updateProfile(PillarProfile profile) {
        this.profile = Objects.requireNonNull(profile, "profile");
    }

    public void buildTriggeredPillar(Location baseLocation) {
        if (!isValidLocation(baseLocation)) {
            return;
        }
        Location snappedBase = baseLocation.toBlockLocation();
        buildPillar(snappedBase);
        applyAura(snappedBase.add(0.5, profile.height(), 0.5));
        playImpact(snappedBase);
        emitSpiral(snappedBase);
    }

    public void buildFormation(Location center, int radius) {
        if (!isValidLocation(center)) {
            return;
        }

        World world = center.getWorld();
        int[][] offsets = new int[][]{
                {radius, radius},
                {-radius, radius},
                {radius, -radius},
                {-radius, -radius}
        };

        for (int[] offset : offsets) {
            Location pillarBase = resolveGround(world, center, offset[0], offset[1]);
            buildPillar(pillarBase);
            emitSpiral(pillarBase);
        }

        applyAura(center.clone().add(0.5, profile.height(), 0.5));
        world.playSound(center, Sound.BLOCK_BEACON_ACTIVATE, SoundCategory.BLOCKS, 1.2f, 0.9f);
        world.spawnParticle(Particle.PORTAL, center, 120, radius * 0.5, profile.height() * 0.5, radius * 0.5, 0.2);
    }

    private void buildPillar(Location baseLocation) {
        World world = baseLocation.getWorld();
        if (world == null) {
            return;
        }

        int minY = world.getMinHeight();
        int maxY = world.getMaxHeight() - 1;
        int baseY = Math.max(minY, Math.min(baseLocation.getBlockY(), maxY));

        Block baseBlock = world.getBlockAt(baseLocation.getBlockX(), baseY, baseLocation.getBlockZ());
        baseBlock.setType(profile.base(), false);

        for (int level = 1; level < profile.height() - 1; level++) {
            Block bodyBlock = baseBlock.getRelative(0, level, 0);
            if (bodyBlock.getY() > maxY) {
                break;
            }
            bodyBlock.setType(profile.body(), false);
        }

        Block capBlock = baseBlock.getRelative(0, profile.height() - 1, 0);
        if (capBlock.getY() <= maxY) {
            capBlock.setType(profile.cap(), false);
        }

        int accentLevel = Math.min(profile.height() - 2, Math.max(1, profile.height() / 2));
        Block accentOrigin = baseBlock.getRelative(0, accentLevel, 0);
        for (BlockFace face : CARDINAL_FACES) {
            Block accentBlock = accentOrigin.getRelative(face);
            accentBlock.setType(profile.accent(), false);
        }
    }

    private void applyAura(Location center) {
        if (profile.effectType() == null) {
            return;
        }
        World world = center.getWorld();
        if (world == null) {
            return;
        }

        double radiusSquared = profile.effectRadius() * profile.effectRadius();
        for (Player player : world.getPlayers()) {
            if (player.getLocation().distanceSquared(center) <= radiusSquared) {
                PotionEffect effect = new PotionEffect(
                        profile.effectType(),
                        profile.effectDuration(),
                        profile.effectAmplifier(),
                        true,
                        true,
                        true
                );
                player.addPotionEffect(effect);
                player.playSound(center, Sound.ITEM_TOTEM_USE, SoundCategory.PLAYERS, 0.5f, 1.4f);
            }
        }
    }

    private void playImpact(Location base) {
        World world = base.getWorld();
        if (world == null) {
            return;
        }
        world.playSound(base.clone().add(0.5, 0.5, 0.5), Sound.BLOCK_ANVIL_PLACE, SoundCategory.BLOCKS, 1.3f, 0.6f);
        world.playSound(base.clone().add(0.5, profile.height(), 0.5), Sound.BLOCK_BEACON_POWER_SELECT, SoundCategory.BLOCKS, 0.8f, 1.6f);
    }

    private void emitSpiral(Location base) {
        World world = base.getWorld();
        if (world == null) {
            return;
        }
        Location particleOrigin = base.clone().add(0.5, 0, 0.5);
        for (double y = 0; y <= profile.height(); y += 0.5) {
            double angle = y * Math.PI * 0.8;
            double radius = 0.6;
            double x = Math.cos(angle) * radius;
            double z = Math.sin(angle) * radius;
            world.spawnParticle(Particle.END_ROD, particleOrigin.clone().add(x, y, z), 1, 0, 0, 0, 0);
        }
    }

    private Location resolveGround(World world, Location center, int offsetX, int offsetZ) {
        int targetX = center.getBlockX() + offsetX;
        int targetZ = center.getBlockZ() + offsetZ;
        Block highest = world.getHighestBlockAt(targetX, targetZ);
        return highest.getLocation();
    }

    private boolean isValidLocation(Location location) {
        return location != null && location.getWorld() != null;
    }
}
