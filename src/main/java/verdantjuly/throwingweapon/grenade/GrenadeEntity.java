package verdantjuly.throwingweapon;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;
import org.bukkit.World;

public class GrenadeEntity {

    private final Plugin plugin;
    private final Snowball grenade;
    private final Player player;

    private int fuseTicks = 100; // Default fuse time (5 seconds)

    public GrenadeEntity(Snowball grenade, Player player, Plugin plugin) {
        this.grenade = grenade;
        this.player = player;
        this.plugin = plugin;
    }

    public void startFuse() {
        // Start a task to handle the fuse countdown
        plugin.getServer().getScheduler().runTaskLater(plugin, this::explode, fuseTicks);
    }

    private void explode() {
        Location location = grenade.getLocation();

        // Trigger explosion
        World world = location.getWorld();
        if (world != null) {
            // Create explosion with damage and block-breaking behavior
            world.createExplosion(location, 4.0F, true, true); // Explosion with damage and block breaking

            // Spawn explosion particles
            world.spawnParticle(Particle.EXPLOSION, location, 1);  // Normal explosion particle effect

            // Spawn flame particles (optional, if you want to simulate fire)
            world.spawnParticle(Particle.FLAME, location, 5);  // Adds flame particles at the explosion site

            // Spawn smoke particles (optional, adds smoke after the explosion)
            world.spawnParticle(Particle.LARGE_SMOKE, location, 10);  // Adds large smoke particles

            // Apply damage to nearby living entities
            for (Entity entity : world.getNearbyEntities(location, 6.0, 6.0, 6.0)) {
                if (entity instanceof LivingEntity && entity != player) {
                    LivingEntity livingEntity = (LivingEntity) entity; // Cast to LivingEntity

                    // Apply damage (example: 4 damage points)
                    livingEntity.damage(4.0);
                }
            }

            // Remove the grenade from the world (the projectile itself)
            grenade.remove();
        }
    }

    public void setVelocity(Vector velocity) {
        grenade.setVelocity(velocity);
    }
}
