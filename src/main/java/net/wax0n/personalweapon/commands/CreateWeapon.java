package net.wax0n.personalweapon.commands;

import net.wax0n.personalweapon.GUI.GUIManager;
import net.wax0n.personalweapon.GUI.implement.SelectTexture;
import net.wax0n.personalweapon.weapon.WeaponManager;
import net.wax0n.personalweapon.ymls.LoadGroupsPerms;
import net.wax0n.personalweapon.ymls.LoadItemTextures;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class CreateWeapon implements CommandExecutor {

    private final GUIManager guiManager;
    private final WeaponManager weaponManager;
    private final LoadItemTextures loadItemTextures;
    private final LoadGroupsPerms loadGroupsPerms;

    public CreateWeapon(GUIManager guiManager, WeaponManager weaponManager, LoadItemTextures loadItemTextures, LoadGroupsPerms loadGroupsPerms) {
        this.guiManager = guiManager;
        this.weaponManager = weaponManager;
        this.loadItemTextures = loadItemTextures;
        this.loadGroupsPerms = loadGroupsPerms;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (weaponManager.getListWeapons(player).size() < loadGroupsPerms.getAmountWeapon(player)){
                if (args.length < 1) {
                    playerSendMessage(player, "Use: /createweapon <name>");
                }else {
                    // To create a string with spaces to set name
                    StringBuilder sb = new StringBuilder();
                    for (String s : args) {
                        sb.append(s + " ");
                    }
                    String name = sb.toString().trim();

                    // open gui
                    SelectTexture GUI = new SelectTexture(name, weaponManager, loadItemTextures, 1);
                    this.guiManager.openGUI(GUI, player);

                }
            }else {
                playerSendMessage(player, "U dont have permission to create more weapons");
                return true;
            }
        }else {
            sender.sendMessage("Only players can use this command");
        }
        return true;
    }

    private void playerSendMessage(Player player, String message) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&l[&6&lPersonalWeapon&b&l]"));
        player.sendMessage(ChatColor.DARK_RED + message);
    }
}
