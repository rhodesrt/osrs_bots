package com.flask1983.grandexchange.nodes;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.widgets.WidgetChild;

import com.flask1983.grandexchange.util.Util;

public class LeatherCrafterNode extends Node {
  // cowhide = 1739
  // hard leather = 1743
  // green dhide = 1753
  // green dleather = 1745
  // hardleather body = 1131
  // green vambs = 1065
  // green chaps = 1099
  private int LEATHER_ID = 1745;
  private int THREAD_ID = 1734;
  private int NEEDLE_ID = 1733;
  private int PRODUCT_ID = 1099;
  // 270, 14, 29 = hardleather body widget
  // 270, 15, 29 = green vambs widget
  // 270, 16, 29 = green vambs widget

  public boolean accept(int i) {
    return true;
  }

  public int execute() {
    if (Inventory.onlyContains(THREAD_ID, NEEDLE_ID, LEATHER_ID)) {
      Inventory.interact(NEEDLE_ID);
      Inventory.interact(LEATHER_ID);
      Sleep.sleepUntil(() -> Widgets.get(270, 14, 29) != null, 5000);
      WidgetChild PRODUCT_WIDGET = Widgets.get(270, 16, 29);
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
