package com.gzoltar.core.spectrum;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.gzoltar.core.model.Node;
import com.gzoltar.core.model.Transaction;
import com.gzoltar.core.model.Tree;

public class SpectrumImpl implements ISpectrum {

  private Tree tree;

  private final ArrayList<Integer> probes;

  private final List<Transaction> transactions;

  public SpectrumImpl() {
    this.tree = new Tree();
    this.probes = new ArrayList<Integer>();
    this.transactions = new ArrayList<Transaction>();
  }

  @Override
  public int getComponentsSize() {
    return this.probes.size();
  }

  @Override
  public int getTransactionsSize() {
    return this.transactions.size();
  }

  @Override
  public boolean isInvolved(final int transactionId, final int componentID) {
    return this.transactions.get(transactionId).getActivity().get(componentID);
  }

  @Override
  public boolean isError(final int transactionId) {
    return this.transactions.get(transactionId).isError();
  }

  public void addTransaction(final String transactionName, final boolean[] activity,final  boolean isError) {
    Transaction t = new Transaction(transactionName, activity, isError);
    if (t.hasActivations()) {
      this.transactions.add(t);
    }
  }

  public void addTransaction(final String transactionName, final boolean[] activity, final int hashCode,
      final boolean isError) {
    Transaction t = new Transaction(transactionName, activity, hashCode, isError);
    if (t.hasActivations()) {
      this.transactions.add(t);
    }
  }

  @Override
  public Tree getTree() {
    return this.tree;
  }

  public void setTree(final Tree tree) {
    this.tree = tree;
  }

  public void addProbe(final int id, final int nodeId) {
    this.probes.ensureCapacity(id + 1);

    while (this.probes.size() <= id) {
      this.probes.add(null);
    }

    this.probes.set(id, nodeId);
  }

  @Override
  public List<Integer> getTestFrequencyPerProbe() {
    ArrayList<Integer> testFrequency = new ArrayList<Integer>();
    testFrequency.ensureCapacity(getComponentsSize());

    for (int p = 0; p < this.getComponentsSize(); p++) {
      Set<Integer> s = new HashSet<Integer>();
      for (Transaction t : this.transactions) {
        if (t.getActivity().get(p)) {
          s.add(t.getActivity().hashCode());
        }
      }

      testFrequency.add(s.size());
    }

    return testFrequency;
  }

  @Override
  public List<Integer> getTestFrequencyPerNode() {
    ArrayList<Integer> nodeTestFrequency = new ArrayList<Integer>();
    nodeTestFrequency.ensureCapacity(getComponentsSize());

    for (int i = 0; i < this.tree.size(); i++) {
      nodeTestFrequency.add(0);
    }

    List<Integer> testFrequency = this.getTestFrequencyPerProbe();
    for (int p = 0; p < this.getComponentsSize(); p++) {
      int freq = testFrequency.get(p);
      int nodeId = this.probes.get(p);
      nodeTestFrequency.set(nodeId, freq + nodeTestFrequency.get(nodeId));
    }

    return nodeTestFrequency;
  }

  public void print() {
    this.tree.print();
    System.out.println("Probe mapping:" + probes);

    for (int t = 0; t < this.getTransactionsSize(); t++) {
      for (int c = 0; c < this.getComponentsSize(); c++) {
        if (this.isInvolved(t, c)) {
          System.out.print("1 ");
        } else {
          System.out.print("0 ");
        }
      }

      if (this.isError(t)) {
        System.out.print("x");
      } else {
        System.out.print(".");
      }

      System.out.println(" hc: " + this.getTransactionActivity(t).hashCode() + " / "
          + this.getTransactionHashCode(t));
    }

    System.out.println("Number of probes: " + this.probes.size() + " number of transactions: "
        + this.transactions.size());
  }

  @Override
  public BitSet getTransactionActivity(final int transactionId) {
    return this.transactions.get(transactionId).getActivity();
  }

  @Override
  public String getTransactionName(final int transactionId) {
    return this.transactions.get(transactionId).getName();
  }

  @Override
  public List<Integer> getActiveComponentsInTransaction(final int transactionId) {
    return this.transactions.get(transactionId).getActiveComponents();
  }

  @Override
  public Node getNodeOfProbe(int probeId) {
    int nodeId = this.probes.get(probeId);
    return this.tree.getNode(nodeId);
  }

  @Override
  public int getTransactionHashCode(final int transactionId) {
    return this.transactions.get(transactionId).hashCode();
  }

  public double getMaxCompTrans(final int componentID) {
    double n = 0;
    for (int t = 0; t < this.getTransactionsSize(); t++) {
      int cc = this.probes.indexOf(componentID);
      if (cc != -1 && this.isInvolved(t, cc)) {
        int nact = this.transactions.get(t).getActivity().cardinality();
        n = nact > n ? nact : n;
      }
    }
    return n;
  }

  public double getMinCompTrans(final int componentID) {
    double n = this.getComponentsSize() + 1;
    for (int t = 0; t < this.getTransactionsSize(); t++) {
      int cc = this.probes.indexOf(componentID);
      if (cc != -1 && this.isInvolved(t, cc)) {
        int nact = this.transactions.get(t).getActivity().cardinality();
        n = nact < n ? nact : n;
      }
    }
    if (n == this.getComponentsSize() + 1) {
      return 0;
    } else {
      return n;
    }
  }

  @Override
  public int getProbeOfNode(final int nodeId) {
    return this.probes.indexOf(nodeId);
  }

  public List<Transaction> getTransactions() {
    return this.transactions;
  }

}
