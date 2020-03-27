package land.face.potions.commands;

import static com.tealcube.minecraft.bukkit.facecore.utilities.MessageUtils.sendMessage;

import land.face.potions.PotionPlugin;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import se.ranzdo.bukkit.methodcommand.Arg;
import se.ranzdo.bukkit.methodcommand.Command;

public class BaseCommand {

  private PotionPlugin plugin;

  public BaseCommand(PotionPlugin plugin) {
    this.plugin = plugin;
  }

  @Command(identifier = "potionseller reload", permissions = "potionseller.reload", onlyPlayers = false)
  public void reloadCommand(CommandSender sender) {
    plugin.onDisable();
    plugin.onEnable();
    sendMessage(sender, plugin.getSettings().getString("config.language.command.reload", "&aReloaded!"));
  }

  @Command(identifier = "potionseller give", permissions = "potionseller.give", onlyPlayers = false)
  public void menuCommand(CommandSender sender, @Arg(name = "target") Player target,  @Arg(name = "potion") String potion) {

    ItemStack stack = new ItemStack(Material.GLASS_BOTTLE);
    stack = plugin.getPotionManager().updateItemStack(stack, plugin.getPotionManager().getPotion(potion));
    target.getInventory().addItem(stack);
  }
}
