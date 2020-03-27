package land.face.potions.data;

import java.util.List;
import land.face.strife.StrifePlugin;
import land.face.strife.data.effects.Effect;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.potion.PotionEffect;

public class Potion {

  private String id;
  private int potionId;

  private int maxDoses;
  private int refillSpeed;
  private int levelRequirement;

  private float instantHealth;
  private float healthOverTime;
  private int healthTicks;

  private float instantEnergy;
  private float energyOverTime;
  private int energyTicks;

  private List<PotionEffect> potionEffects;
  private List<Effect> strifeEffects;

  private String displayName;
  private List<String> flavorText;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public int getPotionId() {
    return potionId;
  }

  public void setPotionId(int potionId) {
    this.potionId = potionId;
  }

  public int getMaxDoses() {
    return maxDoses;
  }

  public void setMaxDoses(int maxDoses) {
    this.maxDoses = maxDoses;
  }

  public int getRefillSpeed() {
    return refillSpeed;
  }

  public void setRefillSpeed(int refillSpeed) {
    this.refillSpeed = refillSpeed;
  }

  public int getLevelRequirement() {
    return levelRequirement;
  }

  public void setLevelRequirement(int levelRequirement) {
    this.levelRequirement = levelRequirement;
  }

  public float getInstantHealth() {
    return instantHealth;
  }

  public void setInstantHealth(float instantHealth) {
    this.instantHealth = instantHealth;
  }

  public float getHealthOverTime() {
    return healthOverTime;
  }

  public void setHealthOverTime(float healthOverTime) {
    this.healthOverTime = healthOverTime;
  }

  public int getHealthTicks() {
    return healthTicks;
  }

  public void setHealthTicks(int healthTicks) {
    this.healthTicks = healthTicks;
  }

  public float getInstantEnergy() {
    return instantEnergy;
  }

  public void setInstantEnergy(float instantEnergy) {
    this.instantEnergy = instantEnergy;
  }

  public float getEnergyOverTime() {
    return energyOverTime;
  }

  public void setEnergyOverTime(float energyOverTime) {
    this.energyOverTime = energyOverTime;
  }

  public int getEnergyTicks() {
    return energyTicks;
  }

  public void setEnergyTicks(int energyTicks) {
    this.energyTicks = energyTicks;
  }

  public List<PotionEffect> getPotionEffects() {
    return potionEffects;
  }

  public void setPotionEffects(List<PotionEffect> potionEffects) {
    this.potionEffects = potionEffects;
  }

  public List<Effect> getStrifeEffects() {
    return strifeEffects;
  }

  public void setStrifeEffects(List<Effect> strifeEffects) {
    this.strifeEffects = strifeEffects;
  }

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public List<String> getFlavorText() {
    return flavorText;
  }

  public void setFlavorText(List<String> flavorText) {
    this.flavorText = flavorText;
  }

  public static Potion loadFromConfigSection(ConfigurationSection section, String key) {

    Potion potion = new Potion();

    potion.setId(key);
    potion.setPotionId(section.getInt("potion-id"));

    potion.setDisplayName(section.getString("display-name", "Potion"));
    potion.setFlavorText(section.getStringList("flavor-text"));

    potion.setMaxDoses(section.getInt("max-doses", 3));
    potion.setRefillSpeed(Math.max(1, Math.min(999, section.getInt("refill-speed", 999))));
    potion.setLevelRequirement(section.getInt("level-requirement", 1));

    potion.setInstantHealth((float) section.getDouble("instant-health", 0));
    potion.setHealthOverTime((float) section.getDouble("health-recovery", 0));
    potion.setHealthTicks(section.getInt("health-recovery-ticks", 20));
    potion.setInstantEnergy((float) section.getDouble("instant-energy", 0));
    potion.setEnergyOverTime((float) section.getDouble("energy-recovery", 0));
    potion.setEnergyTicks(section.getInt("energy-recovery-ticks", 20));

    List<String> effects = section.getStringList("strife-effects");
    potion.setStrifeEffects(StrifePlugin.getInstance().getEffectManager().getEffects(effects));

    return potion;
  }

}
