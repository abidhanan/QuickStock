package com.quickstock.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BackupUtil {
    public static String createBackup() throws Exception {
        // Konfigurasi database
        String dbName = "quickstock"; // Ganti dengan nama database Anda
        String user = "root"; // Ganti dengan username database Anda
        String password = ""; // Ganti dengan password database Anda
        String backupFileName = "quickstock_backup_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".sql";
        
        // Tentukan lokasi file backup
        String backupFilePath = System.getProperty("user.dir") + File.separator + backupFileName;

        // Command untuk mysqldump
        String command = String.format("mysqldump -u %s -p%s %s > %s", user, password, dbName, backupFilePath);
        
        // Eksekusi command
        Process process = Runtime.getRuntime().exec(new String[]{"bash", "-c", command});
        process.waitFor(); // Tunggu hingga proses selesai
        
        // Cek apakah backup berhasil
        if (process.exitValue() != 0) {
            BufferedReader errorReader = new BufferedReader(
                new InputStreamReader(process.getErrorStream())
            );
            String error = errorReader.readLine();
            throw new Exception("Backup failed. Error: " + error);
        }
        
        return backupFilePath; // Kembalikan path file backup
    }
}
