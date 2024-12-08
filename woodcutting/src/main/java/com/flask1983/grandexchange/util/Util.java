package com.flask1983.grandexchange.util;

import java.util.Random;

import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Tile;

public class Util {
  public static Tile getCurrentTile() {
    return Players.getLocal().getTile();
  }

  public static int generateRandom(int i) {
    Random random = new Random();
    return random.nextInt(i + 1);
  }
}
