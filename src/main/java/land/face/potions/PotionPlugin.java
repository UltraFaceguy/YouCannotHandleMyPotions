package land.face.potions;

import com.tealcube.minecraft.bukkit.shade.acf.PaperCommandManager;
import io.pixeloutlaw.minecraft.spigot.config.MasterConfiguration;
import io.pixeloutlaw.minecraft.spigot.config.VersionedConfiguration;
import io.pixeloutlaw.minecraft.spigot.config.VersionedSmartYamlConfiguration;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import land.face.potions.commands.PotionCommand;
import land.face.potions.listeners.ItemListListener;
import land.face.potions.listeners.PotionDrankListener;
import land.face.potions.managers.PotionManager;
import land.face.potions.tasks.ChargeTimer;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public class PotionPlugin extends JavaPlugin {

  private static PotionPlugin instance;
  public static final DecimalFormat INT_FORMAT = new DecimalFormat("#");
  public static final DecimalFormat ONE_DECIMAL = new DecimalFormat("#.#");

  private PotionManager potionManager;
  private MasterConfiguration settings;
  private ChargeTimer chargeTimer;

  public static PotionPlugin getInstance() {
    return instance;
  }

  public void onEnable() {
    instance = this;
    potionManager = new PotionManager();

    List<VersionedSmartYamlConfiguration> configurations = new ArrayList<>();
    VersionedSmartYamlConfiguration configYAML;
    configurations.add(configYAML = defaultSettingsLoad("config.yml"));
    VersionedSmartYamlConfiguration potionYAML;
    configurations.add(potionYAML = defaultSettingsLoad("potions.yml"));

    for (VersionedSmartYamlConfiguration config : configurations) {
      if (config.update()) {
        getLogger().info("Updating " + config.getFileName());
      }
    }

    settings = MasterConfiguration.loadFromFiles(configYAML);

    Bukkit.getPluginManager().registerEvents(new PotionDrankListener(this), this);
    Bukkit.getPluginManager().registerEvents(new ItemListListener(this), this);

    potionManager.loadAllPotions(potionYAML);

    PaperCommandManager commandManager = new PaperCommandManager(this);
    commandManager.registerCommand(new PotionCommand(this));
    commandManager.getCommandCompletions()
        .registerCompletion("potions", c -> potionManager.getPotionIds());


    chargeTimer = new ChargeTimer(this);

    chargeTimer.runTaskTimer(this,
        5L, // Start timer after 5 ticks
        200L // Run it every 10s
    );
    Bukkit.getServer().getLogger().info("PotionsPlugin Enabled!");
  }

  public void onDisable() {
    HandlerList.unregisterAll(this);
    chargeTimer.cancel();
    Bukkit.getServer().getScheduler().cancelTasks(this);
    Bukkit.getServer().getLogger().info("PotionsPlugin Disabled!");
  }

  public MasterConfiguration getSettings() {
    return settings;
  }

  public PotionManager getPotionManager() {
    return potionManager;
  }

  private VersionedSmartYamlConfiguration defaultSettingsLoad(String name) {
    return new VersionedSmartYamlConfiguration(new File(getDataFolder(), name),
        getResource(name), VersionedConfiguration.VersionUpdateType.BACKUP_AND_UPDATE);
  }
}