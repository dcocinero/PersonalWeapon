package net.wax0n.personalweapon.GUI.implement;

import net.wax0n.personalweapon.GUI.InventoryButton;
import net.wax0n.personalweapon.GUI.InventoryGUI;
import net.wax0n.personalweapon.weapon.Weapon;
import net.wax0n.personalweapon.weapon.WeaponManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class SelectWeapon extends InventoryGUI {

    private final WeaponManager weaponManager;

    public SelectWeapon(WeaponManager weaponManager) {
        this.weaponManager = weaponManager;
    }

    @Override
    protected Inventory createInventory() {
        return Bukkit.createInventory(null, 9, ChatColor.translateAlternateColorCodes('&', "&9&lSelect Weapon"));
    }

    @Override
    public void decorate(Player player) {
        ArrayList<Weapon> weaponList = weaponManager.getListWeapons(player);
        for (int i = 0; i < weaponList.size(); i++){
            this.addButton(i, createTextureButton(weaponList.get(i)));
        }
        super.decorate(player);
    }

    private InventoryButton createTextureButton(ItemStack item) {
        return new InventoryButton()
                .creator(player -> item)
                .consumer(event -> {
                    Player player = (Player) event.getWhoClicked();
                    int index = event.getSlot();
                    weaponManager.changeSelectedWeapon(player, index);
                    weaponManager.removeWeaponItem(player);
                    weaponManager.giveWeapon(player);
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 29);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&aYour selected weapon is " + "&r" +
                            weaponManager.getWeapon(player).getName()));
                    player.closeInventory();
                });
    }
}
