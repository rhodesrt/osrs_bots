package com.flask1983.grandexchange.nodes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.dreambot.api.utilities.AccountManager;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.items.GroundItem;

import com.flask1983.grandexchange.util.Util;

import org.dreambot.api.input.Mouse;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.ViewportTools;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.grandexchange.GrandExchange;
import org.dreambot.api.methods.grandexchange.GrandExchangeItem;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.item.GroundItems;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.walking.impl.Walking;

public class AgilityNode extends Node {
  Area canifisStartArea = new Area(3502, 3490, 3510, 3485, 0);
  int[] canifisObstacleIds = { 14844, 14845, 14848, 14846, 14894, 14847, 14897 };
  Area[] canifisObstacleAreas = {
      new Area(3501, 3503, 3503, 3505, 2),
      new Area(3491, 3503, 3493, 3505, 2),
      new Area(3478, 3498, 3480, 3500, 3),
      new Area(3477, 3485, 3479, 3487, 2),
      new Area(3488, 3475, 3490, 3477, 3),
      new Area(3509, 3475, 3511, 3477, 2),
      new Area(3509, 3484, 3511, 3486, 0)
  };

  public boolean accept(int i) {
    return true;
  }

  public int execute() {
    if (Players.getLocal().isMoving()) {
      return 1;
    }

    if (canifisStartArea.contains(Util.getCurrentTile())) {
      GameObject courseStart = GameObjects.closest("Tall tree");
      courseStart.interact();
      Util.randomMouseMovement();
      Sleep.sleep(Calculations.random(1000, 2000));
      Sleep.sleepUntil(() -> Players.getLocal().isStandingStill(), 4000);
    } else if (Util.getCurrentTile().getZ() == 0) {
      Walking.walk(canifisStartArea.getRandomTile());
      Sleep.sleep(Calculations.random(1000, 2000));
    } else {
      int[] obstacleIds = Arrays.copyOf(canifisObstacleIds, canifisObstacleIds.length);
      Area[] obstacleAreas = Arrays.copyOf(canifisObstacleAreas, canifisObstacleAreas.length);

      for (int i = 0; i < obstacleAreas.length; i++) {
        if (obstacleAreas[i].contains(Util.getCurrentTile())) {
          int j = i + 1;
          Util.pickupMark();
          Util.interactWithObstacle(obstacleIds[j]);
          Sleep.sleepUntil(() -> obstacleAreas[j].contains(Util.getCurrentTile()), 4000);
        }
      }

      for (int i = 0; i < obstacleIds.length; i++) {
        if (Util.getCurrentTile().getZ() == 0)
          break;

        int id = obstacleIds[i];
        Area area = obstacleAreas[i];

        Util.pickupMark();
        Util.interactWithObstacle(id);
        Sleep.sleepUntil(() -> (area.contains(Util.getCurrentTile())), 4000);
        Sleep.sleep(1000);
      }
    }

    return 1;
  }
}
