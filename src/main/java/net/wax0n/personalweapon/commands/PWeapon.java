package net.wax0n.personalweapon.commands;

import net.wax0n.personalweapon.GUI.GUIManager;
import net.wax0n.personalweapon.GUI.implement.ChangeTexture;
import net.wax0n.personalweapon.GUI.implement.SelectWeapon;
import net.wax0n.personalweapon.GUI.implement.UpPointsWeapon;
import net.wax0n.personalweapon.weapon.WeaponManager;
import net.wax0n.personalweapon.ymls.LoadGroupsPerms;
import net.wax0n.personalweapon.ymls.LoadItemTextures;
import net.wax0n.personalweapon.ymls.SaveWeapons;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PWeapon implements TabExecutor {

    WeaponManager weaponManager;
    GUIManager guiManager;
    SaveWeapons saveWeapons;
    LoadGroupsPerms loadGroupsPerms;
    LoadItemTextures loadItemTextures;

    public PWeapon(GUIManager guiManager, WeaponManager weaponManager, SaveWeapons saveWeapons,
                   LoadGroupsPerms loadGroupsPerms, LoadItemTextures loadItemTextures) {
        this.guiManager = guiManager;
        this.weaponManager = weaponManager;
        this.saveWeapons = saveWeapons;
        this.loadGroupsPerms = loadGroupsPerms;
        this.loadItemTextures = loadItemTextures;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {

            Player player = (Player) sender;

            if (!weaponManager.hasWeapons(player)){
                playerSendMessage(player, "You dont have any weapon, please use /createweapon");
                return true;
            }
            if (args.length < 1){
                if (weaponManager.checkSpecialItem(player)){
                    weaponManager.removeWeaponItem(player);
                } else {
                    weaponManager.giveWeapon(player);
                }
            } else if (args.length == 1){
                switch (args[0]) {
                    case "texture":
                        long timer = loadGroupsPerms.getResetTextureTimer(player);
                        if (!weaponManager.checkWeaponResetTextureTimer(player, timer)){
                            player.playSound(player.getLocation(), Sound.ENTITY_BLAZE_HURT, 10, 29);
                            player.sendMessage(ChatColor.DARK_RED + "U cant use this now. Time left: " +
                                    LoadGroupsPerms.segToStringTime(weaponManager.leftTextureTime(player, timer)));
                            break;
                        }
                        ChangeTexture changeTexture = new ChangeTexture(weaponManager, loadItemTextures, 1);
                        guiManager.openGUI(changeTexture, player);
                        break;

                    case "delete":
                        String nameWeapon = weaponManager.getWeapon(player).getName();
                        playerSendMessage(player, "Please use \"/pweapon delete confirm\" to definitely delete " + nameWeapon);
                        break;

                    case "info":
                        weaponManager.infoWeapon(player, player);
                        break;

                    case "rename":
                        playerSendMessage(player,  "Please use \"/pweapon rename <newname>\"");
                        break;

                    case "select":
                        if (weaponManager.getAmountWeapon(player) == 1){
                            playerSendMessage(player,  "U dont have any other weapon");
                        } else {
                            SelectWeapon selectWeapon = new SelectWeapon(weaponManager);
                            guiManager.openGUI(selectWeapon, player);
                        }
                        break;
                    case "update":
                        UpPointsWeapon upPointsWeapon = new UpPointsWeapon(weaponManager, loadGroupsPerms);
                        guiManager.openGUI(upPointsWeapon, player);
                        break;

                    default:
                        playerSendMessage(player,  "Unknown argument");
                        break;
                }
            } else if (args.length == 2) {
                switch (args [0]) {
                    case "delete":
                        if (args[1].equals("confirm")){
                            weaponManager.deleteWeapon(player);
                            weaponManager.removeWeaponItem(player);
                            saveWeapons.deleteWeapon(player, weaponManager.getSelectedWeapon(player));
                            playerSendMessage(player, ChatColor.RESET + "" + ChatColor.DARK_GREEN + "Weapon delete correctly");
                        }
                        break;

                    case "info":
                        if (Bukkit.getPlayer(args[1]) != null){
                            weaponManager.infoWeapon(player, Bukkit.getPlayer(args[1]));
                        } else {
                            player.sendMessage(ChatColor.RED + "Unknow player");
                        }

                        break;
                    case "rename":
                        weaponManager.renameWeapon(player, args[1]);
                        break;

                    default:
                        playerSendMessage(player,  "Unknown argument");
                        break;
                }
            } else {
                if (args[0].equals("rename")){
                    StringBuilder sb = new StringBuilder();
                    for (int i = 1; i < args.length; i++) {
                        sb.append(args[i] + " ");
                    }
                    String name = sb.toString().trim();

                    weaponManager.renameWeapon(player, name);

                } else{
                    playerSendMessage(player,  "Unknown argument");
                }
            }
        }
        return true;
    }

    private void playerSendMessage(Player player, String message) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&l[&6&lPersonalWeapon&b&l]"));
        player.sendMessage(ChatColor.DARK_RED + message);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> tabList = new ArrayList<>();

        if (args.length == 1){
            tabList.add("info");
            tabList.add("rename");
            tabList.add("texture");
            tabList.add("update");
            tabList.add("delete");
            tabList.add("select");
            return tabList;
        }

        if (args[1].equals("info")){
            for (Player player : Bukkit.getOnlinePlayers()){
                tabList.add(player.getName());
            }
            return tabList;
        }

        return null;
    }
}
