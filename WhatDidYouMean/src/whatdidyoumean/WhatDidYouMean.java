package whatdidyoumean;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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

    ArrayList<String> comandos = new ArrayList();

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
            //Logger.getLogger(WhatDidYouMean.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Sending Ping Request to " + ip);
        try {
            System.out.println(inet.isReachable(5000) ? "Host is reachable" : "Host is NOT reachable");
        } catch (IOException ex) {
            //Logger.getLogger(WhatDidYouMean.class.getName()).log(Level.SEVERE, null, ex);
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

    public static void guardarArchivo(String comando) {
        FileWriter fichero = null;
        PrintWriter pw = null;
        try {
            fichero = new FileWriter("./comandos.txt", true);
            pw = new PrintWriter(fichero);
            pw.println(comando);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != fichero) {
                    fichero.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public static ArrayList<String> leerArchivo() {
        ArrayList<String> comandos = new ArrayList();
        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;

        try {
            archivo = new File("./comandos.txt");
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);

            String linea;
            while ((linea = br.readLine()) != null) {
                comandos.add(linea);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != fr) {
                    fr.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return comandos;
    }

    public static boolean verificar(String token) {
        ArrayList<String> comandos = new ArrayList();
        comandos = leerArchivo();
        boolean ver = false;
        for (int i = 0; i < comandos.size(); i++) {
            if (comandos.get(i).equals(token)) {
                ver = true;
            }
        }
        return ver;
    }

    public static void correrGzip(ArrayList<String> tokens) {
        for (int i = 1; i < tokens.size(); i++) {
            guardarZIP("./" + tokens.get(i), "./" + tokens.get(i) + ".zip", tokens.get(i));
        }
        if (tokens.size() == 1) {
            System.err.println("Incomplete command gzip");
        }
        if (!verificar(tokens.get(0))) {
            guardarArchivo(tokens.get(0));
        }
    }

    public static void correrPing(ArrayList<String> tokens) {
        switch (tokens.size()) {
            case 1:
                System.err.println("Incomplete command ping");
                if (!verificar(tokens.get(0))) {
                    guardarArchivo(tokens.get(0));
                }
                break;
            case 2:
                ping(tokens.get(1));
                if (!verificar(tokens.get(0))) {
                    guardarArchivo(tokens.get(0));
                }
                break;
            default:
                System.err.println("More arguments than expected");
                break;
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
        if (Pattern.matches("[{kjl][asdf]", tokens.get(0))) {//ls
            if (verificar(tokens.get(0))) {//verifica el comando ingresado en un archivo
                ls();
            } else {
                System.out.println("Did you mean ls?[y/n]");
                String resp = sc.next();
                if (resp.equalsIgnoreCase("y")) {
                    ls();
                    if (!verificar(tokens.get(0))) {
                        guardarArchivo(tokens.get(0));
                    }
                }
            }
            System.out.println("");
        } else if (Pattern.matches("[gfdhj][xcz][iopuy][oi+p]", tokens.get(0))) {//gzip
            if (verificar(tokens.get(0))) {//verificar el comando ingresado
                correrGzip(tokens);
            } else {
                System.out.println("Did you mean gzip?[y/n]");
                String resp = sc.next();
                if (resp.equalsIgnoreCase("y")) {
                    correrGzip(tokens);
                }
            }
            System.out.println("");
        } else if (Pattern.matches("[p+oi][iopuy][nm,bv][ghjfd]", tokens.get(0))) {//ping
            if (verificar(tokens.get(0))) {//verifica el comando ingresado
                correrPing(tokens);
            } else {
                System.out.println("Did you mean ping?[y/n]");
                String resp = sc.next();
                if (resp.equalsIgnoreCase("y")) {
                    correrPing(tokens);
                }
            }
            System.out.println("");
        } else {
            System.err.println("Sorry, \"" + tokens.get(0) + "\" is not recognized 😕");

        }

    }
}
