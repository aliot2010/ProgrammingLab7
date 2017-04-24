package sample;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebView;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by aliot on 15.04.2017.
 */
public class AppViewController implements MessageListener{
    @FXML
    private Button sendButton;
    @FXML
    private WebView webView;
    @FXML
    private TextArea messageTextField;
    @FXML
    private TextField fromTextField;
    @FXML
    private TextField toTextField;
    @FXML
    private CheckBox boldCheck;
    @FXML
    private CheckBox italicCheck;
    @FXML
    private CheckBox underlineCheck;

    private Main mainApp;
    public InstantMessenger instantMessenger;
    private ArrayList<String> listOfMessagesInHTML;

    public  AppViewController() {}

    @FXML
    private void initialize() {
        InstantMessenger messenger = new InstantMessenger();
        messenger.addMessageListener(this);
        this.instantMessenger = messenger;
        listOfMessagesInHTML = new ArrayList<String>();
        sendButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                sendMessage();
            }
        });
    }

    private void sendMessage() {
        String messageText = messageWithHTML(messageTextField.getText());
        String senderName = senderNameToHTML(fromTextField.getText());
        try {
            instantMessenger.sendMessage(senderName, toTextField.getText(), messageText);
            senderName = senderNameToHTML("Я");
            listOfMessagesInHTML.add(senderName + "("+toTextField.getText()+ "): " + messageText);
        } catch (IOException e) {
            e.printStackTrace();
        }

        messageTextField.clear();
        this.recreateWebView();
    }

    private String messageWithHTML(String text) {
        //TODO
        StringBuffer openedTegs = new StringBuffer();
        StringBuffer closedTegs = new StringBuffer();
        if (boldCheck.isSelected()) {
            openedTegs.append("<b>");
            closedTegs.append("</b>");
        }
        if (italicCheck.isSelected()) {
            openedTegs.append("<i>");
            closedTegs.append("</i>");
        }
        if (underlineCheck.isSelected()) {
            openedTegs.append("<u>");
            closedTegs.append("</u>");
        }



        return  openedTegs.toString() + text + closedTegs.toString() + "</small></p>";
    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

    private String senderNameToHTML(String name) {
        return "<p><small>" + name + " -> ";
    }

    @Override
    public void messageReceived(String senderName, String message) {
        listOfMessagesInHTML.add(senderName + message);
        recreateWebView();
    }

    private void recreateWebView() {
        StringBuffer allTextInwebView = new StringBuffer();
        for (String rowString : listOfMessagesInHTML) {
            allTextInwebView.append(rowString);
        }
        Platform.runLater(() -> {
            this.webView.getEngine().loadContent(allTextInwebView.toString());
        });

    }
}
