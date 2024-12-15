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
import org.dreambot.api.randoms.RandomEvent;
import org.dreambot.api.script.listener.ExperienceListener;

public class CowKillerNode extends Node {
  private int TRAPDOOR_ID = 14880;
  private int LADDER_ID = 17385;
  private String GATE_NAME = "Gate";
  private String COW_NAME = "Cow";
  private int COWHIDE_ID = 1739;

  private Area COW_AREA = new Area(3253, 3255, 3265, 3269, 0);
  private Area TRAPDOOR_AREA = new Area(3208, 3217, 3210, 3215, 0);
  private Area LADDER_AREA = new Area(3208, 9615, 3218, 9623, 0);
  private Tile BANK_TILE = new Tile(3218, 9623, 0);
  private Tile OUTSIDE_GATE_TILE = new Tile(3250, 3265, 0);
  private Tile INSIDE_GATE_TILE = new Tile(3253, 3266, 0);

  public boolean accept(int i) {
    return true;
  }

  public int execute() {
    if (Walking.getRunEnergy() > 40 && !Walking.isRunEnabled()) {
      Walking.toggleRun();
      Sleep.sleep(Calculations.random(300, 1000));
    }

    if (Inventory.isFull() && COW_AREA.contains(Util.getCurrentTile())) {
      while (!Util.getCurrentTile().equals(INSIDE_GATE_TILE)) {
        Walking.walk(INSIDE_GATE_TILE);
        Sleep.sleep(Calculations.random(1000, 3000));
      }
      Util.openClosedGate();
      while (!TRAPDOOR_AREA.contains(Util.getCurrentTile())) {
        Walking.walk(TRAPDOOR_AREA.getRandomTile());
        Sleep.sleep(Calculations.random(1000, 3000));
      }
      GameObjects.closest(TRAPDOOR_ID).interact();
      Sleep.sleepUntil(() -> LADDER_AREA.contains(Util.getCurrentTile()), 5000);
    } else if (Inventory.isFull() && LADDER_AREA.contains(Util.getCurrentTile())) {
      Util.openBank();
      Util.depositInventory();
      Bank.close();
    } else if (!Inventory.isFull() && Util.getCurrentTile().equals(BANK_TILE)) {
      GameObjects.closest(LADDER_ID).interact();
      Sleep.sleepUntil(() -> TRAPDOOR_AREA.contains(Util.getCurrentTile()), 8000);
    } else if (!Inventory.isFull() && TRAPDOOR_AREA.contains(Util.getCurrentTile())) {
      while (!Util.getCurrentTile().equals(OUTSIDE_GATE_TILE)) {
        Walking.walk(OUTSIDE_GATE_TILE);
        Sleep.sleep(Calculations.random(1000, 3000));
      }
      Util.openClosedGate();
      while (!COW_AREA.contains(Util.getCurrentTile())) {
        Walking.walk(COW_AREA.getRandomTile());
        Sleep.sleep(Calculations.random(1000, 3000));
      }
    } else if (!Inventory.isFull()) {
      if (Players.getLocal().isInCombat()) {
        return 1;
      }
      List<NPC> cows = NPCs.all(COW_NAME);
      NPC closestCow = Util.getClosestCow(cows);
      closestCow.interact("Attack");
      Sleep.sleep(1000);
      Sleep.sleepUntil(() -> closestCow.getHealthPercent() == 0, 15000);
      Tile[] deadCowTiles = closestCow.getSurroundingArea(2).getTiles();
      Sleep.sleepUntil(() -> !closestCow.exists(), 15000);
      for (Tile tile : deadCowTiles) {
        for (GroundItem item : GroundItems.getForTile(tile)) {
          if (item.getID() == COWHIDE_ID) {
            item.interact("Take");
            int numCowhides = Inventory.count(COWHIDE_ID);
            Sleep.sleepUntil(() -> Inventory.count(COWHIDE_ID) == numCowhides + 1 || Inventory.count(COWHIDE_ID) == 28,
                4000);
            break;
          }
        }
      }
    }

    return 1;
  }
}
