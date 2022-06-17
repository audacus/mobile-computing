package ch.hftm.mobilecomputing;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;
import java.io.PrintStream;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Locale;

public class NetworkActivity extends AppCompatActivity {

    private static final int PORT = 8090;

    private TextView textViewPort;
    private TextView textViewMessages;

    private final StringBuilder message = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);

        var ipAddresses = new ArrayList<String>();
        try {

            // read out all local addresses
            var nics = NetworkInterface.getNetworkInterfaces();
            while (nics.hasMoreElements()) {
                var nic = nics.nextElement();
                var addresses = nic.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    var address = addresses.nextElement();
                    if (address.isSiteLocalAddress()) {
                        ipAddresses.add(address.getHostAddress());
                    }
                }
            }

        } catch (SocketException e) {
            e.printStackTrace();
        }

        var ipAddressesText = new StringBuilder();
        ipAddressesText.append("local addresses:\n\n");

        for (var ipAddress : ipAddresses) {
            ipAddressesText.append(ipAddress).append("\n");
        }

        ((TextView) findViewById(R.id.textViewAddresses)).setText(ipAddressesText.toString());

        this.textViewPort = findViewById(R.id.textViewPort);
        this.textViewMessages = findViewById(R.id.textViewMessages);

        new ServerSocketThread().start();
    }

    private class ServerSocketThread extends Thread {

        private ServerSocket serverSocket;

        private int counter = 0;

        @Override
        public void run() {
            try {
                this.serverSocket = new ServerSocket(PORT);
                runOnUiThread(() -> textViewPort.setText(String.format(Locale.ENGLISH, "waiting on port: %d", this.serverSocket.getLocalPort())));

                while (true) {
                    var socket = this.serverSocket.accept();
                    this.counter++;

                    message.append(String.format(Locale.ENGLISH, "connection #%d from IP %s  and port %d\n", this.counter, socket.getInetAddress(), socket.getPort()));

                    runOnUiThread(() -> textViewMessages.setText(message.toString()));

                    // reply
                    new ServerSocketReplyThread(socket, this.counter).run();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class ServerSocketReplyThread extends Thread {

        private final Socket hostThreadSocket;
        private final int counter;

        public ServerSocketReplyThread(Socket hostThreadSocket, int counter) {
            this.hostThreadSocket = hostThreadSocket;
            this.counter = counter;
        }

        @Override
        public void run() {
            try {
                var returnMessage = String.format(Locale.ENGLISH, "> Hello from your android device! Request #%d\n", this.counter);

                PrintStream printStream = new PrintStream(this.hostThreadSocket.getOutputStream());
                printStream.println(returnMessage);
                printStream.close();

                message.append(returnMessage);

                runOnUiThread(() -> textViewMessages.setText(message.toString()));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}