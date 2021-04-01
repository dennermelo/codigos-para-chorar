package com.github.dennermelo.next.damageindicator.event;

import com.github.dennermelo.next.damageindicator.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class PlayerDamageEvent implements Listener {


    public static HashMap<Player, ArmorStand> info = new HashMap<>();

    @EventHandler(priority = EventPriority.HIGH)
    public void event(EntityDamageByEntityEvent event) {
        if (event.getDamager() != null && event.getDamager() instanceof Player && event.getEntity() != null && !(event.getEntity() instanceof ArmorStand)) {
            Player player = (Player) event.getDamager();
            if (info.containsKey(player)) {
                info.get(player).remove();
                info.remove(player);
            }
            ArmorStand stand = event.getEntity().getWorld().spawn(event.getEntity().getLocation(), ArmorStand.class);
            if (!event.getDamager().isOnGround()) {
                stand.setCustomName("§7" + event.getDamage() + "§c ❤ §f*CRITICAL*");
                new BukkitRunnable() {
                    @Override
                    public void run() {

                        if (!info.containsKey(player) || info.containsKey(player) && !info.get(player).equals(stand)) {
                            cancel();
                        }
                        if (stand.getCustomName().equals("§7" + event.getDamage() + "§c ❤ §f*CRITICAL*")) {
                            stand.setCustomName("§7" + event.getDamage() + "§c ❤ §4*CRITICAL*");
                        } else {
                            stand.setCustomName("§7" + event.getDamage() + "§c ❤ §f*CRITICAL*");
                        }
                    }
                }.runTaskTimerAsynchronously(Main.getInstance(), 0L, 3L);
            } else {
                stand.setCustomName("§7" + event.getDamage() + "§c ❤");
            }
            stand.setCustomNameVisible(true);
            stand.setVisible(false);
            stand.setGravity(false);
            info.put(player, stand);

            new BukkitRunnable() {

                @Override
                public void run() {
                    stand.teleport(event.getEntity().getLocation());
                    if (!info.containsKey(player) || info.containsKey(player) && !info.get(player).equals(stand)) {
                        Bukkit.getConsoleSender().sendMessage("cancelado.");
                        cancel();
                    }
                }
            }.runTaskTimerAsynchronously(Main.getInstance(), 0L, 1L);

            new BukkitRunnable() {
                int i = 0;

                @Override
                public void run() {
                    if (i < 3) i++;
                    if (i == 3) {
                        if (info.containsKey(player)) {
                            info.get(player).remove();
                            info.remove(player);
                        }
                        cancel();
                    }

                }
            }.runTaskTimerAsynchronously(Main.getInstance(), 0L, 20L);
        }
    }
}
