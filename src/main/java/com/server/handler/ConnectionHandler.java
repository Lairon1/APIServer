package com.server.handler;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Logger;

public class ConnectionHandler implements Runnable{

    private Logger logger = Logger.getLogger(this.getClass().getSimpleName());
    private Socket socket;

    public ConnectionHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            Scanner scanner = new Scanner(socket.getInputStream());
//            while (scanner.hasNext()) {
//                /*
//                 * Будем использовать System.out.println для того чтобы заголовки вывелись одним сообщением.
//                 */
//                System.out.println(scanner.nextLine());
//            }

            /*
             *  Для отправки потока байт данных в java есть класс OutputStream
             *  Но мы будем использовать обертку для него,
             *  так-же как мы делали со Scanner,
             *  чтобы можно было отправлять строки а не только байты
             */
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true); // true в конце означает то что все сообщения будут сразу отправляться.

            sendHeaders(printWriter);

            //Отправляем сообщение браузеру.
            printWriter.println("Да здравствует мир");
            //И закрываем соединение с клиентом чтобы браузер мог закончить чтение.
            socket.close();
        } catch (IOException e) {
            logger.warning(e.getMessage());
        }
    }

    private void sendHeaders(PrintWriter printWriter){
        //Отправляем заголовки и пустую строчку после них по правилам протокола HTTP
        printWriter.println("HTTP/1.x 200 OK");
        printWriter.println("Content-Type: text/html; charset=UTF-8");
        printWriter.println("");
    }
}
