/**
 * The MIT License Copyright (c) 2015 Teal Cube Games
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package land.face.potions.tasks;

import land.face.potions.PotionPlugin;
import land.face.potions.data.Potion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class ChargeTimer extends BukkitRunnable {

  private PotionPlugin plugin;

  public ChargeTimer(PotionPlugin plugin) {
    this.plugin = plugin;
  }

  @Override
  public void run() {
    for (Player p : Bukkit.getOnlinePlayers()) {
      for (int i = 0; i <= 9; i++) {
        ItemStack stack = p.getInventory().getItem(i);
        Potion potion = plugin.getPotionManager().getPotionFromStack(stack);
        if (potion == null) {
          continue;
        }
        plugin.getPotionManager().tickPotion(stack, potion);
      }
    }
  }
}
