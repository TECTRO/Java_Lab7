package com.tectro;

import java.awt.image.LookupOp;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.server.ExportException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class Main {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        try {
            Random rand = new Random();
            ServerSocket server = new ServerSocket(25565, 0, InetAddress.getByName("localhost"));
            System.out.println("Server started");
            while (true) {
                try {


                    Socket client = server.accept();

                    ObjectInputStream ois = new ObjectInputStream(client.getInputStream());
                    ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());

                    ArrayList<Integer> MainList = GenerateMass(rand);
                    ArrayList<Integer> OtherList = new ArrayList<>();

                    oos.writeObject(MainList);

                    //noinspection InfiniteLoopStatement
                    while (true) {
                        int TaskNum = ois.readInt();

                        switch (TaskNum) {
                            case 1: {
                                int otherEnd = rand.nextInt(MainList.size()-1)+1;
                                int otherStart = rand.nextInt(otherEnd);
                                OtherList = new ArrayList<>();
                                if(otherStart<otherEnd)
                                for (int i = otherStart; i < otherEnd; i++)
                                    OtherList.add(MainList.get(i));
                                oos.writeObject(OtherList);
                                oos.flush();
                            }
                            break;
                            case 2: {
                                OtherList.sort(Integer::compareTo);
                                oos.writeObject(new ArrayList<Integer>(OtherList));
                                oos.flush();
                            }
                            break;
                            case 3: {
                                Stack<String> strings = (Stack<String>) ois.readObject();
                                Stack<String> result = new Stack<>();
                                while (!strings.empty())
                                    result.push(strings.pop());

                                oos.writeObject(result);
                                oos.flush();
                            }
                            break;
                        }
                    }
                } catch (Exception exc) {
                    System.out.println(exc.getMessage());
                }
                //dos.writeDouble(CalculateFunction());
            }
        } catch (Exception exc) {
            System.out.println(exc.getMessage());
        }
    }

    private static ArrayList<Integer> GenerateMass(Random rand) {
        ArrayList<Integer> result = new ArrayList<>();
        int mainListLen = rand.nextInt(15) + 5;
        for (int i = 0; i < mainListLen; i++)
            result.add(rand.nextInt(200) - 100);
        return result;
    }
}
