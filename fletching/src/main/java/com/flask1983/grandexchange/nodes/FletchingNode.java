package com.flask1983.grandexchange.nodes;

import java.util.ArrayList;
import java.util.Arrays;

import org.dreambot.api.utilities.AccountManager;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
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
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.randoms.RandomEvent;

public class FletchingNode extends Node {
  private int KNIFE = 946;
  private int MAPLE_LONGBOW_U = 62;
  private int MAPLE_LONGBOW = 851;
  private int MAPLE_LOGS = 1517;
  private int BOWSTRING = 1777;

  public boolean accept(int i) {
    return true;
  }

  public int execute() {
    if (Players.getLocal().isAnimating()) {
      return 1;
    }

    Util.openBank();
    if (!Bank.contains(MAPLE_LOGS) && (!Bank.contains(MAPLE_LONGBOW_U) || !Bank.contains(BOWSTRING))) {
      Util.logout();
      return -1;
    }

    if (Bank.contains(MAPLE_LOGS)) {
      if (!Inventory.contains(KNIFE)) {
        Bank.withdraw(KNIFE);
      }
      if (!Inventory.contains(MAPLE_LOGS) && Inventory.contains(MAPLE_LONGBOW_U)) {
        Bank.depositAll(MAPLE_LONGBOW_U);
      }
      Bank.withdrawAll(MAPLE_LOGS);
      Bank.close();

      Inventory.interact(KNIFE);
      Inventory.interact(MAPLE_LOGS);
      Sleep.sleep(Calculations.random(1000, 1500));

      WidgetChild unstrungLongbow = Widgets.get(270, 16, 29);
      if (unstrungLongbow != null) {
        unstrungLongbow.interact();
      }
      Sleep.sleepUntil(() -> Players.getLocal().isAnimating(), 4000);
    } else {
      if (Bank.contains(MAPLE_LONGBOW_U) && Bank.contains(BOWSTRING)) {
        if (Inventory.contains(KNIFE)) {
          Bank.deposit(KNIFE);
        }
        if (!Inventory.contains(BOWSTRING) || !Inventory.contains(MAPLE_LONGBOW_U)) {
          if (Inventory.contains(MAPLE_LONGBOW)) {
            Bank.depositAll(MAPLE_LONGBOW);
          }
          Bank.withdraw(MAPLE_LONGBOW_U, 14);
          Bank.withdraw(BOWSTRING, 14);
          Bank.close();

          Inventory.interact(MAPLE_LONGBOW_U);
          Inventory.interact(BOWSTRING);
          Sleep.sleep(Calculations.random(1000, 1500));

          WidgetChild strungBow = Widgets.get(270, 14, 29);
          if (strungBow != null) {
            strungBow.interact();
          }
          Sleep.sleepUntil(() -> Inventory.onlyContains(MAPLE_LONGBOW), 25000);
        }
      }
    }

    return 1;
  }
}
