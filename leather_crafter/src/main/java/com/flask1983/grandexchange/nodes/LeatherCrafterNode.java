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
import org.dreambot.api.methods.widget.Widget;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.randoms.RandomEvent;
import org.dreambot.api.script.listener.ExperienceListener;

public class LeatherCrafterNode extends Node {
  private int LEATHER_ID = 1743;
  private int THREAD_ID = 1734;
  private int NEEDLE_ID = 1733;
  private int PRODUCT_ID = 1131;
  // 270, 14, 29 is hardleather body widget

  public boolean accept(int i) {
    return true;
  }

  public int execute() {
    if (Inventory.onlyContains(THREAD_ID, NEEDLE_ID, LEATHER_ID)) {
      Inventory.interact(NEEDLE_ID);
      Inventory.interact(LEATHER_ID);
      Sleep.sleepUntil(() -> Widgets.get(270, 14, 29) != null, 5000);
      WidgetChild PRODUCT_WIDGET = Widgets.get(270, 14, 29);
      PRODUCT_WIDGET.interact();
      Sleep.sleepUntil(() -> (Inventory.onlyContains(THREAD_ID, NEEDLE_ID, PRODUCT_ID)), 60000);

      Util.openBank();
      Bank.depositAll(PRODUCT_ID);
      Bank.withdrawAll(LEATHER_ID);
      Bank.close();
      Sleep.sleep(Calculations.random(400, 1000));
    }
    return 1;
  }
}
