package land.face.potions.listeners;

import land.face.market.data.PlayerMarketState.Category;
import land.face.market.data.PlayerMarketState.FilterFlagA;
import land.face.market.events.ListItemEvent;
import land.face.potions.PotionPlugin;
import land.face.potions.data.Potion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ItemListListener implements Listener {

  private PotionPlugin plugin;

  public ItemListListener(PotionPlugin plugin) {
    this.plugin = plugin;
  }

  @EventHandler
  public void onPlayerClick(ListItemEvent event) {
    Potion potion = plugin.getPotionManager().getPotionFromStack(event.getListing().getItemStack());
    if (potion != null) {
      event.getListing().setCategory(Category.CATEGORY_3);
      event.getListing().setFlagA(FilterFlagA.FLAG_2);
    }
  }
}

