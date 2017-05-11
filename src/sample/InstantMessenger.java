package sample;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * Created by aliot on 09.04.2017.
 */
public class InstantMessenger {
    public String sender;
    private ArrayList<MessageListener> listeners;
    private static final int SERVER_PORT = 4572;
    Socket socket;

    public InstantMessenger() {

        listeners = new ArrayList<MessageListener>();
        startServer();
    }

    public void closeSocket(){

    }

    public void sendMessage(String senderName, String destinationAddress, String message) throws UnknownHostException, IOException {
        try {


            this.socket = new Socket(destinationAddress, SERVER_PORT);
            socket.setReuseAddress(true);
            // Открываем поток вывода данных
            final DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            // Записываем в поток имя
            out.writeUTF(senderName);

            // Записываем в поток сообщение
            out.writeUTF(message);

            // Закрываем сокет
            socket.close();
        } catch (UnknownHostException e) {
            throw e;

        } catch (IOException e) {
            throw e;
        }
    }

    private void startServer() {
        new Thread(() -> {
            try {
                final ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
                while (!Thread.interrupted()) {
                    final Socket socket1 = serverSocket.accept();
                    final DataInputStream in = new DataInputStream(
                            socket1.getInputStream());

                    // Читаем имя отправителя
                    final String senderName = in.readUTF();

                    // Читаем сообщение
                    final String message = in.readUTF();

                    // Закрываем соединение
                    socket1.close();

                    // Выделяем IP-адрес
                    final String address = ((InetSocketAddress) socket1
                            .getRemoteSocketAddress())
                            .getAddress()
                            .getHostAddress();

                    for (MessageListener listener : listeners) {
                        listener.messageReceived(senderName, message);
                    }

                    // Выводим сообщение в текстовую область

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void addMessageListener(MessageListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }

    public void removeMessageListener(MessageListener listener) {
        synchronized (listeners) {
            listeners.remove(listener);
        }
    }

    private void notifyListeners(String sender, String message) {
        synchronized (listeners) {
            for (MessageListener listener : listeners) {
                listener.messageReceived(sender, message);
            }
        }

    }
}
