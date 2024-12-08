package com.flask1983.grandexchange.nodes;

public abstract class Node {
  public abstract boolean accept(int cycle);

  public abstract int execute();
}
