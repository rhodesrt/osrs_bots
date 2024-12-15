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
import org.dreambot.api.methods.magic.Magic;
import org.dreambot.api.methods.magic.Normal;
import org.dreambot.api.methods.magic.Spell;
import org.dreambot.api.methods.magic.Spellbook;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.randoms.RandomEvent;
import org.dreambot.api.script.listener.ExperienceListener;

public class SeersNode extends Node {
  private int MARK_ID = 11849;
  private int LOBSTER_ID = 379;

  private Area UPSTAIRS_BANK_AREA = new Area(2726, 3490, 2729, 3492, 1);
  private int UPSTAIRS_BANK_LADDER_ID = 25939;
  private Area START_AREA = new Area(2728, 3487, 2730, 3489, 0);
  private int COURSE_START_ID = 14927;
  private Area AREA_1 = new Area(2721, 3490, 2730, 3497, 3);
  private int OBSTACLE_ID_1 = 14928;
  private Area AREA_2 = new Area(2713, 3497, 2705, 3488, 2);
  private int OBSTACLE_ID_2 = 14932;
  private Area AREA_3 = new Area(2709, 3481, 2716, 3476, 2);
  private int OBSTACLE_ID_3 = 14929;
  private Area AREA_4 = new Area(2716, 3475, 2700, 3469, 3);
  private int OBSTACLE_ID_4 = 14930;
  private Area AREA_5 = new Area(2703, 3466, 2697, 3459, 2);
  private int OBSTACLE_ID_5 = 14931;

  public boolean accept(int i) {
    return true;
  }

  public int execute() {
    if (Players.getLocal().getHealthPercent() < 50) {
      Inventory.interact(LOBSTER_ID);
      Sleep.sleep(Calculations.random(1000, 2500));
    } else if (Util.getCurrentTile().getZ() == 0 && !START_AREA.contains(Util.getCurrentTile())) {
      Util.pickupMark();
      Walking.walk(START_AREA.getRandomTile());
      Sleep.sleep(Calculations.random(1500, 3000));
    } else if (START_AREA.contains(Util.getCurrentTile())) {
      Util.pickupMark();
      GameObjects.closest(COURSE_START_ID).interact();
      Sleep.sleepUntil(() -> Util.getCurrentTile().getZ() == 3, 5000);
      Sleep.sleep(Calculations.random(500, 700));
    } else if (AREA_1.contains(Util.getCurrentTile())) {
      Util.pickupMark();
      GameObjects.closest(OBSTACLE_ID_1).interact();
      Sleep.sleepUntil(() -> AREA_2.contains(Util.getCurrentTile()), 5000);
      Sleep.sleep(Calculations.random(500, 700));
    } else if (AREA_2.contains(Util.getCurrentTile())) {
      Util.pickupMark();
      GameObjects.closest(OBSTACLE_ID_2).interact();
      Sleep.sleepUntil(() -> AREA_3.contains(Util.getCurrentTile()), 5000);
      Sleep.sleep(Calculations.random(500, 700));
    } else if (AREA_3.contains(Util.getCurrentTile())) {
      Util.pickupMark();
      GameObjects.closest(OBSTACLE_ID_3).interact();
      Sleep.sleepUntil(() -> AREA_4.contains(Util.getCurrentTile()), 5000);
      Sleep.sleep(Calculations.random(500, 700));
    } else if (AREA_4.contains(Util.getCurrentTile())) {
      Util.pickupMark();
      GameObjects.closest(OBSTACLE_ID_4).interact();
      Sleep.sleepUntil(() -> AREA_5.contains(Util.getCurrentTile()), 5000);
      Sleep.sleep(Calculations.random(500, 700));
    } else if (AREA_5.contains(Util.getCurrentTile())) {
      Util.pickupMark();
      GameObjects.closest(OBSTACLE_ID_5).interact();
      Sleep.sleepUntil(() -> Util.getCurrentTile().getZ() == 0, 5000);
      Sleep.sleep(Calculations.random(500, 700));
    } else if (UPSTAIRS_BANK_AREA.contains(Util.getCurrentTile())) {
      GameObjects.closest(UPSTAIRS_BANK_LADDER_ID).interact();
      Sleep.sleepUntil(() -> Util.getCurrentTile().getZ() == 0, 3000);
      Sleep.sleep(Calculations.random(1000, 2000));
    }

    return 1;
  }
}
