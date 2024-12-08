package com.flask1983.grandexchange.util;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dreambot.api.Client;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.randoms.RandomEvent;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.widgets.WidgetChild;

public class Util {
  private static int CLOSED_ROOM_DOOR_ID = 7452;
  private static Tile CLOSED_ROOM_DOOR_TILE = new Tile(1798, 3605, 0);
  private static int FRUIT_STALL_ID = 28823;
  private static int DEPLETED_FRUIT_STALL_ID = 27537;
  private static Tile FRUIT_STALL_TILE = new Tile(1801, 3607, 0);

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

    GameObject bankBooth = GameObjects.closest("Bank booth", "Bank chest");
    bankBooth.interact();
    Sleep.sleepUntil(() -> Bank.isOpen(), 5000);
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

  public static boolean isDoorOpen() {
    GameObject[] objectsOnClosedDoorTile = GameObjects.getObjectsOnTile(CLOSED_ROOM_DOOR_TILE);
    for (GameObject object : objectsOnClosedDoorTile) {
      if (object.getID() == CLOSED_ROOM_DOOR_ID) {
        return false;
      }
    }

    return true;
  }

  public static boolean isFruitAvailable() {
    GameObject[] objectsOnFruitStallTile = GameObjects.getObjectsOnTile(FRUIT_STALL_TILE);
    for (GameObject object : objectsOnFruitStallTile) {
      if (object.getID() == FRUIT_STALL_ID) {
        return true;
      }
    }
    return false;
  }

  public static void openClosedDoor() {
    Logger.log("Opening door.");
    GameObject[] objectsOnClosedDoorTile = GameObjects.getObjectsOnTile(CLOSED_ROOM_DOOR_TILE);
    for (GameObject object : objectsOnClosedDoorTile) {
      if (object.getID() == CLOSED_ROOM_DOOR_ID) {
        object.interact();
        Sleep.sleepUntil(() -> isDoorOpen(), 5000);
      }
    }
  }
}
