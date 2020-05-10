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
            System.out.print(">: ");
            String input = sc.nextLine();
            if (input.equalsIgnoreCase("exit")) {
                break;
            }
            tokenizer(input);
        }

        //guardarZIP("C:/Users/Jetstereo/OneDrive/Desktop/Prueba.txt", "C:/Users/Jetstereo/OneDrive/Desktop/comprimido.zip");
    }

    public static void ls() {
        String directorio = "c:\\";
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

    public static void guardarZIP(String archivo, String nombreZip) {
        File aZipear = new File(archivo);
        if (aZipear.exists()) {
            try {
                FileOutputStream fos = new FileOutputStream(nombreZip);
                ZipOutputStream zos = new ZipOutputStream(fos);

                ZipEntry entradaZIP = new ZipEntry("subcarpeta/texto.txt");
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

                System.out.println("Archivo zip creado exitosamente!");

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("El archivo a comprimir no existe!");
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
                    System.out.println("\"" + tokens.get(1) + "\" is not recognizable");
                }
                break;
            case "gzip":
                for (int i = 1; i < tokens.size(); i++) {
                    guardarZIP("C:/Users/Jetstereo/OneDrive/Desktop/"+tokens.get(i), "C:/Users/Jetstereo/OneDrive/Desktop/"+tokens.get(i)+".zip");
                }
                break;
            case "ping":
                ping(tokens.get(1));
                break;
            default:
                String regex;
                switch (tokens.get(0).charAt(0)) {
                    case 'l':
                        regex = "[ñ{ploikj,.][adewsqzxf]";
                        if (Pattern.matches(regex, input)) {
                            System.out.println("Did you mean ls?[y/n]");
                            String resp = sc.next();
                            if (resp.equalsIgnoreCase("y")) {
                                ls();
                            } else {
                                System.out.println("Ok!");
                            }
                        } else {
                            System.err.println("Sorry, \"" + tokens.get(0) + "\" is not recognized 😕");
                        }
                        break;
                    case 'g':
                        regex = "[gfdhj][xcz][iopuy][oi´+p]";
                        if (Pattern.matches(regex, input)) {
                            System.out.println("Did you mean gzip?[y/n]");
                            String resp = sc.next();
                            if (resp.equalsIgnoreCase("y")) {
                                ls();
                            } else {
                                System.out.println("Ok!");
                            }
                        } else {
                            System.err.println("Sorry, \"" + tokens.get(0) + "\" is not recognized 😕");
                        }
                        break;
                    case 'p':
                        regex = "[p+oi][iopuy][nm,bv][ghjfd]";
                        if (Pattern.matches(regex, input)) {
                            System.out.println("Did you mean ping?[y/n]");
                            String resp = sc.next();
                            if (resp.equalsIgnoreCase("y")) {
                                ls();
                            } else {
                                System.out.println("Ok!");
                            }
                        } else {
                            System.err.println("Sorry, \"" + tokens.get(0) + "\" is not recognized 😕");
                        }
                        break;
                    default:
                        System.err.println("Sorry, \"" + tokens.get(0) + "\" is not recognized 😕");
                        break;
                }
                break;
        }
        
    }
}
