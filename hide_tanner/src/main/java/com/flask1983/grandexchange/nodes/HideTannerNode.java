package com.flask1983.grandexchange.nodes;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.widgets.WidgetChild;

import com.flask1983.grandexchange.util.Util;

public class HideTannerNode extends Node {
  private Area BANK_AREA = new Area(3269, 3165, 3271, 3168, 0);
  private int ELLIS_ID = 3231;
  private Area TANNERY_AREA = new Area(3271, 3189, 3277, 3193, 0);
  private String ELLIS_ACTION = "Trade";
  private String LEATHER_WIDGET_ACTION = "Tan All";

  // cowhide = 1739
  // hard leather = 1743
  // green dhide = 1753
  // green dleather = 1745
  private int HIDE_ID = 1753;
  private int LEATHER_ID = 1745;

  public boolean accept(int i) {
    return true;
  }

  public int execute() {

    if (Inventory.contains(HIDE_ID) && !TANNERY_AREA.contains(Util.getCurrentTile())) {
      walkToTannery();
    } else if (Inventory.contains(HIDE_ID) && TANNERY_AREA.contains(Util.getCurrentTile())) {
      tanHides();
    } else if (Inventory.contains(LEATHER_ID) && !BANK_AREA.contains(Util.getCurrentTile())) {
      walkToBank();
    } else if (Inventory.contains(LEATHER_ID) && BANK_AREA.contains(Util.getCurrentTile())) {
      bankAllLeather();
      Sleep.sleep(Calculations.random(100, 1000));
      Util.withdrawHides();
      Sleep.sleep(Calculations.random(100, 1000));

      Bank.close();
    } else if (!Inventory.contains(LEATHER_ID, HIDE_ID) && BANK_AREA.contains(Util.getCurrentTile())) {
      Util.openBank();
      Util.withdrawHides();
      Sleep.sleep(Calculations.random(100, 1000));
      Bank.close();
    }

    return 1;
  }

  public void walkToTannery() {
    while (!TANNERY_AREA.contains(Util.getCurrentTile())) {
      Walking.walk(TANNERY_AREA.getRandomTile());
      Sleep.sleep(Calculations.random(2000, 3000));
    }
  }

  public void tanHides() {
    NPC ellis = NPCs.closest(ELLIS_ID);
    if (ellis != null) {
      ellis.interact(ELLIS_ACTION);
      Sleep.sleep(Calculations.random(2000, 3000));

      // Hard leather = 324, 101
      // Green dleather = 324, 104
      WidgetChild LEATHER_WIDGET = Widgets.get(324, 104);
      if (LEATHER_WIDGET != null) {
        LEATHER_WIDGET.interact(LEATHER_WIDGET_ACTION);
        Sleep.sleep(Calculations.random(200, 1000));
      }
    }
  }

  public void walkToBank() {
    while (!BANK_AREA.contains(Util.getCurrentTile())) {
      Walking.walk(BANK_AREA.getRandomTile());
      Sleep.sleep(Calculations.random(2000, 3000));
    }
  }

  public void bankAllLeather() {
    Util.openBank();

    Util.depositLeather();
  }
}
