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

public class Util {

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

  public static void logout() {
    if (Client.isLoggedIn()) {
      Client.getInstance().getRandomManager().disableSolver(RandomEvent.LOGIN);
      Tabs.logout();
    }
  }

  public static int getRemainingSpace(String input) {
    Pattern pattern = Pattern.compile("Space:\\s*(\\d+)");
    Matcher matcher = pattern.matcher(input);

    if (matcher.find()) {
      int space = Integer.parseInt(matcher.group(1));
      return space;
    }

    return 0;
  }

  public static int remainingDirtNeeded() {
    int[] spaceWidgetNums = { 382, 6 };
    int numPaydirt = Inventory.count("Pay-dirt");
    int hopperSpace = Util.getRemainingSpace(Widgets.get(spaceWidgetNums[0], spaceWidgetNums[1]).getText());
    Logger.log(hopperSpace - numPaydirt);
    return hopperSpace - numPaydirt;
  }
}
