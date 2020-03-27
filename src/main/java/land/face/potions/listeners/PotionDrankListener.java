package land.face.potions.listeners;

import com.tealcube.minecraft.bukkit.TextUtils;
import com.tealcube.minecraft.bukkit.facecore.utilities.MessageUtils;
import land.face.potions.PotionPlugin;
import land.face.potions.data.Potion;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class PotionDrankListener implements Listener {

  private PotionPlugin plugin;

  public PotionDrankListener(PotionPlugin plugin) {
    this.plugin = plugin;
  }

  @EventHandler
  public void onPlayerClick(PlayerInteractEvent event) {
    if (event.getPlayer().getCooldown(Material.GLASS_BOTTLE) > 0) {
      return;
    }
    if (event.getAction() == Action.RIGHT_CLICK_AIR
        || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
      ItemStack stack;
      if (event.getHand() == EquipmentSlot.HAND) {
        stack = event.getPlayer().getEquipment().getItemInMainHand();
      } else if (event.getHand() == EquipmentSlot.OFF_HAND) {
        stack = event.getPlayer().getEquipment().getItemInOffHand();
      } else {
        return;
      }
      Potion potion = plugin.getPotionManager().getPotionFromStack(stack);
      if (potion != null) {
        event.setCancelled(true);
      } else {
        return;
      }
      if (stack.getAmount() > 1) {
        MessageUtils
            .sendMessage(event.getPlayer(), TextUtils.color("&eYou cannot drink stacked potions"));
        return;
      }
      if (event.getPlayer().getLevel() < potion.getLevelRequirement()) {
        MessageUtils.sendMessage(event.getPlayer(),
            TextUtils.color("&e&o* You try to drink the potion, but it overpowers you *"));
        return;
      }
      if (plugin.getPotionManager().getDoses(stack) < 1) {
        return;
      }
      event.getPlayer().getWorld()
          .playSound(event.getPlayer().getLocation(), Sound.ITEM_HONEY_BOTTLE_DRINK, 1, 1);
      plugin.getPotionManager().consumeDose(event.getPlayer(), potion, stack);
      event.setCancelled(true);
    }
  }
}

