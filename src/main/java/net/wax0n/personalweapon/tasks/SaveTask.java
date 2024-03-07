package net.wax0n.personalweapon.tasks;

import net.wax0n.personalweapon.PersonalWeapon;
import net.wax0n.personalweapon.weapon.WeaponManager;
import net.wax0n.personalweapon.ymls.SaveWeapons;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class SaveTask extends BukkitRunnable {

    PersonalWeapon plugin;
    WeaponManager weaponManager;
    SaveWeapons saveWeapons;

    public SaveTask(PersonalWeapon plugin, WeaponManager weaponManager, SaveWeapons saveWeapons) {
        this.plugin = plugin;
        this.weaponManager = weaponManager;
        this.saveWeapons = saveWeapons;
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()){
            if (weaponManager.hasWeapons(player)){
                saveWeapons.savePlayerWeapons(player, weaponManager.getListWeapons(player), weaponManager.getSelectedWeapon(player));
            }
        }
        weaponManager.clearWeapons();
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&b&l[&6&lPersonalWeapon&b&l]"));
        Bukkit.broadcastMessage(ChatColor.DARK_GREEN + "Saving weapons...");
        for (Player player : Bukkit.getOnlinePlayers()){
            if (saveWeapons.loadWeapons(player).isEmpty()) continue;
            weaponManager.loadWeaponList(player, saveWeapons.loadWeapons(player), saveWeapons.getSelectedIndex(player));
            weaponManager.removeWeaponItem(player);
            weaponManager.giveWeapon(player);
        }
    }
}
