package Client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.util.Objects;

class MessageReciever implements Runnable {
    static byte[] buffer; //Message buffer

    MessageReciever() {
        //Set up buffer
        buffer = new byte[1024];
    }

    public static String Recieve() {
        try {
            //Initalize packet
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            //Recieve data from network into packet
            GameClient.socket.receive(packet);
            //Convert pack data to string
            //Return recieved message
            return new String(packet.getData(), 0, packet.getLength());

        } catch (IOException ignored) {
        }

        return null;
    }

    private static boolean RespondToServer(String serverMessage) throws Exception {

        switch (serverMessage) {
            case "0":
                if (StaticData.GetCarIndex() != 0)
                    StaticData.SetCar(0);
                System.err.println("Allocated white car");
                StaticData.waitForRecieve = true;
                StaticData.GetForm();
                break;
            case "1":
                if (StaticData.GetCarIndex() != 1)
                    StaticData.SetCar(1);
                System.err.println("Allocated blue car");
                StaticData.GetForm();
                break;
            case "restart":
                Objects.requireNonNull(StaticData.GetForm()).Restart();
                break;
            case "exit":
                //Close Application. Server full(Already controling two cars)
                System.out.println("Server down");
                System.out.println("Server closing");
                Objects.requireNonNull(StaticData.GetForm()).dispose();
                System.exit(0);
                return false; //Stop running
            default:
                //Split server message
                String[] args = serverMessage.split(" ");
                //Use array created as details for new car details
                if (args.length == 5)
                    Objects.requireNonNull(StaticData.GetForm()).MakeChanges(args);
                break;
        }

        return true;  //Continue running 
    }

    public static boolean RecieveAndRespond() throws Exception {
        //Recieve message from server
        String serverMessage = Recieve();
        //Display server message
        assert serverMessage != null;
        if (!serverMessage.isEmpty())
            System.out.println("Server: " + serverMessage);
        //return continue value
        return RespondToServer(serverMessage);
    }

    @Override
    public void run() {

        try {
            //Recieve messages from server and respond indefinitly
            while (RecieveAndRespond()) {
            }
        } catch (Exception ex) {
            //Display error message
            System.err.println("Messages can no longer recieve messages");
        }
    }
}