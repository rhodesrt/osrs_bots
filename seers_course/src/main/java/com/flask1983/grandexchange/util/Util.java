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
import org.dreambot.api.methods.item.GroundItems;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.randoms.RandomEvent;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.items.GroundItem;
import org.dreambot.api.wrappers.widgets.WidgetChild;

public class Util {
  private static int MARK_ID = 11849;

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

    GameObject bankBooth = GameObjects.closest("Bank booth");
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

  public static void pickupMark() {
    GroundItem mark = GroundItems.closest(MARK_ID);
    if (mark != null && mark.distance() <= 8) {
      int marksInInventory = Inventory.count(MARK_ID);
      mark.interact();
      Sleep.sleepUntil(() -> Inventory.count(MARK_ID) > marksInInventory, 5000);
    }
  }
}
