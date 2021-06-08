
package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddClientRequest extends Thread {

    Socket socket;
    DataInputStream dataInputStream = null;
    DataOutputStream dataOutputStream = null;
    FileOutputStream fileOutputStream = null;

    static int index;
    String file_name;

    public AddClientRequest(Socket socket) {
        this.socket = socket;
        index++;
    }

    @Override
    public void run() {
        int var;

        try {
            dataInputStream = new DataInputStream(socket.getInputStream());//Возвращает входной поток для этого сокета.
            dataOutputStream = new DataOutputStream(socket.getOutputStream());

            file_name = dataInputStream.readUTF();

            file_name = index + "_" + file_name;

            fileOutputStream = new FileOutputStream(ServerFrameForm.dir_path + file_name);

            while ((var = dataInputStream.read()) != -1)//пока не достигнут конец файла
            {
                fileOutputStream.write(var);
            }
            fileOutputStream.close();

            ServerFrameForm.linkedList.add(file_name);

            while (!ServerFrameForm.linkedList.isEmpty()) {
                String printFile = ServerFrameForm.linkedList.remove();
                DoPrint print = new DoPrint(ServerFrameForm.dir_path + printFile);
                print.start();
            }
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(AddClientRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
