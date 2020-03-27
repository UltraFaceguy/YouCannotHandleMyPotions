package land.face.potions.managers;

import com.tealcube.minecraft.bukkit.TextUtils;
import io.pixeloutlaw.minecraft.spigot.config.VersionedSmartYamlConfiguration;
import io.pixeloutlaw.minecraft.spigot.hilt.ItemStackExtensionsKt;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import land.face.potions.PotionPlugin;
import land.face.potions.data.Potion;
import land.face.strife.StrifePlugin;
import land.face.strife.util.ItemUtil;
import land.face.strife.util.PlayerDataUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PotionManager {

  private Map<Integer, Potion> potionMap = new HashMap<>();
  private Map<String, Integer> potionIdMap = new HashMap<>();

  private static ChatColor[] colors = ChatColor.values();

  private Random random = new Random();

  public Potion getPotion(String potionId) {
    int intId = potionIdMap.getOrDefault(potionId, -1);
    if (intId == -1) {
      return null;
    }
    return getPotion(intId);
  }

  public Potion getPotion(int id) {
    return potionMap.get(id);
  }

  public ItemStack updateItemStack(ItemStack stack, Potion potion) {

    int data = potion.getPotionId() * 100000;
    data += potion.getMaxDoses() * 10000;

    if (ItemStackExtensionsKt.hasCustomModelData(stack)) {
      data += getDoses(stack) * 1000;
      data += ItemUtil.getCustomData(stack) % 1000;
    } else {
      data += potion.getMaxDoses() * 1000;
    }
    ItemStackExtensionsKt.setCustomModelData(stack, data);

    String name = TextUtils.color(potion.getDisplayName());
    name += " (" + getDoses(stack) + "/" + potion.getMaxDoses() + ")";
    ItemStackExtensionsKt.setDisplayName(stack, name);

    List<String> lore = new ArrayList<>();
    lore.add("&fLevel Requirement: " + potion.getLevelRequirement() + getAntiStack());
    if (potion.getInstantHealth() > 0.1) {
      lore.add("&aInstant Life: &f" + PotionPlugin.INT_FORMAT.format(potion.getInstantHealth()));
    }
    if (potion.getHealthOverTime() > 0.1) {
      lore.add("&aLife Over Time: &f"
          + PotionPlugin.INT_FORMAT.format(potion.getHealthOverTime()));
      lore.add("&aDuration: &f"
          + PotionPlugin.ONE_DECIMAL.format((double) potion.getHealthTicks() / 20) + "s");
    }
    if (potion.getInstantEnergy() > 0.1) {
      lore.add("&eInstant Energy: &f" + PotionPlugin.INT_FORMAT.format(potion.getInstantEnergy()));
    }
    if (potion.getEnergyOverTime() > 0.1) {
      lore.add("&eEnergy Over Time: &f"
          + PotionPlugin.INT_FORMAT.format(potion.getEnergyOverTime()));
      lore.add("&eDuration: &f"
          + PotionPlugin.ONE_DECIMAL.format((double) potion.getEnergyTicks() / 20) + "s");
    }
    lore.addAll(potion.getFlavorText());
    ItemStackExtensionsKt.setLore(stack, TextUtils.color(lore));
    System.out.println("data: " + data);
    return stack;
  }

  public int getDoses(ItemStack stack) {
    int data = ItemUtil.getCustomData(stack);
    data = data % 10000;
    return data / 1000;
  }

  public void addDoses(ItemStack stack, int doses) {
    int value = ItemUtil.getCustomData(stack);
    value += doses * 1000;
    ItemStackExtensionsKt.setCustomModelData(stack, value);
  }

  public void consumeDose(Player player, Potion potion, ItemStack stack) {
    addDoses(stack, -1);
    updateItemStack(stack, potion);
    if (potion.getInstantHealth() > 0.1) {
      PlayerDataUtil.restoreHealth(player, potion.getInstantHealth());
    }
    if (potion.getHealthOverTime() > 0.1) {
      PlayerDataUtil
          .restoreHealthOverTime(player, potion.getHealthOverTime(), potion.getHealthTicks());
    }
    if (potion.getInstantEnergy() > 0.1) {
      PlayerDataUtil.restoreEnergy(player, potion.getInstantEnergy());
    }
    if (potion.getEnergyOverTime() > 0.1) {
      PlayerDataUtil
          .restoreEnergyOverTime(player, potion.getEnergyOverTime(), potion.getEnergyTicks());
    }
    if (!potion.getStrifeEffects().isEmpty()) {
      StrifePlugin.getInstance().getEffectManager()
          .execute(StrifePlugin.getInstance().getStrifeMobManager().getStatMob(player), player,
              potion.getStrifeEffects());
    }
    player.setCooldown(Material.GLASS_BOTTLE, 200);
  }

  public Potion getPotionFromStack(ItemStack stack) {
    if (stack == null || stack.getType() != Material.GLASS_BOTTLE) {
      return null;
    }
    return getPotion((int) Math.floor((double) ItemUtil.getCustomData(stack) / 100000));
  }

  public void tickPotion(ItemStack stack, Potion potion) {
    if (potion.getMaxDoses() == getDoses(stack) || stack.getAmount() > 1) {
      return;
    }
    int status = ItemUtil.getCustomData(stack);
    if (status % 1000 == potion.getRefillSpeed()) {
      ItemStackExtensionsKt.setCustomModelData(stack, status - status % 1000);
      addDoses(stack, 1);
      updateItemStack(stack, potion);
      return;
    }
    status += 1;
    ItemStackExtensionsKt.setCustomModelData(stack, status);
  }

  public void loadAllPotions(VersionedSmartYamlConfiguration potionYAML) {
    for (String key : potionYAML.getKeys(false)) {
      if (!potionYAML.isConfigurationSection(key)) {
        continue;
      }
      ConfigurationSection cs = potionYAML.getConfigurationSection(key);
      Potion potion = Potion.loadFromConfigSection(cs, key);
      potionMap.put(potion.getPotionId(), potion);
      potionIdMap.put(potion.getId(), potion.getPotionId());
    }
  }

  private String getAntiStack() {
    int number = 20;
    StringBuilder string = new StringBuilder();
    while (number > 0) {
      string.append(colors[random.nextInt(colors.length)]);
      number--;
    }
    return string.toString();
  }
}
