package net.wax0n.personalweapon.GUI.implement;

import net.wax0n.personalweapon.GUI.InventoryButton;
import net.wax0n.personalweapon.GUI.InventoryGUI;
import net.wax0n.personalweapon.weapon.WeaponManager;
import net.wax0n.personalweapon.ymls.LoadItemTextures;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.logging.Level;

public class SelectTexture extends InventoryGUI {

    private final String nameItem;
    private final WeaponManager weaponManager;
    private final LoadItemTextures loadItemTextures;
    private final int invSize = 6 * 9;
    private int page;
    private HashMap<Integer, ItemStack> items;


    public SelectTexture(String nameItem, WeaponManager weaponManager, LoadItemTextures loadItemTextures, int page) {
        this.nameItem = nameItem;
        this.weaponManager = weaponManager;
        this.loadItemTextures = loadItemTextures;
        // this.invSize = loadItemTextures.getSizeGUI();
        this.page = page;
    }

    @Override
    protected Inventory createInventory() {
        return Bukkit.createInventory(null, invSize, ChatColor.translateAlternateColorCodes('&', "&9&lSelect a texture"));
    }

    @Override
    public void decorate(Player player) {
        if (items != null) items.clear();
        items = this.loadItemTextures.getItems(page);
        if (items != null || !items.isEmpty()){
            for (int i = 1; i <= invSize; i++) {
                if (items.get(i) != null){
                    this.addButton(i-1, createTextureButton(items.get(i)));
                } else {
                    this.addButton(i-1, createAir());
                }
            }
        }
        super.decorate(player);
    }

    private InventoryButton createTextureButton(ItemStack item) {
        return new InventoryButton()
                .creator(player -> item)
                .consumer(event -> {
                    Player player = (Player) event.getWhoClicked();
                    if (item.getItemMeta().getDisplayName().contains("Next")){
                        page += 1;
                        this.decorate(player);
                        return;
                    } else if (item.getItemMeta().getDisplayName().contains("Prev")) {
                        page -= 1;
                        this.decorate(player);
                        return;
                    }
                    try{
                        weaponManager.createWeapon(player, item, nameItem);
                        weaponManager.giveWeapon(player);
                        player.sendMessage(ChatColor.RED + "Please use /personalweapon or /pw to call and store ur weapon");
                        player.closeInventory();
                    } catch (Exception e){
                        player.sendMessage(e.toString());
                    }
                });
    }
    private InventoryButton createAir() {
        return new InventoryButton()
                .creator(player -> new ItemStack(Material.AIR))
                .consumer(event -> {});
    }

}
