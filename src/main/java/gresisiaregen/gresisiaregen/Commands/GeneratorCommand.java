package gresisiaregen.gresisiaregen.Commands;

import gresisiaregen.gresisiaregen.Config.ConfigManager;
import gresisiaregen.gresisiaregen.Generators.GeneratorManager;
import gresisiaregen.gresisiaregen.GresisiaRegen;
import jdk.nashorn.internal.runtime.regexp.joni.Config;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GeneratorCommand implements CommandExecutor {

    private static String color(String msg) { return ChatColor.translateAlternateColorCodes('&', msg); }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;

        if(player.hasPermission("oregen.admin")) {
            if(args.length > 0) {
                if(args[0].equalsIgnoreCase("tool")) {
                    if(args.length > 1) {
                        if(ConfigManager.GetGenerators().contains(args[1])) {
                            String key = args[1];
                            ItemStack generatorTool = new ItemStack(Material.STICK);
                            ItemMeta generatorToolMeta = generatorTool.getItemMeta();

                            generatorToolMeta.setDisplayName(key);
                            generatorTool.setItemMeta(generatorToolMeta);

                            player.getInventory().addItem(generatorTool);
                        }
                        else {
                            player.sendMessage(color("&cThere is no generator by that name"));
                        }
                    }
                    else {
                        player.sendMessage(color("&cMake sure you wrote the command correctly"));
                    }
                }
                else if(args[0].equalsIgnoreCase("remove")) {
                    Block block = player.getTargetBlock(null, 5);
                    GeneratorManager.RemoveBlock(block.getLocation());
                }
                else {
                    player.sendMessage(color("&cMake sure you wrote the command correctly"));
                }
            }
            else {
                player.sendMessage(color("&cMake sure you wrote the command correctly"));
            }
        }
        else {
            player.sendMessage(color("&cYou dont have permission to use this command"));
        }

        return false;
    }
}
