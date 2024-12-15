package com.flask1983.grandexchange.nodes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.dreambot.api.utilities.AccountManager;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.Entity;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.interactive.Player;
import org.dreambot.api.wrappers.items.GroundItem;
import org.dreambot.api.wrappers.items.Item;
import org.dreambot.api.wrappers.widgets.WidgetChild;

import com.flask1983.grandexchange.util.Util;

import org.dreambot.api.Client;
import org.dreambot.api.input.Mouse;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.ViewportTools;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.container.impl.equipment.Equipment;
import org.dreambot.api.methods.container.impl.equipment.EquipmentSlot;
import org.dreambot.api.methods.grandexchange.GrandExchange;
import org.dreambot.api.methods.grandexchange.GrandExchangeItem;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.item.GroundItems;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.methods.worldhopper.WorldHopper;
import org.dreambot.api.randoms.RandomEvent;
import org.dreambot.api.script.listener.ExperienceListener;

public class GreenDragonKillerNode extends Node {
  private String GAMES_NECK_NAME = "Games necklace";
  private int SWORDFISH_ID = 373;

  private String DUELING_RING_NAME = "Ring of dueling";
  private int ACCUMULATOR_ID = 10499;
  private int BONE_BOLT_ID = 8882;
  private int DORGESHUUN_CROSSBOW_ID = 8880;
  private int HARD_LEATHER_BODY_ID = 1131;
  private int ANTI_DFIRE_SHIELD_ID = 1540;
  private int GREEN_VAMBS_ID = 1065;

  private Tile bankTile = new Tile(2443, 3083, 0);

  private int CORP_CAVE_EXIT_ID = 679;
  private Area CORP_AREA = new Area(2968, 4379, 2964, 4382, 2);

  private Tile SAFESPOT = new Tile(3345, 3670, 0);
  private Tile DRAGON_SPAWN_TILE = new Tile(3340, 3678, 0);
  private Tile DRAGON_FIGHT_TILE = new Tile(3347, 3675, 0);
  private Area DRAGON_AREA = new Area(3339, 3667, 3347, 3676, 0);
  private int GREEN_DRAG_ID = 260;
  List<String> wantedItems = Arrays.asList("Rune dagger", "Dragon bones", "Green dragonhide", "Law rune", "Nature rune",
      "Grimy ranarr weed", "Grimy irit leaf", "Grimy avantoe", "Grimy kwuarm", "Grimy cadantine", "Grimy lantadyme",
      "Grimy dwarf weed", "Grimy harralander", "Ensouled dragon head", "Uncut sapphire", "Uncut emerald",
      "Uncut ruby", "Nature talisman", "Uncut diamond", "Loop half of key", "Tooth half of key",
      "Rune javelin", "Rune spear", "Shield left half", "Dragon spear");

  public boolean accept(int i) {
    return true;
  }

  public int execute() {
    if (Inventory.isFull() || Inventory.isEmpty()) {
      resupply();
      Sleep.sleep(Calculations.random(1000, 2000));
    } else if (!Inventory.isFull() && !DRAGON_AREA.contains(Util.getCurrentTile())) {
      travelToDragons();
    } else if (!Inventory.isFull() && DRAGON_AREA.contains(Util.getCurrentTile())) {
      killDragons();
    }

    return 1;
  }

  public void resupply() {
    if (GameObjects.closest("Bank booth", "Bank chest", "Chest") == null) {
      Util.teleportCastleWars();
      Sleep.sleep(Calculations.random(2000, 5000));
    }

    Util.openBank();
    Util.depositInventory();
    Bank.depositAllEquipment();

    Bank.withdraw(SWORDFISH_ID, 2);
    Bank.withdraw(item -> item.getName().startsWith(GAMES_NECK_NAME), 1);
    Bank.withdraw(ACCUMULATOR_ID, 1);
    Bank.withdraw(BONE_BOLT_ID, 120);
    Bank.withdraw(DORGESHUUN_CROSSBOW_ID, 1);
    Bank.withdraw(HARD_LEATHER_BODY_ID, 1);
    Bank.withdraw(ANTI_DFIRE_SHIELD_ID, 1);
    Bank.withdraw(GREEN_VAMBS_ID, 1);
    Bank.withdraw(item -> item.getName().startsWith(DUELING_RING_NAME), 1);
    Bank.close();

    Inventory.interact(item -> item.getName().startsWith(GAMES_NECK_NAME));
    Inventory.interact(ACCUMULATOR_ID);
    Inventory.interact(BONE_BOLT_ID);
    Inventory.interact(DORGESHUUN_CROSSBOW_ID);
    Inventory.interact(HARD_LEATHER_BODY_ID);
    Inventory.interact(ANTI_DFIRE_SHIELD_ID);
    Inventory.interact(GREEN_VAMBS_ID);
    Inventory.interact(item -> item.getName().startsWith(DUELING_RING_NAME));
  }

