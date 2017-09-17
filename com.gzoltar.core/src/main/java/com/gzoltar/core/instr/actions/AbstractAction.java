package com.gzoltar.core.instr.actions;

import com.gzoltar.core.instr.Outcome;
import com.gzoltar.core.instr.matchers.IMatcher;
import javassist.CtBehavior;
import javassist.CtClass;

public abstract class AbstractAction implements IAction {

  private final IMatcher matcher;

  public AbstractAction(final IMatcher matcher) {
    this.matcher = matcher;
  }

  @Override
  public final Outcome getAction(final CtClass c) {
    return getAction(this.matcher.matches(c));
  }

  @Override
  public final Outcome getAction(final CtBehavior b) {
    return getAction(this.matcher.matches(b));
  }

  protected abstract Outcome getAction(boolean matches);
}
