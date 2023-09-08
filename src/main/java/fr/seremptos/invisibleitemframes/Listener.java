package fr.seremptos.invisibleitemframes;

import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTEntity;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;


public class Listener implements org.bukkit.event.Listener {

    private Main main;

    public Listener(Main main) {
        this.main = main;
    }


    @EventHandler
    public void onCraft(PrepareItemCraftEvent event) {
        if(event.getInventory().getMatrix().length != 9) return; // On check si on est bien dans un établi.
        ItemStack item = event.getInventory().getMatrix()[4];
        if(item == null) return;

        if(Main.frames.contains(item.getType())){
            NBTItem nbt = new NBTItem(item);
            NBTCompound coupound = nbt.getCompound("EntityTag");
            if(coupound != null && coupound.hasTag("Invisible")) {
                event.getInventory().setResult(null);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onHit(HangingBreakEvent event) {
        if (!event.isCancelled() && event.getEntity() instanceof ItemFrame itemFrame && main.getConfig().getBoolean("override-item-frame-drop", true)) {
            event.setCancelled(true);
            NBTEntity nbt = new NBTEntity(itemFrame);
            Location loc = event.getEntity().getLocation();
            if (nbt.hasTag("Invisible")) {
                loc.getWorld().dropItem(loc, Main.getInvisibleFrame(
                        new ItemStack(Material.matchMaterial(event.getEntity().getType().toString())))
                );
            }
            itemFrame.remove();
        }
    }
}
