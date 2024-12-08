package com.flask1983.grandexchange.util;

import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dreambot.api.Client;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.randoms.RandomEvent;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.widgets.WidgetChild;

public class Util {
  private static Tile OPEN_GATE_TILE = new Tile(3251, 3266, 0);
  private static Tile CLOSED_GATE_TILE = new Tile(3253, 3267, 0);
  private static String GATE_NAME = "Gate";
  private static Area COW_AREA = new Area(3253, 3255, 3265, 3269, 0);

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

    GameObject bankBooth = GameObjects.closest("Chest");
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

  public static boolean isGateOpen() {
    GameObject[] objectsOnClosedGateTile = GameObjects.getObjectsOnTile(CLOSED_GATE_TILE);
    for (GameObject object : objectsOnClosedGateTile) {
      if (object.getName().equals(GATE_NAME)) {
        return false;
      }
    }

    return true;
  }

  public static void openClosedGate() {
    if (!isGateOpen()) {
      GameObjects.closest(GATE_NAME).interact();
      Sleep.sleepUntil(() -> isGateOpen(), 8000);
    }
  }

  public static NPC getClosestCow(List<NPC> cows) {
    NPC closestCow = null;
    double distance = 1000;

    for (NPC cow : cows) {
      if (cow != null && !cow.isInCombat() && COW_AREA.contains(cow.getTile())) {
        if (Players.getLocal().distance(cow) < distance) {
          distance = Players.getLocal().distance(cow);
          closestCow = cow;
        }
      }
    }

    return closestCow;
  }
}
