package com.flask1983.grandexchange;

import java.util.concurrent.TimeUnit;
import java.awt.Graphics;

import org.dreambot.api.Client;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.randoms.RandomEvent;
import org.dreambot.api.randoms.RandomManager;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;

import com.flask1983.grandexchange.nodes.Node;
import com.flask1983.grandexchange.util.Util;
import com.flask1983.grandexchange.nodes.LeatherCrafterNode;

@ScriptManifest(name = "Leather Crafter", description = "Leather Crafter script.", author = "flask1983", version = 1.0, category = Category.CRAFTING, image = "")
public final class LeatherCrafter extends AbstractScript {
  private int MAX_RUN_TIME = 120;

  private long timeBegan;
  private long runTime;
  private int beginXp;
  private int currentXp;
  private int gainedXp;

  private final Node[] nodes = {
      new LeatherCrafterNode()
  };

  @Override
  public void onStart() {
    timeBegan = System.currentTimeMillis();
    beginXp = Skills.getExperience(Skill.CRAFTING);

    Logger.log("Leather Crafter script starting up.");
  }

  public int onLoop() {
    for (final Node node : nodes) {
      if (node.accept(1))
        node.execute();
    }

    if (TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis() - this.timeBegan) >= MAX_RUN_TIME) {
      Util.logout();
      return -1;
    }
    return Calculations.random(100, 1000); // ms until loop runs again
  }

  public void onPaint(Graphics g) {
    int lowestYText = 327;
    int leftX = 15;
    int yStep = 15;

    runTime = System.currentTimeMillis() - this.timeBegan;
    currentXp = Skills.getExperience(Skill.CRAFTING);
    gainedXp = currentXp - beginXp;
    int xpPerHour = (int) (gainedXp / (runTime / 1000.0 / 60.0 / 60.0)) / 1000;
    g.drawString(ft(runTime), leftX, lowestYText);
    g.drawString("xp/hr: " + xpPerHour + "k", leftX, lowestYText - yStep);
    g.drawString("xp gained: " + gainedXp, leftX, lowestYText - yStep * 2);
    g.drawString("Crafting: " + Skills.getRealLevel(Skill.CRAFTING), leftX, lowestYText - yStep * 3);
  }

  private String ft(long duration) {
    String res = "";
    long days = TimeUnit.MILLISECONDS.toDays(duration);
    long hours = TimeUnit.MILLISECONDS.toHours(duration)
        - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(duration));
    long minutes = TimeUnit.MILLISECONDS.toMinutes(duration)
        - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS
            .toHours(duration));
    long seconds = TimeUnit.MILLISECONDS.toSeconds(duration)
        - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
            .toMinutes(duration));
    if (days == 0) {
      res = (hours + ":" + minutes + ":" + seconds);
    } else {
      res = (days + ":" + hours + ":" + minutes + ":" + seconds);
    }
    return res;
  }
}
