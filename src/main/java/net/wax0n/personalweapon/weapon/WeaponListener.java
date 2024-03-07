package net.wax0n.personalweapon.weapon;

import net.wax0n.personalweapon.ymls.ConfigMobXp;
import net.wax0n.personalweapon.ymls.SaveWeapons;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Objects;

public class WeaponListener implements Listener {
    private final WeaponManager weaponManager;
    private final SaveWeapons saveWeapons;
    private final ConfigMobXp configMobXp;

    public WeaponListener(WeaponManager weaponManager, SaveWeapons saveWeapons, ConfigMobXp configMobXp) {
        this.weaponManager = weaponManager;
        this.saveWeapons = saveWeapons;
        this.configMobXp = configMobXp;
    }

    // drop item
    @EventHandler
    public void WeaponItemDrop(PlayerDropItemEvent e) {
        if (weaponManager.isEspecialItem(e.getItemDrop().getItemStack())) {
            e.setCancelled(true);
        }
    }


    // avoid pass to a chest
    @EventHandler
    public void InvClick(InventoryClickEvent e) {

        if (e.getCurrentItem() == null) {
            return;
        }

        // idk
        if (e.getClick() == ClickType.NUMBER_KEY && e.getClickedInventory().getType() != InventoryType.PLAYER) {
            e.setCancelled(true);
        }


        // Need to add inventory player?
        if (weaponManager.isEspecialItem(e.getCurrentItem())) {
            if(e.getInventory().getType() != InventoryType.CRAFTING) {
                e.setCancelled(true);
            }
        }
    }


    // try this xd
    @EventHandler
    public void OnDeathRemove(PlayerDeathEvent e) {
        e.getDrops().removeIf(weaponManager::isEspecialItem);
    }


    // add xp on kill mob
    @EventHandler
    public void killMob(EntityDeathEvent e){
        Player killer = e.getEntity().getKiller();
        if (killer == null) return;
        //if (!weaponManager.hasWeapons(killer)) return;
        ItemStack item = killer.getInventory().getItemInMainHand();
        if (weaponManager.isEspecialItem(item)){
            Weapon weapon = weaponManager.getWeapon(killer);
            String mob = e.getEntity().getType().name();
            if (configMobXp.getMobs().get(mob) != null){
                int actLvl = weapon.getLevel();
                weapon.addXp(configMobXp.getMobs().get(mob));
                Boolean upLvl = weapon.getLevel() != actLvl;
                if (upLvl){
                    weaponManager.updateWeapon(killer);
                    killer.sendMessage(ChatColor.GREEN + "Your weapon reached level: " + weapon.getLevel());
                    killer.sendMessage(ChatColor.LIGHT_PURPLE + "U have " + ChatColor.GOLD + weapon.getFreePoints() + ChatColor.LIGHT_PURPLE + " points please use /pweapon update");
                    killer.playSound(killer.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 29);
                }
            }
        }
    }


    // load weapon on join
    @EventHandler
    public void onJoinServer(PlayerJoinEvent e){
        Player player = e.getPlayer();
        player.setResourcePack("https://www.dropbox.com/scl/fo/w4sxsgubyyhm3tmt34pdx/h?rlkey=l0gkfm9ip3u0n2q5yu4bc8kr1&dl=1");
        weaponManager.removeWeaponItem(player);
        ArrayList<Weapon> weapons = saveWeapons.loadWeapons(player);
        if (!weaponManager.hasWeapons(player) && !weapons.isEmpty()){
            weaponManager.loadWeaponList(player, weapons, saveWeapons.getSelectedIndex(player));
        }
    }


    // save weapon on left
    @EventHandler
    public void onLeftServer(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        if (weaponManager.hasWeapons(player)) {
            saveWeapons.savePlayerWeapons(player, weaponManager.getListWeapons(player), weaponManager.getSelectedWeapon(player));
            weaponManager.removeWeaponItem(player);
        }
    }

    // save weapon on kick
    @EventHandler
    public void onKickServer(PlayerKickEvent e){
        Player player = e.getPlayer();
        if (weaponManager.hasWeapons(player)){
            saveWeapons.savePlayerWeapons(player, weaponManager.getListWeapons(player), weaponManager.getSelectedWeapon(player));
            weaponManager.removeWeaponItem(player);
        }
    }

    @EventHandler
    public void onItemFrameInteract(PlayerInteractEntityEvent e) {
        if(e.getRightClicked() instanceof ItemFrame &&
                weaponManager.isEspecialItem(e.getPlayer().getInventory().getItemInMainHand())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDecoratePotInteract(PlayerInteractEvent e) {
        if (e.getClickedBlock() == null) return;
        if(e.getClickedBlock().getType() == Material.DECORATED_POT &&
                weaponManager.isEspecialItem(e.getItem())) {
            e.setCancelled(true);
        }
    }


}
