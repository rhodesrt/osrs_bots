package com.flask1983.grandexchange.util;

import java.awt.Dialog;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dreambot.api.Client;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.container.impl.equipment.Equipment;
import org.dreambot.api.methods.container.impl.equipment.EquipmentSlot;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.randoms.RandomEvent;
import org.dreambot.api.script.event.impl.EquipmentItemEvent;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.interactive.Player;
import org.dreambot.api.wrappers.items.Item;
import org.dreambot.api.wrappers.widgets.WidgetChild;

public class Util {
  private static int GREEN_DRAG_ID = 260;
  private static Tile DRAGON_SPAWN_TILE = new Tile(3340, 3678, 0);
  private static Tile DRAGON_FIGHT_TILE = new Tile(3347, 3675, 0);
  private static Tile DRAGON_FIGHT_TILE_1 = new Tile(3340, 3672, 0);
  private static Area DRAGON_AREA = new Area(3339, 3667, 3347, 3676, 0);

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
    Equipment.getItemInSlot(EquipmentSlot.RING).interact("Castle Wars");
    Inventory.open();
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
