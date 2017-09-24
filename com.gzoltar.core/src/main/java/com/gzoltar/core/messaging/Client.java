package com.gzoltar.core.messaging;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;
import com.gzoltar.core.events.IEventListener;
import com.gzoltar.core.messaging.Message.AddNodeMessage;
import com.gzoltar.core.messaging.Message.AddProbeMessage;
import com.gzoltar.core.messaging.Message.ByeMessage;
import com.gzoltar.core.messaging.Message.EndTransactionMessage;
import com.gzoltar.core.messaging.Message.HandshakeMessage;
import com.gzoltar.core.model.NodeType;

public class Client implements IEventListener {

  private final String host;

  private final int port;

  private final String id;

  private final Queue<Message> messages;

  private Boolean seenByeMessage = false;

  private Socket socket = null;

  private Thread thread = null;

  public Client(final String host, final int port) {
    this.host = host;
    this.port = port;
    this.id = UUID.randomUUID().toString();
    this.messages = new LinkedList<Message>();
  }

  public Client(final int port) {
    this(null, port);
  }

  private synchronized Thread postMessage(final Message m) {
    this.messages.add(m);

    if (this.thread == null) {
      this.thread = new Thread(new ClientDispatcher());
      this.thread.start();
    }

    return this.thread;
  }

  private void postBlockingMessage(final Message m) {
    try {
      postMessage(new ByeMessage()).join(5000);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private synchronized Message getMessage() {
    if (this.messages.size() == 0) {
      this.thread = null;
      return null;
    }

    return this.messages.poll();
  }

  private class ClientDispatcher implements Runnable {

    @Override
    public void run() {
      Message message = getMessage();

      while (message != null) {
        try {
          if (socket == null) {
            socket = new Socket(host, port);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(new HandshakeMessage(id));
            out.flush();
          }

          if (!seenByeMessage) {
            seenByeMessage |= message instanceof ByeMessage;

            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(message);
            out.flush();
          }

          message = getMessage();
        } catch (Exception e) {
          System.err.println("Exception, reseting socket");
          e.printStackTrace();

          socket = null;
          try {
            Thread.sleep(10000);
          } catch (Exception e2) {
            e.printStackTrace();
          }
        }
      }
    }

  }

  @Override
  public void endTransaction(final String transactionName, final boolean[] activity,
      final boolean isError) {
    this.postMessage(new EndTransactionMessage(transactionName, activity, isError));
  }

  @Override
  public void endTransaction(final String transactionName, final boolean[] activity,
      final int hashCode, final boolean isError) {
    this.postMessage(new EndTransactionMessage(transactionName, activity, isError));
  }

  @Override
  public void addNode(final int id, final String name, final NodeType type, final int parentId) {
    this.postMessage(new AddNodeMessage(id, name, type, parentId));
  }

  @Override
  public void addProbe(final int id, final int nodeId) {
    this.postMessage(new AddProbeMessage(id, nodeId));
  }

  @Override
  public void endSession() {
    this.postBlockingMessage(new ByeMessage());
  }

}
