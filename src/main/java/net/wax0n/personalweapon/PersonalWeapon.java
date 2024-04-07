package net.wax0n.personalweapon;

import net.wax0n.personalweapon.GUI.GUILisener;
import net.wax0n.personalweapon.GUI.GUIManager;
import net.wax0n.personalweapon.commands.CreateWeapon;
import net.wax0n.personalweapon.commands.PWeapon;
import net.wax0n.personalweapon.tasks.SaveTask;
import net.wax0n.personalweapon.weapon.Weapon;
import net.wax0n.personalweapon.weapon.WeaponListener;
import net.wax0n.personalweapon.weapon.WeaponManager;
import net.wax0n.personalweapon.ymls.ConfigMobXp;
import net.wax0n.personalweapon.ymls.LoadGroupsPerms;
import net.wax0n.personalweapon.ymls.LoadItemTextures;
import net.wax0n.personalweapon.ymls.SaveWeapons;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.logging.Level;

public final class PersonalWeapon extends JavaPlugin {

    WeaponManager weaponManager = new WeaponManager(new NamespacedKey(this, "WeaponItem"));
    SaveWeapons saveWeapons = new SaveWeapons(this);

    @Override
    public void onEnable() {
        // Plugin startup logic
        //Gui
        GUIManager guiManager = new GUIManager();
        GUILisener guiLisener = new GUILisener(guiManager);
        Bukkit.getPluginManager().registerEvents(guiLisener, this);

        // config textures
        LoadItemTextures loadItemTextures = new LoadItemTextures(this);
        ConfigMobXp configMobXp = new ConfigMobXp(this);


        // timers
        LoadGroupsPerms loadGroupsPerms = new LoadGroupsPerms(this);

        //weapons
        WeaponListener weaponListener = new WeaponListener(weaponManager, saveWeapons, configMobXp);
        Bukkit.getPluginManager().registerEvents(weaponListener, this);

        // commandos
        this.getCommand("createweapon").setExecutor(new CreateWeapon(guiManager, weaponManager, loadItemTextures,loadGroupsPerms));
        this.getCommand("pweapon").setExecutor(new PWeapon(guiManager, weaponManager, saveWeapons, loadGroupsPerms, loadItemTextures));
        this.getCommand("pweapon").setTabCompleter(new PWeapon(guiManager, weaponManager, saveWeapons, loadGroupsPerms, loadItemTextures));

        //on reboot loadweapons for online player
        for (Player player : Bukkit.getOnlinePlayers()){
            ArrayList<Weapon> weapons = saveWeapons.loadWeapons(player);
            if (!weapons.isEmpty()){
                weaponManager.loadWeaponList(player, weapons, saveWeapons.getSelectedIndex(player));
            }
        }

        BukkitTask saveTask = new SaveTask(this, weaponManager, saveWeapons).runTaskTimer(this, 36000L,
                36000L);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        for (Player player : this.getServer().getOnlinePlayers()){
            if (weaponManager.hasWeapons(player)){
                try {
                    weaponManager.removeWeaponItem(player);
                    saveWeapons.savePlayerWeapons(player, weaponManager.getListWeapons(player),
                            weaponManager.getSelectedWeapon(player));
                } catch (Exception e){
                    Bukkit.getLogger().log(Level.SEVERE, player.getName() + "Couldnt save weapon");
                }

            }
        }
    }
}
