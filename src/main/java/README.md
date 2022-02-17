
# Разработка сервера на java
## Содержание

1. Создание проекта в IntellijIDEA.
2. Создание пакетов классов.
3. Написание одно поточного сервера.
4. Запуск сервера.
5. Обработка подключений и обмен пакетами
7. Добавление много поточности

### Создание проекта в IntellijIDEA
Для начало давайте создадим проект в IntellijIDEA с прикрученным к нему maven (Далее он нам понадобится).  
При открытии IDE нас приветствует окно создания проекта, это то что нам и нужно.  
Нажимаем New project.  
</br>  
![Screenshot project create](https://github.com/Lairon1/APIServer/blob/images/Screenshot_8.png?raw=true)  
</br>  
На следующем окне выбираем maven и смело жмем next  
</br>  
![Screenshot project create](https://github.com/Lairon1/APIServer/blob/images/Screenshot_9.png?raw=true)  
</br>  
В поле name задаем имя проекту и финишируем создание.
### Создание пакетов классов
В пакете src.main.java создаем еще пару пакетов с таким именем: "com.server".
![enter image description here](https://github.com/Lairon1/APIServer/blob/images/Screenshot_1.png?raw=true)
![enter image description here](https://github.com/Lairon1/APIServer/blob/images/Screenshot_2.png?raw=true)
Далее создаем новый класс "Server", он у нас будет отвечать за запуск сервера и инициализацию других классов.
![enter image description here](https://github.com/Lairon1/APIServer/blob/images/Screenshot_3.png?raw=true)
### Написание одно поточного сервера
Давайте начнем уже создавать свой сервер.
Для начало создадим метод start в класса Server, он и будет отвечать за запуск сервера.
Метод у нас будет принимать 1 параметр. Это port
</br>
```java
public class Server {

    public void start(int port){  
  
    }  

}
```
Для того чтобы запустить слушанье порта, нужно создать ServerSocket.
Так как при создании ServerSocket может вылететь IOException,
который скажет нам о том что выбранный нами порт не доступен,
мы должны обрамить создание Socket'a в блок try-catch.
```java
public class Server {

    private ServerSocket serverSocket; //объявляем переменную типа ServerSocket.

    public void start(int port){
        try {
            serverSocket = new ServerSocket(port); //Задаем и выделяем память для переменной.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
```
Так как мы не хотим хардкодить и просто выплевывать ошибки в консоль, 
мы будем использовать Logger который входит в стандартную sdk java.
```java
public class Server {

    private ServerSocket serverSocket; //объявляем переменную типа ServerSocket.
    private Logger logger = Logger.getLogger(this.getClass().getSimpleName()); //Получаем Logger класса и помещаем его в поле типа Logger.

    public void start(int port){
        try {
            serverSocket = new ServerSocket(port); //Задаем и выделяем память для переменной.
            logger.info("Server started."); //Оповещаем пользователя что сервер запущен.
        } catch (IOException e) {
            logger.warning(e.getMessage()); //в случае ошибки выдаем warning в консоль.
        }
    }

}
```
Далее мы установим наш сервер в режим ожидания клиента,
делается это при помощи ```serverSocket.accept()```
и этот метод вернет нам простой Socket,
из которого можно будет получить информацию о подключившемся клиенте.
Так-же выведем лог в консоль, 
о том что мы получили новое соединение.
```java
    public void start(int port){
        try {
            serverSocket = new ServerSocket(port); //Задаем и выделяем память для переменной.
            logger.info("Server started."); //Оповещаем пользователя что сервер запущен.
            Socket socket = serverSocket.accept(); //Ожидаем подключение.
        logger.info(String.format("Client %s connected to the server.", socket.getInetAddress())); //Подробнее про String.format вы можете прочитать тут https://hr-vector.com/java/formatirovanie-chisel-strok
        } catch (IOException e) {
            logger.warning(e.getMessage()); //в случае ошибки выдаем warning в консоль.
        }
    }
```
### Запуск сервера
Ну что же пора нам попробовать запустить наш сервер.
Для этого создадим класс ServerStarter и определим в нем входную точку main.
```java
public class ServerStarter {

    public static void main(String[] args) {
        
    }
    
}
```
В этом методе мы будем инициализировать и запускать наш сервер.
Запустим сервер на порту 80, который используется для HTTP протокола.
То-есть мы сможем без проблем присоединится к нему через браузер.
```java
public class ServerStarter {

    public static void main(String[] args) {
        new Server().start(80);
    }

}
```
Теперь мы можем запустить наш сервер прямо из IDE.
![](https://github.com/Lairon1/APIServer/blob/images/Screenshot_5.png?raw=true)
Запускаем и видим в консоли что все работает
и сервер выводит информацию что он запущен.
![](https://github.com/Lairon1/APIServer/blob/images/Screenshot_6.png?raw=true)
Попробуем присоединится к нему используя браузер chrome.
![](https://github.com/Lairon1/APIServer/blob/images/Screenshot_7.png?raw=true)
Как мы видим наш сервер обработал подключение, вывел нам лог в консоль
и завершил свою работу.
Для того чтобы сервер не выключался после 1 подключения мы можем
запихнуть его в цикл while.
```java

    public void start(int port) {
        try {
            serverSocket = new ServerSocket(port); //Задаем и выделяем память для переменной.
            logger.info("Server started."); //Оповещаем пользователя что сервер запущен.
            while (true) {
                Socket socket = serverSocket.accept(); //Ожидаем подключение.
                logger.info(String.format("Client %s connected to the server.", socket.getInetAddress())); //Подробнее про String.format вы можете прочитать тут https://hr-vector.com/java/formatirovanie-chisel-strok
            }
        } catch (IOException e) {
            logger.warning(e.getMessage()); //в случае ошибки выдаем warning в консоль.
        }
    }


```
А именно нужно запихнуть все после вызова метода serverSocket.accept();
И теперь у нас есть однопоточный простенький сервер для простого
принятия подключений из вне.
### Обработка подключений и обмен пакетами
Для того чтобы обрабатывать подключение клиентов мы создадим класс
ConnectionHandler в пакете com.server.handler
В котором определим метод handle с параметром типа данных Socket.
```java
public class ConnectionHandler {
    
    public void handle(Socket socket){
        
    }
    
}
```
Теперь в классе Server создаем новый объект класса ConnectionHandler
и вызываем у него метод handle попутно передаем в него наш Socket.
```java
    public void start(int port) {
        try {
            serverSocket = new ServerSocket(port); //Задаем и выделяем память для переменной.
            logger.info("Server started."); //Оповещаем пользователя что сервер запущен.
            while (true) {
                Socket socket = serverSocket.accept(); //Ожидаем подключение.
                logger.info(String.format("Client %s connected to the server.", socket.getInetAddress())); //Подробнее про String.format вы можете прочитать тут https://hr-vector.com/java/formatirovanie-chisel-strok
                new ConnectionHandler().handle(socket);
            }
        } catch (IOException e) {
            logger.warning(e.getMessage()); //в случае ошибки выдаем warning в консоль.
        }
    }
```
В методе handle мы и будем организовывать общение сервера с клиентом.
Реализуем слушатель входящих пакетов из Socket при помощи класса Scanner.
И попробуем прослушать то что отправляет нам браузер.
+ также определим Logger для класса ConnectionHandler.
+ нужно также обработать ошибку на случай если соединение было закрыто.
```java
public class ConnectionHandler {

    private Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    public void handle(Socket socket) {
        try {
            /*
             *  Определяем переменную типа Scanner и передаем в его конструктор InputStream.
             *  Который получаем из Socket
             *
             *  Подробнее про InputStream:
             *  InputStream - это класс в package java.io,
             *  который является базовым классом,
             *  представляющим поток bytes (stream of bytes),
             *  полученный при чтении определенного источника данных,
             *  например файла.
             */
            Scanner scanner = new Scanner(socket.getInputStream());

            /*
             * Теперь пробуем в цикле слушать поток данных при помощи Scanner
             * и выводить их в логи.
             */
            while (scanner.hasNext()) {
                /*
                 * Будем использовать System.out.println для того чтобы заголовки вывелись одним сообщением.
                 */
                System.out.println(scanner.nextLine());
            }
        } catch (IOException e) {
            logger.warning(e.getMessage());
        }
    }

}
```
Теперь пробуем запустить сервак и посмотреть заголовки HTTP протокола,
которые нам отправляет браузер.
Теперь мы можем увидеть что отправляет нам браузер в момент подключения.
```
февр. 17, 2022 5:15:51 PM com.server.Server start
INFO: Server started.
февр. 17, 2022 5:15:51 PM com.server.Server start
INFO: Client /127.0.0.1 connected to the server.
GET / HTTP/1.1
Host: localhost
Connection: keep-alive
Cache-Control: max-age=0
sec-ch-ua: " Not A;Brand";v="99", "Chromium";v="98", "Google Chrome";v="98"
sec-ch-ua-mobile: ?0
sec-ch-ua-platform: "Windows"
Upgrade-Insecure-Requests: 1
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.82 Safari/537.36
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9
Sec-Fetch-Site: none
Sec-Fetch-Mode: navigate
Sec-Fetch-User: ?1
Sec-Fetch-Dest: document
Accept-Encoding: gzip, deflate, br
Accept-Language: ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7
Cookie: Idea-262bfa11=bc66bd36-f4df-4f5a-afb8-6abf4300400e
```
Попробуем ответить браузеру чем-то чтобы он вывел это на страницу.
Нужно убрать цикл ```while (scanner.hasNext())``` так как 
функция ```scanner.hasNext()``` является останавливающей
и она всегда будет останавливать поток пока не увидит поток данных.
Таким образом этот цикл никогда не закончится.
```java
    public void handle(Socket socket) {
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
            //Отправляем сообщение браузеру.
            printWriter.println("Да здравствует мир");
            //И закрываем соединение с клиентом чтобы браузер мог закончить чтение.
            socket.close();
        } catch (IOException e) {
            logger.warning(e.getMessage());
        }
    }
```
Теперь попробуем опять подключится через браузер.
Запускаем сервер и смотрим.
![](https://github.com/Lairon1/APIServer/blob/images/Screenshot_10.png?raw=true)
Почему-то у нас сбилась кодировка в браузере.
Это происходит потому что по протоколу HTTP мы должны с начало отправить
заголовок с указанной кодировкой чтобы браузер знал как ему читать.
Реализуем метод отправки заголовков.
Подробнее про заголовки можно прочитать тут. https://code.tutsplus.com/ru/tutorials/http-headers-for-dummies--net-8039
```java
    private void sendHeaders(PrintWriter printWriter){
        //Отправляем заголовки и пустую строчку после них по правилам протокола HTTP
        printWriter.println("HTTP/1.x 200 OK");
        printWriter.println("Content-Type: text/html; charset=UTF-8");
        printWriter.println("");
        }
```
Также добавляем вызов метода перед отправкой текста в браузер.
И пробуем еще раз подключится.

![](https://github.com/Lairon1/APIServer/blob/images/Screenshot_11.png?raw=true)
Теперь у нас вполне понятный текст.
### Добавление много поточности
Так как наш сервер выполняет все в главном потоке 
он не может обрабатывать более одного пользователя за раз.
Для реализации этого используют многопоточность.

Для начало воспользуемся CachedThreadPool для запуска и пере использования потоков.
Благо в java уже идет данный клас и мы можем им просто пользоваться.

Определяем его в классе Server
```java
private ExecutorService threadPool = Executors.newCachedThreadPool();
```
Теперь нужно нашим классом ConnectionHandler реализовать интерфейс Runnable.
Чтобы этот класс можно было запустить в потоке.
И переопределяем метод run, который вызовется при выполнении потока.
И нужно перенести всю логику метода handle в метод run.
Но так как мы переопределяем метод run мы не можем в него передать Socket
как это у нас было с методом handle.
Тогда на помощь к нам придет конструктор в который мы и передадим Socket.
```java
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
```
Теперь нам нужно переписать вызов обработчика в классе Server,
так как мы убрали метод handle и сделали из класса исполняемый поток.
```java
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
```
Теперь наш сервер может обрабатывать пользователей в много потоков одновременно.
### Заключение
Я рассказал о создание простенького сервера на Java с использованием ServerSocket.
Но это в общем то малая часть того что может ServerSocket.
Тут описаны лишь базовые методы.