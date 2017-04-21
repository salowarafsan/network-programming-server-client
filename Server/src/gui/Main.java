package gui;

import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class Main extends Application {             // Application class - from which JavaFX application extends

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        ServerSocket serverSocket;
        Socket socket;
        ObjectInputStream objectInputStream = null;
        ObjectOutputStream objectOutputStream = null;

        //enemy and player coordinates initialization
        int enemyX = 50, enemyY = 50;
        int playerX, playerY;
        
        //randomize the position, x & y coordinates
        Random random = new Random(System.currentTimeMillis());
        playerX = Math.abs(random.nextInt()) % 350 + 50;
        playerY = Math.abs(random.nextInt()) % 350 + 50;
        
        //code for connection, data passing between client and server
        try {
            serverSocket = new ServerSocket(1234);
            socket = serverSocket.accept();
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

            objectOutputStream.writeInt(playerX);           //send the value of coordinate X
            objectOutputStream.flush();                     //clear
            objectOutputStream.writeInt(playerY);           //send the value of coordinate Y
            objectOutputStream.flush();                     //clear

            enemyX = objectInputStream.readInt();           //retrieving the X xoordinate from enemy (client)
            enemyY = objectInputStream.readInt();           //retrieving the Y xoordinate from enemy (client)

        } catch (IOException e) {
            e.printStackTrace();
        }

        primaryStage.setTitle("Server");            
        primaryStage.setOnCloseRequest(e -> System.exit(0));    //clicking on close

        Group group = new Group();          //creating object of javafx group class
        Scene scene = new Scene(group);     //Creates a Scene for Group
        primaryStage.setScene(scene);       //Sets the value of the property scene. The Scene to be rendered on this Stage.
        primaryStage.setWidth(500);         //setting the width
        primaryStage.setHeight(500);        //setting the height
        primaryStage.setResizable(false);
        scene.setCursor(Cursor.NONE);
        primaryStage.show();

        new MainBoard(group, scene, objectOutputStream, objectInputStream ,
                playerX, playerY, enemyX, enemyY, random);              //passing all the necessary values to constructor of MainBoard class

    }
}
