import fi.iki.elonen.NanoHTTPD;

import java.io.*;
import java.util.List;

public class Server extends NanoHTTPD {

    RequestUrlMapper requestUrlMapper = new RequestUrlMapper();


    public Server(int port) throws IOException {
        super(port);
        start(5000, false);
        System.out.println("Server has been started");

    }

    public static void main(String[] args) {

        try {
            FileInputStream fileInputStream = new FileInputStream("server.txt");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            ListGameStorageImpl.gamesStorage = (List<Game>) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            System.err.println("Error: " + '\n' + e);
        } catch (IOException e) {
            System.err.println("Error: " + '\n' + e);
        } catch (ClassNotFoundException e) {
            System.err.println("Error: " + '\n' + e);
        }


        try {
            Server server = new Server(8080);
        } catch (IOException e) {
            System.err.println("Error: " + e);
        }

    }

    @Override
    public Response serve(IHTTPSession session) {
        return requestUrlMapper.delegateRequest(session);
    }
}
