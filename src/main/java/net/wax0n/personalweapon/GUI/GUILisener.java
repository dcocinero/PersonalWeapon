package net.wax0n.personalweapon.GUI;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class GUILisener implements Listener {
    private final GUIManager guiManager;

    public GUILisener(GUIManager guiManager) {
        this.guiManager = guiManager;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event){
        this.guiManager.handleClick(event);
    }
    @EventHandler
    public void onOpen(InventoryOpenEvent event){
        this.guiManager.handleOpen(event);
    }
    @EventHandler
    public void onClose(InventoryCloseEvent event){
        this.guiManager.handleClose(event);
    }
}
