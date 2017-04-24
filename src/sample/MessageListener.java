package sample;

/**
 * Created by aliot on 09.04.2017.
 */
public interface MessageListener {
    void messageReceived(String senderName, String message);
}
