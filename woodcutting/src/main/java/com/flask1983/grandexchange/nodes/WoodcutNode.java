package com.flask1983.grandexchange.nodes;

import java.util.ArrayList;
import java.util.Arrays;

import org.dreambot.api.utilities.AccountManager;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.widgets.WidgetChild;

import com.flask1983.grandexchange.util.Util;

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
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.methods.widget.Widgets;

public class WoodcutNode extends Node {
  Area bankArea = new Area(2724, 3490, 2727, 3493);
  Area wcArea = new Area(2721, 3498, 2732, 3503);

  public boolean accept(int i) {
    return true;
  }

  public int execute() {
    if (Inventory.isFull() && !bankArea.contains(Util.getCurrentTile())) {
      Walking.walk(bankArea.getRandomTile());
      Sleep.sleep(1000);
      Sleep.sleepUntil(() -> Players.getLocal().isStandingStill(), 10000);
    }

    if (Inventory.isFull() && bankArea.contains(Util.getCurrentTile()) && !Bank.isOpen()) {
      if (Util.generateRandom(20) == 0) {
        Sleep.sleep(Calculations.random(5000, 10000));
      }

      GameObject bankBooth = GameObjects.closest("Bank booth");
      bankBooth.interact("Bank");
      Sleep.sleepUntil(() -> Bank.isOpen(), 5000);

      WidgetChild depositInventory = Widgets.get(12, 44);
      if (depositInventory != null) {
        depositInventory.interact();
      }
    }

    if (Players.getLocal().isAnimating()) {
      if (Util.generateRandom(30) == 0) {
        Mouse.move(ViewportTools.getRandomPointOnCanvas());
      }
      return 1;
    }

    if (!Inventory.isFull() && !wcArea.contains(Util.getCurrentTile())) {
      Walking.walk(wcArea.getRandomTile());
      Sleep.sleep(1000);
      Sleep.sleepUntil(() -> Players.getLocal().isStandingStill(), 10000);
    }

    if (!Inventory.isFull() && wcArea.contains(Util.getCurrentTile())) {
      GameObject tree = GameObjects.closest("Maple tree");
      tree.interact("Chop down");
      Sleep.sleep(1000);
      Sleep.sleepUntil(() -> Players.getLocal().isAnimating(), 4000);
    }
    return 1;
  }
}
