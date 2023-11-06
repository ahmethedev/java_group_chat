import java.io.*;
import java.net.Socket;
import java.util.ArrayList;


public class ClientHandler implements  Runnable{
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>(); // allow multiple client to send messages
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUsername;

    public ClientHandler(Socket socket){
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())); // we want to send charecters not bytes. bufferedwriter increases quality
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientUsername = bufferedReader.readLine();
            clientHandlers.add(this);
            broadcastMessage("SERVER:" + clientUsername + "has entered the chat!");
        }
        catch (IOException e){
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }
    @Override
    public void run() {
        String messageFromClient;
        while
        (socket.isConnected()){
            try{
                messageFromClient = bufferedReader.readLine();
                broadcastMessage(messageFromClient);

            }catch (IOException e){
                closeEverything(socket, bufferedReader, bufferedWriter);
                break;
            }

        }

    }

    public void broadcastMessage(String messageToSend){
        for (ClientHandler clientHandler : clientHandlers){
            try {
                if (!clientHandler.clientUsername.equals(clientUsername)){
                    clientHandler.bufferedWriter.write(messageToSend);
                    clientHandler.bufferedWriter.newLine(); // Im done with sending data so no need to wait for new line.
                    clientHandler.bufferedWriter.flush();
                }
            }catch (IOException e){
                closeEverything(socket, bufferedReader,bufferedWriter);
            }
        }
    }
    public void removeClientHandler(){
        clientHandlers.remove(this); //user left the chat
        broadcastMessage("SERVER:" + clientUsername + "has left the chat!");
    }

    public void closeEverything(Socket socket,BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        removeClientHandler();
        try{
            if (bufferedReader!= null){
                bufferedReader.close();
            }
            if (bufferedWriter != null){
                bufferedWriter.close();
            }
            if (socket != null){
                socket.close();
            }

        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
