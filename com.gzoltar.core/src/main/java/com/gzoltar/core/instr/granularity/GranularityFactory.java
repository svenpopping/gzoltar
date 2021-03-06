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
package com.gzoltar.core.instr.granularity;

import javassist.CtClass;
import javassist.bytecode.MethodInfo;

public class GranularityFactory {

  public static IGranularity getGranularity(final CtClass ctClass, final MethodInfo methodInfo,
      final GranularityLevel level) {
    switch (level) {
      case LINE:
        return new LineGranularity(ctClass, methodInfo);
      case METHOD:
        return new MethodGranularity(ctClass, methodInfo);
      case BASICBLOCK:
      default:
        return new BasicBlockGranularity(ctClass, methodInfo);
    }
  }

}
