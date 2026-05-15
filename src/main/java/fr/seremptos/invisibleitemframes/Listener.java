package fr.seremptos.invisibleitemframes;

import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.iface.ReadableNBT;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;


public class Listener implements org.bukkit.event.Listener {

    private final Plugin plugin;

    public Listener(Plugin plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void onCraft(PrepareItemCraftEvent event) {
        if(event.getInventory().getMatrix().length != 9) return; // On check si on est bien dans un établi.
        ItemStack item = event.getInventory().getMatrix()[4];
        if(item == null) return;

        if(Main.frames.contains(item.getType())){
            NBT.getComponents(item, nbt -> {
                ReadableNBT entity_data = nbt.getCompound(Main.ENTITY_DATA);
                if(entity_data != null && entity_data.hasTag(Main.INVISIBLE)) {
                    event.getInventory().setResult(null);
                }
            });
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onHit(HangingBreakEvent event) {
        if (!event.isCancelled()
                && event.getEntity() instanceof ItemFrame itemFrame
                && plugin.getConfig().getBoolean("override-item-frame-drop", true)
        ) {
            NBT.get(itemFrame, nbt -> {
                    if (nbt.getBoolean(Main.INVISIBLE) == true) {
                        event.setCancelled(true);
                        Material m = Material.matchMaterial(itemFrame.getType().toString());
                        itemFrame.remove();
                        if (m != null) {
                            Location loc = event.getEntity().getLocation();
                            loc.getWorld().dropItem(loc, Main.getInvisibleFrame(
                                    plugin,
                                    new ItemStack(m))
                            );
                    }
                }
            });
        }
    }
}
