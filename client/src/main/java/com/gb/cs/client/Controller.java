package com.gb.cs.client;

import com.gb.cs.client.fm.FileManager;
import com.gb.cs.common.AuthMess;
import com.gb.cs.common.CommandType;
import com.gb.cs.common.FileMessage;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    TextField loginField, password;
    @FXML
    HBox hboxTop;
    @FXML
    VBox loginPane;
    @FXML
    Button bLogin;
    @FXML
    ListView clientView, serverView;

    private Network connection;
    private FileManager fm;
    private String clientsRoot;
    private String login;

    private String host;
    private int port;

    public Controller() {
        host = "127.0.0.1";
        port = 8189;
        clientsRoot = System.getenv().get("USERPROFILE") + "\\GB-Cloud-Client";
        fm = new FileManager(clientsRoot, this);
        connection = new NettyNetwork(host, port, this);
    }

    public String getClientHome() {
        String result = null;
        if (login != null)
            result = clientsRoot + "\\" + login;
        else
            result = null;

        return result;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        login();
    }

    @FXML
    private void login() {
        AuthMess com = new AuthMess("cloudman1", "123");
        connection.sendMessage(com);
    }

    //todo
    private void setVisible() {
        loginField.setVisible(false);
        password.setVisible(false);
        bLogin.setVisible(false);
        loginPane.setVisible(false);
        loginPane.maxHeight(0.0);
        hboxTop.setVisible(true);
    }

    public void fillPane(List<String> list, boolean isServerView) {
        ListView view;

        if (isServerView)
            view = serverView;
        else
            view = clientView;

        Platform.runLater(() -> {
            view.getItems().clear();
            for (String el : list) {
                view.getItems().add(el);
            }
        });
    }

    public void onMouseClicked(MouseEvent mouseEvent) {
        int clickedCount = mouseEvent.getClickCount();
        //System.out.println(clickedCount); остается вопрос почему он считает 3 нажатия, хотя их было 2
        if (clickedCount == 2) {
            ListView source = (ListView) mouseEvent.getSource();
            int selectedIndex = source.getSelectionModel().getSelectedIndex();
            if (selectedIndex != -1) {
                switch (source.getId()) {
                    case "serverView":
                        System.out.println(source.getItems().get(selectedIndex).toString());
                        connection.sendMessage(
                                new FileMessage(CommandType.READ_FILE)
                                        .fillItems(Arrays.asList(source.getItems().get(selectedIndex).toString())));

                        break;
                    case "clientView":
                        //todo
                        System.out.println(source.getItems().get(selectedIndex).toString());
                        break;
                }
            }
        }
    }

    public void setLogin(String login) {
        this.login = login;
        createClientHome(getClientHome());
    }

    private void createClientHome(String home){
        fm.createClientHome(home);
        fm.chDir(home);
    }

    public void writeFile(byte [] file, String name) {
        fm.writeFile(file,name);
    }
}
