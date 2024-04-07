package net.wax0n.personalweapon.GUI.implement;

import net.wax0n.personalweapon.GUI.InventoryButton;
import net.wax0n.personalweapon.GUI.InventoryGUI;
import net.wax0n.personalweapon.utils.CostsAndLimits;
import net.wax0n.personalweapon.weapon.Weapon;
import net.wax0n.personalweapon.weapon.WeaponManager;
import net.wax0n.personalweapon.ymls.LoadGroupsPerms;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class UpPointsWeapon extends InventoryGUI {

    private final WeaponManager weaponManager;
    private final LoadGroupsPerms loadGroupsPerms;
    private final int invSize = 3 * 9;

    public UpPointsWeapon(WeaponManager weaponManager, LoadGroupsPerms loadGroupsPerms) {
        this.weaponManager = weaponManager;
        this.loadGroupsPerms = loadGroupsPerms;
    }

    @Override
    protected Inventory createInventory() {
        return Bukkit.createInventory(null, invSize, ChatColor.translateAlternateColorCodes('&', "&3&lWeapon Up Points"));
    }

    @Override
    public void decorate(Player player) {
        Weapon weapon = weaponManager.getWeapon(player);
        ItemStack damageUp = makeButton(Material.DIAMOND_AXE, ChatColor.DARK_RED + "Damage Lvl " +
                weapon.getDamagePoints() + "/" + CostsAndLimits.getLimitDamage(), ChatColor.YELLOW + "Cost: " +
                CostsAndLimits.getDamagePointsCost(weapon.getDamagePoints()));
        ItemStack speedUp = makeButton(Material.GOLDEN_SWORD, ChatColor.BLUE + "AttackSpeed Lvl " +
                weapon.getSpeedPoints() + "/" + CostsAndLimits.getLimitSpeed(), ChatColor.YELLOW + "Cost: " +
                CostsAndLimits.getSpeedPointsCost(weapon.getSpeedPoints()));
        ItemStack fireUp = makeButton(Material.FLINT_AND_STEEL, ChatColor.RED + "FireAspect Lvl " +
                weapon.getFirePoints() + "/" + CostsAndLimits.getLimitFire(), ChatColor.YELLOW + "Cost: " +
                CostsAndLimits.getFirePointsCost(weapon.getFirePoints()));
        ItemStack smiteUp = makeButton(Material.ROTTEN_FLESH, ChatColor.GOLD + "Smite Lvl " +
                weapon.getSmitePoints() + "/" + CostsAndLimits.getLimitSmite(), ChatColor.YELLOW + "Cost: " +
                CostsAndLimits.getSmitePointsCost(weapon.getSmitePoints()));
        ItemStack arthropodUp = makeButton(Material.SPIDER_EYE, ChatColor.DARK_PURPLE + "Bane of arthropods Lvl " +
                weapon.getArthropodPoints() + "/" + CostsAndLimits.getLimitArthropod(), ChatColor.YELLOW + "Cost: " +
                CostsAndLimits.getArthropodPointsCost(weapon.getArthropodPoints()));
        ItemStack knockbackUp = makeButton(Material.SLIME_BALL, ChatColor.GREEN + "Knockback Lvl " +
                weapon.getKnockbackPoints() + "/" + CostsAndLimits.getLimitKnockback(), ChatColor.YELLOW + "Cost: " +
                CostsAndLimits.getKnockbackPointsCost(weapon.getKnockbackPoints()));
        ItemStack lootingUp = makeButton(Material.GOLDEN_APPLE, ChatColor.YELLOW + "Looting Lvl " +
                weapon.getLootingPoints() + "/" + CostsAndLimits.getLimitLooting(), ChatColor.YELLOW + "Cost: " +
                CostsAndLimits.getLootingPointsCost(weapon.getLootingPoints()));
        ItemStack sweepUp = makeButton(Material.WOODEN_SWORD, ChatColor.AQUA + "Sweeping Edge Lvl " +
                weapon.getSweepPoints() + "/" + CostsAndLimits.getLimitSweep(), ChatColor.YELLOW + "Cost: " +
                CostsAndLimits.getSweepPointsCost(weapon.getSweepPoints()));
        ItemStack freePoints = makeButton(Material.NETHER_STAR, ChatColor.AQUA + "FreePoints: " + weapon.getFreePoints(), ChatColor.RED + "Points used: " + weapon.getUsedPoints());

        long groupTimer = loadGroupsPerms.getResetSkillTimer(player);
        ItemStack reset;
        if (weaponManager.checkWeaponResetSkillTimer(player, groupTimer)){
            reset = makeButton(Material.BARRIER, ChatColor.DARK_RED + "Reset Points " + weapon.getUsedPoints(),
                    ChatColor.GREEN + "Available");
        }else {
            reset = makeButton(Material.BARRIER, ChatColor.DARK_RED + "Reset Points " + weapon.getUsedPoints(),
                    ChatColor.GREEN + "Time left: " + LoadGroupsPerms.segToStringTime(weaponManager.leftResetTime(player, groupTimer)));
        }

        this.addButton(0, createTextureButton(damageUp, "damage"));
        this.addButton(2, createTextureButton(speedUp, "speed"));
        this.addButton(6, createTextureButton(fireUp, "fire"));
        this.addButton(8, createTextureButton(knockbackUp, "knockback"));
        this.addButton(18, createTextureButton(smiteUp, "smite"));
        this.addButton(20, createTextureButton(lootingUp, "looting"));
        this.addButton(24, createTextureButton(arthropodUp, "arthropod"));
        this.addButton(26, createTextureButton(sweepUp, "sweep"));
        this.addButton(13, createTextureButton(freePoints, "freepoints"));
        this.addButton(22, createTextureButton(reset, "reset"));

        super.decorate(player);
    }
    private InventoryButton createTextureButton(ItemStack item, String category) {
        return new InventoryButton()
                .creator(player -> item)
                .consumer(event -> {
                    Player player = (Player) event.getWhoClicked();
                    Weapon weapon = weaponManager.getWeapon(player);
                    if (category.equals("freepoints")){
                        return;
                    }
                    if (category.equals("reset")){
                        long timer = loadGroupsPerms.getResetSkillTimer(player);
                        if (!weaponManager.checkWeaponResetSkillTimer(player, timer)){
                            player.playSound(player.getLocation(), Sound.ENTITY_BLAZE_HURT, 10, 29);
                            player.sendMessage(ChatColor.DARK_RED + "U cant use this now. Time left: " +
                                    LoadGroupsPerms.segToStringTime(weaponManager.leftResetTime(player, timer)));
                            return;
                        }
                        if (weapon.getUsedPoints() == 0){
                            player.playSound(player.getLocation(), Sound.ENTITY_BLAZE_HURT, 10, 29);
                            player.sendMessage(ChatColor.DARK_RED + "U dont have points to reset");
                            return;
                        }
                        weaponManager.resetSelectedWeapon(player);
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 29);
                        this.decorate(player);
                        return;
                    }
                    Boolean canUpdate = weapon.updatePoint(category);
                    if (canUpdate){
                        weaponManager.updateWeapon(player);
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 29);
                        this.decorate(player);
                    } else {
                        player.playSound(player.getLocation(), Sound.ENTITY_BLAZE_HURT, 10, 29);
                        player.sendMessage(ChatColor.DARK_RED + "U dont have points to up this");
                    }
                });
    }
    private ItemStack makeButton(Material material, String display, String lore){
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(display);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        List<String> lorelist = new ArrayList<>();
        lorelist.add(lore);
        itemMeta.setLore(lorelist);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }


}
