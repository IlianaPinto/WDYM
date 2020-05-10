package whatdidyoumean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class WhatDidYouMean {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.print(":> ");
            String input = sc.nextLine();
            if (input.equalsIgnoreCase("exit")) {
                break;
            }
            tokenizer(input);

        }
    }

    public static void ls() {
        String directorio = "./";
        File f = new File(directorio);
        if (f.exists()) {
            File[] ficheros = f.listFiles();
            for (int i = 0; i < ficheros.length; i++) {
                System.out.println(ficheros[i].getName());
            }
        } else {
            System.out.println("No se encontró la dirección especificada");
        }
    }

    public static void ping(String ip) {
        InetAddress inet = null;
        try {
            inet = InetAddress.getByName(ip);
        } catch (UnknownHostException ex) {
            Logger.getLogger(WhatDidYouMean.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Sending Ping Request to " + ip);
        try {
            System.out.println(inet.isReachable(5000) ? "Host is reachable" : "Host is NOT reachable");
        } catch (IOException ex) {
            Logger.getLogger(WhatDidYouMean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void guardarZIP(String archivo, String nombreZip, String nombre) {
        File aZipear = new File(archivo);
        if (aZipear.exists()) {
            try {
                FileOutputStream fos = new FileOutputStream(nombreZip);
                ZipOutputStream zos = new ZipOutputStream(fos);

                ZipEntry entradaZIP = new ZipEntry(nombre + ".txt");
                zos.putNextEntry(entradaZIP);
                FileInputStream fis = new FileInputStream(archivo);

                byte[] buffer = new byte[1024];
                int leido = 0;
                while (0 < (leido = fis.read(buffer))) {
                    zos.write(buffer, 0, leido);
                }

                fis.close();
                zos.closeEntry();
                zos.close();
                fos.close();

                // System.out.println("Archivo "+nombre+".zip creado exitosamente!");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println(nombre + " does not exist");
        }
    }

    public static void tokenizer(String input) {
        Scanner sc = new Scanner(System.in);
        ArrayList<String> tokens = new ArrayList();

        //reconocimiento de tokens
        StringTokenizer token = new StringTokenizer(input);
        while (token.hasMoreTokens()) {
            tokens.add(token.nextToken());
        }

        //verificar input 
        switch (tokens.get(0)) {
            case "ls":
                if (tokens.size() == 1) {
                    ls();
                } else {
                    System.err.println("\"" + tokens.get(1) + "\" is not recognizable");
                }
                break;
            case "gzip":
                for (int i = 1; i < tokens.size(); i++) {
                    guardarZIP("./" + tokens.get(i), "./" + tokens.get(i) + ".zip", tokens.get(i));
                }
                if (tokens.size() == 1) {
                    System.err.println("Incomplete command gzip");
                }
                break;
            case "ping":
                switch (tokens.size()) {
                    case 2:
                        ping(tokens.get(1));
                        break;
                    case 1:
                        System.err.println("Incomplete command ping");
                        break;
                    default:
                        System.err.println("More arguments than expected");
                        break;
                }
                break;
            default:
                if (Pattern.matches("[ñ{ploikj,.][adewsqzxf]", input)) {//ls
                    System.out.println("Did you mean ls?[y/n]");
                    String resp = sc.next();
                    if (resp.equalsIgnoreCase("y")) {
                        ls();
                    } else {
                        System.out.println("Ok!");
                    }
                } else if (Pattern.matches("[gfdhj][xcz][iopuy][oi+p]", input)) {//gzip
                    System.out.println("Did you mean gzip?[y/n]");
                    String resp = sc.next();
                    if (resp.equalsIgnoreCase("y")) {
                        for (int i = 1; i < tokens.size(); i++) {
                            guardarZIP("./" + tokens.get(i), "./" + tokens.get(i) + ".zip", tokens.get(i));
                        }
                        if (tokens.size() == 1) {
                            System.err.println("Incomplete command gzip");
                        }
                    } else {
                        System.out.println("Ok!");
                    }
                } else if (Pattern.matches("[p+oi][iopuy][nm,bv][ghjfd]", input)) {//ping
                    System.out.println("Did you mean ping?[y/n]");
                    String resp = sc.next();
                    if (resp.equalsIgnoreCase("y")) {
                        switch (tokens.size()) {
                            case 1:
                                System.err.println("Incomplete command ping");
                                break;
                            case 2:
                                ping(tokens.get(1));
                                break;
                            default:
                                System.err.println("More arguments than expected");
                                break;
                        }

                    } else {
                        System.out.println("Ok!");
                    }
                } else {
                    System.err.println("Sorry, \"" + tokens.get(0) + "\" is not recognized 😕");
                }
                break;
        }
    }
}
