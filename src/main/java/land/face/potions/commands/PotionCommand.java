package land.face.potions.commands;

import static com.tealcube.minecraft.bukkit.facecore.utilities.MessageUtils.sendMessage;

import com.tealcube.minecraft.bukkit.facecore.utilities.MessageUtils;
import com.tealcube.minecraft.bukkit.shade.acf.BaseCommand;
import com.tealcube.minecraft.bukkit.shade.acf.annotation.CommandAlias;
import com.tealcube.minecraft.bukkit.shade.acf.annotation.CommandCompletion;
import com.tealcube.minecraft.bukkit.shade.acf.annotation.CommandPermission;
import com.tealcube.minecraft.bukkit.shade.acf.annotation.Subcommand;
import com.tealcube.minecraft.bukkit.shade.acf.bukkit.contexts.OnlinePlayer;
import land.face.potions.PotionPlugin;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

@CommandAlias("potionseller|potions|potion")
public class PotionCommand extends BaseCommand {

  private final PotionPlugin plugin;

  public PotionCommand(PotionPlugin plugin) {
    this.plugin = plugin;
  }

  @Subcommand("reload")
  @CommandPermission("potionseller.reload")
  public void reload(CommandSender sender) {
    plugin.onDisable();
    plugin.onEnable();
    sendMessage(sender, plugin.getSettings().getString("config.language.command.reload", "&aReloaded!"));
  }

  @Subcommand("give")
  @CommandCompletion("@players @potions")
  @CommandPermission("potionseller.give")
  public void give(CommandSender sender, OnlinePlayer target, String potion) {
    ItemStack stack = new ItemStack(Material.GLASS_BOTTLE);
    stack = plugin.getPotionManager().updateItemStack(stack, plugin.getPotionManager().getPotion(potion));
    target.getPlayer().getInventory().addItem(stack);
    MessageUtils.sendMessage(sender, "&aPotion given");
  }
}
