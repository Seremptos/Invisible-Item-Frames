package fr.seremptos.invisibleitemframes;

import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;


public class Listener implements org.bukkit.event.Listener {
    @EventHandler
    public void onCraft(PrepareItemCraftEvent event) {
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
}
