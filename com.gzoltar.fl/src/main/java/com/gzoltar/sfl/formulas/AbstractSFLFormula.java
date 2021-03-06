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
package com.gzoltar.sfl.formulas;

import com.gzoltar.core.model.Transaction;
import com.gzoltar.core.runtime.Probe;
import com.gzoltar.core.runtime.ProbeGroup;
import com.gzoltar.core.spectrum.ISpectrum;

public abstract class AbstractSFLFormula implements ISFLFormula {

  /**
   * {@inheritDoc}
   */
  public void diagnose(final ISpectrum spectrum) {
    int n00 = 0;
    int n01 = 0;
    int n10 = 0;
    int n11 = 0;

    for (ProbeGroup probeGroup : spectrum.getProbeGroups()) {
      for (Probe probe : probeGroup.getProbes()) {
        n00 = n01 = n10 = n11 = 0;

        for (Transaction transaction : spectrum.getTransactions()) {
          boolean hasFailed = transaction.hasFailed();

          if (transaction.isProbeActived(probeGroup, probe.getArrayIndex())) {
            if (hasFailed) {
              n11++;
            } else {
              n10++;
            }
          } else {
            if (hasFailed) {
              n01++;
            } else {
              n00++;
            }
          }
        }

        probe.getNode().addSuspiciousnessValue(this.getName(), this.compute(n00, n01, n10, n11));
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  public abstract String getName();

  /**
   * {@inheritDoc}
   */
  public abstract double compute(final double n00, final double n01, final double n10,
      final double n11);
}
