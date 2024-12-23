package com.flask1983.grandexchange.util;

import java.util.Random;

import org.dreambot.api.Client;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.container.impl.equipment.Equipment;
import org.dreambot.api.methods.container.impl.equipment.EquipmentSlot;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.magic.Magic;
import org.dreambot.api.methods.magic.Normal;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.randoms.RandomEvent;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.interactive.Player;
import org.dreambot.api.wrappers.widgets.WidgetChild;

public class Util {
  private static int GREEN_DRAG_ID = 260;
  private static Tile DRAGON_SPAWN_TILE = new Tile(3340, 3678, 0);
  private static Tile DRAGON_FIGHT_TILE = new Tile(3347, 3675, 0);
  private static Tile DRAGON_FIGHT_TILE_1 = new Tile(3340, 3672, 0);
  private static Area DRAGON_AREA = new Area(3339, 3667, 3347, 3676, 0);
  private static String DUELING_RING_NAME = "Ring of dueling";
  private static Area LUMBRIDGE_TELE_AREA = new Area(3217, 3211, 3226, 3225, 0);
  private static Tile TRAPDOOR_TILE = new Tile(3210, 3216, 0);
  private static int TRAPDOOR_ID = 14880;
  private static int BANK_CHEST_ID = 12308;

  public static Tile getCurrentTile() {
    return Players.getLocal().getTile();
  }

  public static int generateRandom(int i) {
    Random random = new Random();
    return random.nextInt(i + 1);
  }

  public static void openBank() {
    if (Bank.isOpen()) {
      return;
    }

    GameObject bankBooth = GameObjects.closest("Bank booth", "Chest", "Bank chest");
    bankBooth.interact();
    Sleep.sleepUntil(() -> Bank.isOpen(), 10000);
  }

  public static void depositInventory() {
    WidgetChild depositInventory = Widgets.get(12, 44);
    if (depositInventory != null) {
      depositInventory.interact();
    }
  }

  public static void logout() {
    if (Client.isLoggedIn()) {
      Client.getInstance().getRandomManager().disableSolver(RandomEvent.LOGIN);
      Tabs.logout();
    }
  }

  public static void teleportCorp() {
    Equipment.getItemInSlot(EquipmentSlot.AMULET).interact("Corporeal Beast");
    Inventory.open();
  }

  public static void teleportCastleWars() {
    if (Equipment.getItemInSlot(EquipmentSlot.RING).getName().startsWith(DUELING_RING_NAME)) {
      Equipment.getItemInSlot(EquipmentSlot.RING).interact("Castle Wars");
      Inventory.open();
    } else {
      if (Magic.canCast(Normal.HOME_TELEPORT)) {
        Magic.castSpell(Normal.HOME_TELEPORT);
        Sleep.sleepUntil(() -> LUMBRIDGE_TELE_AREA.contains(Util.getCurrentTile()), 15000);
      }

      while (!Util.getCurrentTile().equals(TRAPDOOR_TILE)) {
        Walking.walk(TRAPDOOR_TILE);
        Sleep.sleep(Calculations.random(2000, 4000));
      }
      GameObject trapdoor = GameObjects.closest(TRAPDOOR_ID);
      trapdoor.interact();
      Sleep.sleep(Calculations.random(2000, 4000));

      openBank();
    }
  }

  public static boolean isPlayerAttackingMe() {
    return (Players.getLocal().isInCombat() && Players.getLocal().getCharacterInteractingWithMe() != null
        && Players.getLocal().getCharacterInteractingWithMe() instanceof Player);
  }

  public static boolean isDragonOnFightTiles() {
    NPC dragon = NPCs.closest(GREEN_DRAG_ID);
    if (dragon != null) {
      return (dragon.getTile().distance(DRAGON_AREA.getRandomTile()) <= 3
          || dragon.getTile().distance(DRAGON_FIGHT_TILE) <= 3
          || dragon.getTile().distance(DRAGON_FIGHT_TILE_1) <= 3 || dragon.getTile().distance(DRAGON_SPAWN_TILE) <= 3);
    }
    return false;
  }
}
