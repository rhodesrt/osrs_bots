package com.flask1983.grandexchange.util;

import org.dreambot.api.input.Mouse;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.ViewportTools;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.item.GroundItems;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.items.GroundItem;

public class Util {
  public static Tile getCurrentTile() {
    return Players.getLocal().getTile();
  }

  public static void interactWithObstacle(int id) {
    GameObject obstacle = GameObjects.closest(id);
    obstacle.interact();
    randomMouseMovement();
    Sleep.sleep(1000);
  }

  public static void pickupMark() {
    GroundItem mark = GroundItems.closest("Mark of grace");
    if (mark != null && mark.distance(getCurrentTile()) <= 10) {
      mark.interact("Take");
      randomMouseMovement();
      Sleep.sleep(1000);
      Sleep.sleepUntil(() -> Players.getLocal().isStandingStill(), 4000);
    }
  }

  public static void randomMouseMovement() {
    Sleep.sleep(Calculations.random(50, 300));
    Mouse.move(ViewportTools.getRandomPointOnCanvas());
  }
}
