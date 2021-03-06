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
package com.gzoltar.agent.rt.output;

import java.io.IOException;
import com.gzoltar.core.model.Transaction;
import com.gzoltar.core.spectrum.ISpectrum;

public interface IAgentOutput {

  /**
   * Write a spectrum instance to a location determined by the agent controller.
   * 
   * @param spectrum a {@link com.gzoltar.core.spectrum.ISpectrum} object
   * @throws Exception in case writing fails
   */
  public void writeSpectrum(final ISpectrum spectrum) throws Exception;

  /**
   * Write a transaction instance to a location determined by the agent controller.
   * 
   * @param transaction a {@link com.gzoltar.core.model.Transaction} object
   * @throws IOException in case writing fails
   */
  public void writeTransaction(final Transaction transaction) throws IOException;
}
