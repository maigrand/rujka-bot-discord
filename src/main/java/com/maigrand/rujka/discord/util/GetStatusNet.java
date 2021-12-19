package com.maigrand.rujka.discord.util;

import java.io.IOException;
import java.net.*;

/**
 * @author Noman23
 */
public class GetStatusNet {

    public static String getStatusNet(String host, int port) {
        byte[] message = ("    getstatus").getBytes();
        message[0] = (byte) 255;
        message[1] = (byte) 255;
        message[2] = (byte) 255;
        message[3] = (byte) 255;

        // Get the internet address of the specified host
        InetAddress address = null;
        try {
            address = InetAddress.getByName(host);

            // Initialize a datagram packet with data and address
            DatagramPacket packet = new DatagramPacket(message, message.length, address, port);

            // Create a datagram socket, send the packet through it, close it.
            DatagramSocket dsocket = new DatagramSocket();
            dsocket.setSoTimeout(10000);
            dsocket.send(packet);
            byte[] buf = new byte[3096];
            DatagramPacket datagramPacket = new DatagramPacket(buf, 3096);
            dsocket.receive(datagramPacket);
            String response = new String(datagramPacket.getData());
            dsocket.close();
            return response;
        } catch (SocketTimeoutException e) {
            return "offline";
        } catch (IOException e) {
            e.printStackTrace();
            return "offline";
        }
    }
}
