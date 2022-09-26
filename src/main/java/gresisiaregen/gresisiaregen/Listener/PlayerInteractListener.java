package gresisiaregen.gresisiaregen.Listener;

import gresisiaregen.gresisiaregen.Config.ConfigManager;
import gresisiaregen.gresisiaregen.CropTypes;
import gresisiaregen.gresisiaregen.Generators.GeneratorManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerInteractListener implements Listener {

    private static String color(String msg) { return ChatColor.translateAlternateColorCodes('&', msg); }

    @EventHandler
    public void PlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack tool = event.getPlayer().getInventory().getItemInMainHand();
        Block block = event.getClickedBlock();

        if(block != null) {
            if(tool.getType().equals(Material.STICK)) {
                if(tool.getItemMeta().hasDisplayName()) {
                    if(event.getAction() == Action.LEFT_CLICK_BLOCK) {
                        if(player.hasPermission("oreregen.admin")) {
                            if(ConfigManager.GetGenerators().contains(tool.getItemMeta().getDisplayName())) {
                                GeneratorManager.AddBlock(block.getLocation(), tool.getItemMeta().getDisplayName());
                                block.setType(ConfigManager.GetBlock(tool.getItemMeta().getDisplayName()));

                                for (CropTypes cropType : CropTypes.values()) {
                                    if(block.getType().equals(cropType.getMaterial())) {
                                        player.getWorld().getBlockAt(block.getLocation().add(0, -1, 0)).setType(Material.FARMLAND);

                                        Block crop = player.getWorld().getBlockAt(block.getLocation());
                                        crop.setType(ConfigManager.GetBlock(tool.getItemMeta().getDisplayName()));
                                        Ageable ageable = (Ageable) crop.getBlockData();
                                        ageable.setAge(7);
                                        crop.setBlockData(ageable);
                                    }
                                }

                                event.setCancelled(true);
                            }
                        }
                        else {
                            player.sendMessage(color("&cYou dont have permission to create a generator"));
                        }
                    }
                }
            }
        }
    }
}
