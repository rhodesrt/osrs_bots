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

public class FruitStallNode extends Node {
  private int CLOSED_ROOM_DOOR_ID = 7452;
  private Tile CLOSED_ROOM_DOOR_TILE = new Tile(1798, 3605, 0);
  private int OPEN_ROOM_DOOR_ID = 7453;
  private Tile OPEN_ROOM_DOOR_TILE = new Tile(1798, 3606, 0);
  private Tile OUTSIDE_ROOM_TILE = new Tile(1798, 3605, 0);
  private Tile INSIDE_ROOM_TILE = new Tile(1798, 3606, 0);

  private int FRUIT_STALL_ID = 28823;
  private int DEPLETED_FRUIT_STALL_ID = 27537;
  private Tile FRUIT_STALL_TILE = new Tile(1801, 3607, 0);
  private Area FRUIT_STALL_AREA = new Area(1800, 3607, 1800, 3608, 0);

  private int STRANGE_FRUIT_ID = 464;
  private int GOLOVANOVA_ID = 19653;

  private Tile BANK_TILE = new Tile(1809, 3566, 0);

  public boolean accept(int i) {
    return true;
  }

  public int execute() {
    if (!FRUIT_STALL_AREA.contains(Util.getCurrentTile()) && Inventory.isEmpty()) {
      travelToFruitStall();
    }

    if ((Inventory.isFull() && Inventory.onlyContains(STRANGE_FRUIT_ID, GOLOVANOVA_ID))) {
      bankFruit();
    }

    if ((Inventory.isFull() && !Inventory.onlyContains(STRANGE_FRUIT_ID, GOLOVANOVA_ID))) {
      Logger.log("Dropping unwanted fruit.");
      Inventory.dropAllExcept(STRANGE_FRUIT_ID, GOLOVANOVA_ID);
    }

    if (!Inventory.isFull() && FRUIT_STALL_AREA.contains(Util.getCurrentTile())) {
      stealFruit();
    }

    return 1;
  }

  public void travelToFruitStall() {
    Logger.log("Traveling to fruit stall.");

    while (!Util.getCurrentTile().equals(CLOSED_ROOM_DOOR_TILE)) {
      Walking.walk(OUTSIDE_ROOM_TILE);
      Sleep.sleep(Calculations.random(2000, 3000));
    }

    if (!Util.isDoorOpen()) {
      Util.openClosedDoor();
    } else {
      while (!FRUIT_STALL_AREA.contains(Util.getCurrentTile())) {
        Walking.walk(FRUIT_STALL_AREA.getRandomTile());
        Sleep.sleep(Calculations.random(2500, 3500));
      }
    }
  }

  public void bankFruit() {
    Logger.log("Traveling to Bank.");
    if (!Util.isDoorOpen()) {
      Util.openClosedDoor();
    }
    while (!Util.getCurrentTile().equals(BANK_TILE)) {
      Walking.walk(BANK_TILE);
      Sleep.sleep(Calculations.random(2000, 3000));
    }

    Logger.log("Banking fruit.");
    Util.openBank();
    Util.depositInventory();
    Bank.close();
  }

  public void stealFruit() {

    Sleep.sleepUntil(() -> Util.isFruitAvailable(), 5000);
    Sleep.sleep(Calculations.random(50, 300));

    Logger.log("Stealing fruit.");
    GameObject[] objectsOnFruitStallTile = GameObjects.getObjectsOnTile(FRUIT_STALL_TILE);
    for (GameObject object : objectsOnFruitStallTile) {
      if (object.getID() == FRUIT_STALL_ID) {
        object.interact();
        Sleep.sleepUntil(() -> !Util.isFruitAvailable(), 5000);
      }
    }
  }
}
