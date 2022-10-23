package com.company;

import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class GameCreate {
    static StringBuilder log = new StringBuilder();

    public void makeNewDir(String pathname) {
        File dir = new File(pathname);
        Date date = new Date();
// пробуем создать каталог
        if (dir.mkdir())
            log.append(date + " каталог " + dir.getName() + " успешно создан в директории " + dir.getParent() + '\n');
        else
            log.append(date + " каталог " + dir.getName() + " НЕ создан в директории " + dir.getParent() + '\n');
    }

    public void makeNewFile(String pathname) {
        File file = new File(pathname);
        Date date = new Date();

        // создадим новый файл
        try {
            if (file.createNewFile())
                log.append(date + " файл " + file.getName() + " успешно создан в директории " + file.getParent() + '\n');
        } catch (IOException ex) {
            log.append(date + " файл " + file.getName() + " НЕ создан в директории " + file.getParent() + ". " +
                    ex.getMessage() + '\n');
        }
    }

    public void buildLog() {
        try (FileWriter writer = new FileWriter("Games//temp/temp.txt", false)) {
            // запись всей строки
            writer.write(String.valueOf(log));
            // дозаписываем и очищаем буфер
            writer.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void zipFiles(String pathnameZip, List<String> zipFiles) {
        try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(pathnameZip))) {
            for (String item : zipFiles) {
                try (FileInputStream fis = new FileInputStream(item)) {
                    ZipEntry entry = new ZipEntry(item);
                    zout.putNextEntry(entry);
                    // считываем содержимое файла в массив byte
                    byte[] buffer = new byte[fis.available()];
                    fis.read(buffer);
                    // добавляем содержимое к архиву
                    zout.write(buffer);
                    // закрываем текущую запись для новой записи
                    zout.closeEntry();
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
                // удаление файла
                delFile(item);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void delFile(String pathname) {
        File file = new File(pathname);
        file.delete();
    }


    public void openZip(String pathnameZip, String pathname) {
        try (ZipInputStream zin = new ZipInputStream(new FileInputStream(pathnameZip))) {
            ZipEntry entry;
            String name;
            while ((entry = zin.getNextEntry()) != null) {
                name = entry.getName(); // получим название файла
                // распаковка
                FileOutputStream fout = new FileOutputStream(name);
                for (int c = zin.read(); c != -1; c = zin.read()) {
                    fout.write(c);
                }
                fout.flush();
                zin.closeEntry();
                fout.close();
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public GameProgress openProgress(String pathSaveGame){
        GameProgress gameProgress = null;
// откроем входной поток для чтения файла
        try (FileInputStream fis = new FileInputStream(pathSaveGame);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            // десериализуем объект и скастим его в класс
            gameProgress = (GameProgress) ois.readObject();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return gameProgress;
    }

}