  public void travelToDragons() {
    if (Players.getLocal().getHealthPercent() < 75) {
      Inventory.interact(SWORDFISH_ID);
      Sleep.sleep(Calculations.random(400, 1000));
    } else if (Util.getCurrentTile().equals(bankTile)) {
      Util.teleportCorp();
      Sleep.sleepUntil(() -> CORP_AREA.contains(Util.getCurrentTile()), 4000);
    } else if (CORP_AREA.contains(Util.getCurrentTile())) {
      GameObjects.closest(CORP_CAVE_EXIT_ID).interact();
      Sleep.sleepUntil(() -> Widgets.get(219, 1, 1) != null, 5000);
      WidgetChild exitCorpCave = Widgets.get(219, 1, 1);
      exitCorpCave.interact();
    } else {
      Walking.walk(SAFESPOT);
      Sleep.sleep(Calculations.random(1000, 2000));
    }
  }

  public void killDragons() {
    if (Players.getLocal().getHealthPercent() < 75) {
      if (Inventory.contains(SWORDFISH_ID)) {
        Inventory.interact(SWORDFISH_ID);
        Sleep.sleep(Calculations.random(400, 700));
      } else {
        Util.teleportCastleWars();
        Sleep.sleep(Calculations.random(1000, 2000));
      }
    } else if (Util.isPlayerAttackingMe()) {
      Util.teleportCastleWars();
      Sleep.sleep(Calculations.random(1000, 2000));
    } else if (Players.getLocal().isInCombat() && !Util.getCurrentTile().equals(SAFESPOT)) {
      Walking.walk(SAFESPOT);
      Sleep.sleepUntil(() -> Util.getCurrentTile().equals(SAFESPOT), 4000);
    } else if (!Players.getLocal().isInCombat() && !Util.getCurrentTile().equals(SAFESPOT)) {
      Walking.walk(SAFESPOT);
      Sleep.sleepUntil(() -> Util.getCurrentTile().equals(SAFESPOT), 4000);
    } else if (!Players.getLocal().isInCombat() && !Inventory.isFull()) {
      NPC dragon = NPCs.closest(GREEN_DRAG_ID);
      if (dragon == null || !Util.isDragonOnFightTiles()) {
        return;
      }
      dragon.interact();
      Sleep.sleepUntil(() -> Players.getLocal().isInCombat(), 5000);
      while (!Util.isPlayerAttackingMe() && Players.getLocal().isInCombat() && dragon.getHealthPercent() > 0
          && Util.getCurrentTile().equals(SAFESPOT)) {
        Sleep.sleep(Calculations.random(100, 700));
      }
      if (Util.isPlayerAttackingMe()) {
        Util.teleportCastleWars();
        Sleep.sleep(Calculations.random(1000, 1500));
      } else if (!Util.getCurrentTile().equals(SAFESPOT)) {
        Walking.walk(SAFESPOT);
        Sleep.sleepUntil(() -> Util.getCurrentTile().equals(SAFESPOT), 4000);
      } else if (dragon.getHealthPercent() == 0) {
        Tile[] deadDragonTiles = dragon.getSurroundingArea(2).getTiles();
        Sleep.sleepUntil(() -> !dragon.exists(), 5000);
        for (Tile tile : deadDragonTiles) {
          for (GroundItem item : GroundItems.getForTile(tile)) {
            if (!Util.getCurrentTile().equals(item.getTile())) {
              Walking.walk(item.getTile());
              Sleep.sleepUntil(() -> Util.getCurrentTile().equals(item.getTile()), 3000);
            }
            if (!Inventory.isFull() && wantedItems.contains(item.getName())) {
              item.interact("Take");
              Sleep.sleep(Calculations.random(500, 700));
            }
          }
        }
      }
    }
  }
}
