package com.maigrand.rujka.discord.rconmodule.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.*;

/**
 * @author Noman23
 */
public class RconUtil {

    private final DatagramSocket serverSocket;

    private final InetAddress inetAddress;
    private final int port;
    private final String rconPassword;

    public RconUtil(String host, int port, String rconPassword) throws SocketException, UnknownHostException {
        this.serverSocket = new DatagramSocket();

        this.inetAddress = InetAddress.getByName(host);
        this.port = port;
        this.rconPassword = rconPassword;
    }

    public void commandAsnc(String command, Callback callback) {
        new Thread(() -> {

            String response = command(command);

            if (callback != null) {
                callback.call(response);
            }
        }).start();
    }

    public String command(String command) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            byteArrayOutputStream.write(new byte[]{(byte) 255, (byte) 255, (byte) 255, (byte) 255});
            byteArrayOutputStream.write("rcon ".getBytes());
            byteArrayOutputStream.write((rconPassword + " ").getBytes());
            byteArrayOutputStream.write(command.getBytes());
        } catch (IOException e) {
            System.err.println("packet generate error");
            e.printStackTrace();
        }

        DatagramPacket packet = new DatagramPacket(byteArrayOutputStream.toByteArray(), byteArrayOutputStream.toByteArray().length, inetAddress, port);

        byte[] receiveData = new byte[4096];
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

        try {
            serverSocket.send(packet);
            serverSocket.receive(receivePacket);
        } catch (IOException e) {
            System.err.println("net error send/receive");
            e.printStackTrace();
        }

        return new String(receiveData);
    }

    public interface Callback {

        void call(String response);
    }

    public String getNormalizedResponse(String response) {
        String out = "";
        for (String s : response.split("\n")) {
            if (s.trim().isEmpty() || s.trim().isBlank()) {
                continue;
            }
            out += s.trim() + "\n";
        }
        return out
                .replaceAll("�", "")
                .replaceAll("\\^\\d", "")
                .replaceAll("ÿÿÿÿ", "");
    }
}
