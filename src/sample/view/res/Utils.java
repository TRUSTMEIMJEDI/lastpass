package sample.view.res;

import java.io.*;
import java.util.ArrayList;


public class Utils {
    private final static String CHARSET = "UTF-8";


    static ArrayList<String> getFileInFolder(String chemin, String extension) {
        return getFileInFolder(new File(chemin), extension);
    }
    private static ArrayList<String> getFileInFolder(File dossier, String extension) {
        ArrayList<String> fichiers = new ArrayList<>();

        File[] files;
        if (dossier.exists() && dossier.isDirectory() && (files = dossier.listFiles((dir, name) -> name.endsWith(extension))) != null)
            for (File f : files)
                fichiers.add(f.getAbsolutePath());

        return fichiers;
    }

    public static String toLocalPath(String path) {
        String localPath = new File(".").getAbsolutePath();
        localPath = localPath.substring(0 , localPath.length() - 1);

        String toRet = path
                .replace(localPath, "." + File.separator)
                .replace("/", File.separator)
                .replace("." + File.separator, "");

        return toRet;
    }
}