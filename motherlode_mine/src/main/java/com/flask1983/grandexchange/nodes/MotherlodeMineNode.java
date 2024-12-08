package com.flask1983.grandexchange.nodes;

import java.util.ArrayList;
import java.util.Arrays;

import org.dreambot.api.utilities.AccountManager;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.Entity;
import org.dreambot.api.wrappers.interactive.GameObject;
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
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.randoms.RandomEvent;
import org.dreambot.api.script.listener.ExperienceListener;

public class MotherlodeMineNode extends Node {
  Area miningArea = new Area(3731, 5687, 3744, 5691, 0);
  Area southRockfallArea = new Area(3733, 5679, 3736, 5678, 0);
  Area northRockfallArea = new Area(3729, 5684, 3731, 5686, 0);
  Area hopperArea = new Area(3746, 5676, 3750, 5673, 0);
  Area bankArea = new Area(3757, 5668, 3760, 5665, 0);
  Area sackArea = new Area(3750, 5660, 3747, 5658, 0);
  Tile SOUTH_ROCKFALL = new Tile(3733, 5680, 0);
  Tile NORTH_ROCKFALL = new Tile(3731, 5683, 0);
  int DEPLETED_VEIN = 26667;
  int SACK = 26688;

  public boolean accept(int i) {
    return true;
  }

  public int execute() {
    if (Players.getLocal().isAnimating()) {
      return 1;
    }

    // Get to mining area
    if (!miningArea.contains(Util.getCurrentTile()) && Util.remainingDirtNeeded() > 0) {
      while (!southRockfallArea.contains(Util.getCurrentTile())) {
        Walking.walk(southRockfallArea.getRandomTile());
        Sleep.sleep(1000);
        Sleep.sleepUntil(() -> !Players.getLocal().isMoving(), 5000);
      }

      if (GameObjects.getTopObjectOnTile(SOUTH_ROCKFALL).getName().equals("Rockfall")) {
        GameObjects.getTopObjectOnTile(SOUTH_ROCKFALL).interact();
        Sleep.sleep(Calculations.random(3000, 4000));
        Sleep.sleepUntil(() -> !Players.getLocal().isAnimating(), 5000);
      }
      if (GameObjects.getTopObjectOnTile(NORTH_ROCKFALL).getName().equals("Rockfall")) {
        GameObjects.getTopObjectOnTile(NORTH_ROCKFALL).interact();
        Sleep.sleep(Calculations.random(3000, 4000));
        Sleep.sleepUntil(() -> !Players.getLocal().isAnimating(), 5000);
      }
      while (!miningArea.contains(Util.getCurrentTile())) {
        Walking.walk(miningArea.getRandomTile());
        Sleep.sleep(Calculations.random(2000, 3000));
      }
    }

    // Mine pay dirt
    if (!Inventory.isFull() && miningArea.contains(Util.getCurrentTile()) && Util.remainingDirtNeeded() > 0) {
      GameObject vein = GameObjects.closest("Ore vein");
      Tile veinTile = vein.getTile();
      if (vein.interact("Mine")) {
        Sleep.sleepUntil(
            () -> !GameObjects.getTopObjectOnTile(veinTile).getName().equals("Ore vein") || Inventory.isFull()
                || Util.remainingDirtNeeded() <= 0,
            60000);
        Sleep.sleep(Calculations.random(500, 3000));
      }
    }

    // Deposit pay dirt in hopper
    if ((Inventory.isFull() || Util.remainingDirtNeeded() <= 0) && !hopperArea.contains(Util.getCurrentTile())) {
      while (!northRockfallArea.contains(Util.getCurrentTile())) {
        Walking.walk(northRockfallArea.getRandomTile());
        Sleep.sleep(1000);
        Sleep.sleepUntil(() -> !Players.getLocal().isMoving(), 5000);
      }

      if (GameObjects.getTopObjectOnTile(NORTH_ROCKFALL) != null) {
        GameObjects.getTopObjectOnTile(NORTH_ROCKFALL).interact();
        Sleep.sleep(Calculations.random(3000, 4000));
        Sleep.sleepUntil(() -> !Players.getLocal().isAnimating(), 5000);
      }
      if (GameObjects.getTopObjectOnTile(SOUTH_ROCKFALL) != null) {
        GameObjects.getTopObjectOnTile(SOUTH_ROCKFALL).interact();
        Sleep.sleep(Calculations.random(3000, 4000));
        Sleep.sleepUntil(() -> !Players.getLocal().isAnimating(), 5000);
      }
      while (!hopperArea.contains(Util.getCurrentTile())) {
        Walking.walk(hopperArea.getRandomTile());
        Sleep.sleep(1000);
        Sleep.sleepUntil(() -> !Players.getLocal().isMoving(), 5000);
      }

      if (Util.remainingDirtNeeded() <= 0) {
        GameObjects.closest("Hopper").interact();
        Sleep.sleep(Calculations.random(3000, 4000));
        while (Util.remainingDirtNeeded() <= 100) {
          while (!sackArea.contains(Util.getCurrentTile())) {
            Walking.walk(sackArea.getRandomTile());
            Sleep.sleep(1000);
            Sleep.sleepUntil(() -> !Players.getLocal().isMoving(), 5000);
          }
          GameObjects.closest(SACK).interact();
          Sleep.sleep(Calculations.random(1500, 2000));

          GameObjects.closest("Bank chest").interact();
          Sleep.sleepUntil(() -> Bank.isOpen(), 10000);
          WidgetChild depositInventory = Widgets.get(12, 44);
          if (depositInventory != null) {
            depositInventory.interact();
          }
          Bank.close();
        }
      } else {
        GameObjects.closest("Hopper").interact();
        Sleep.sleep(Calculations.random(2000, 3000));
      }
    }

    return 1;
  }
}
