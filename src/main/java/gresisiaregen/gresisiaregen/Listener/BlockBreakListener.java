package gresisiaregen.gresisiaregen.Listener;

import com.archyx.aureliumskills.api.AureliumAPI;
import com.archyx.aureliumskills.skills.Skills;
import gresisiaregen.gresisiaregen.Config.ConfigManager;
import gresisiaregen.gresisiaregen.CropTypes;
import gresisiaregen.gresisiaregen.Generators.GeneratorManager;
import gresisiaregen.gresisiaregen.GresisiaRegen;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

public class BlockBreakListener implements Listener {

    private static String color(String msg) { return ChatColor.translateAlternateColorCodes('&', msg); }

    @EventHandler
    public void OnBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if(!item.getType().equals(Material.STICK)) {
            if(GeneratorManager.GeneratorAtLocation(block.getLocation())) {
                String key = GeneratorManager.GetGeneratorName(block.getLocation());

                if(player.hasPermission(ConfigManager.GetPermission(key))) {
                    if(!block.getType().equals(ConfigManager.GetBlock(key))) {
                        player.sendMessage(color("&cBlock is regenerating"));
                        event.setCancelled(true);
                        player.getWorld().getBlockAt(block.getLocation()).setType(ConfigManager.GetReplacement(key));
                    }
                    else {
                        if(ConfigManager.GetDoubleDrops(player, key)) {
                            if(ConfigManager.GetDoubleDrops(key)) {
                                ItemStack drop = new ItemStack(ConfigManager.GetDrop(key), ConfigManager.GetAmountDrop(key) + 1);
                                player.getInventory().addItem(drop);
                            }
                        }
                        else {
                            ItemStack drop = new ItemStack(ConfigManager.GetDrop(key), ConfigManager.GetAmountDrop(key));
                            player.getInventory().addItem(drop);
                        }

                        event.setCancelled(true);

                        for (CropTypes cropType : CropTypes.values()) {
                            if(block.getType().equals(cropType.getMaterial())) {

                                BukkitTask task = Bukkit.getScheduler().runTaskLater(GresisiaRegen.main, () -> {
                                    player.getWorld().getBlockAt(block.getLocation().add(0, -1, 0)).setType(Material.FARMLAND);

                                    Block crop = player.getWorld().getBlockAt(block.getLocation());
                                    crop.setType(ConfigManager.GetBlock(key));
                                    Ageable ageable = (Ageable) crop.getBlockData();
                                    ageable.setAge(7);
                                    crop.setBlockData(ageable);

                                }, ConfigManager.GetDelay(key));

                                AureliumAPI.addXp(player, ConfigManager.GetSkillType(key), ConfigManager.GetExp(key));
                                break;
                            }
                            else {
                                BukkitTask task = Bukkit.getScheduler().runTaskLater(GresisiaRegen.main, () -> {
                                    Location location = block.getLocation();

                                    player.getWorld().getBlockAt(location).setType(ConfigManager.GetBlock(key));

                                }, ConfigManager.GetDelay(key));

                                AureliumAPI.addXp(player, ConfigManager.GetSkillType(key), ConfigManager.GetExp(key));
                                break;
                            }
                        }
                    }

                    block.setType(ConfigManager.GetReplacement(key));
                }
                else {
                    event.setCancelled(true);
                    player.sendMessage(color("&cYou dont have permission to use this generator"));
                }
            }
        }
    }
}
