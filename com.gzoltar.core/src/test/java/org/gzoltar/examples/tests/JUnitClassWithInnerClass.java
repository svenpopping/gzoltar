/**
 * Copyright (C) 2019 GZoltar contributors.
 * 
 * This file is part of GZoltar.
 * 
 * GZoltar is free software: you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 * 
 * GZoltar is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with GZoltar. If
 * not, see <https://www.gnu.org/licenses/>.
 */
package org.gzoltar.examples.tests;

import org.junit.Assert;
import org.junit.Test;

public class JUnitClassWithInnerClass {

  public class SomeInnerClass extends JUnitClassWithInnerClass {
    // empty
  }

  public static class SomeStaticInnerClass extends JUnitClassWithInnerClass {
    @Test
    public void test_2() {
      Assert.assertNotNull(new SomeStaticInnerClass());
    }
  }

  @Test
  public void test_1() {
    Assert.assertNotNull(new SomeInnerClass());
  }
}
