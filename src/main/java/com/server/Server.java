package com.server;

import com.server.handler.ConnectionHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class Server {

    private ServerSocket serverSocket; //объявляем переменную типа ServerSocket.
    private Logger logger = Logger.getLogger(this.getClass().getSimpleName()); //Получаем Logger класса и помещаем его в поле типа Logger.
    private ExecutorService threadPool = Executors.newCachedThreadPool();

    public void start(int port) {
        try {
            serverSocket = new ServerSocket(port); //Задаем и выделяем память для переменной.
            logger.info("Server started."); //Оповещаем пользователя что сервер запущен.
            while (true) {
                Socket socket = serverSocket.accept(); //Ожидаем подключение.
                logger.info(String.format("Client %s connected to the server.", socket.getInetAddress())); //Подробнее про String.format вы можете прочитать тут https://hr-vector.com/java/formatirovanie-chisel-strok

                //Создаем новый объект нашего класса ConnectionHandler и передаем в конструктор Socket
                ConnectionHandler connectionHandler = new ConnectionHandler(socket);
                //Далее вызываем у ранее созданного ThreadPool метод который отвечает за запуск потока.
                //В этот метод нам нужно передать connectionHandler.
                threadPool.execute(connectionHandler);
            }
        } catch (IOException e) {
            logger.warning(e.getMessage()); //в случае ошибки выдаем warning в консоль.
        }
    }

}
