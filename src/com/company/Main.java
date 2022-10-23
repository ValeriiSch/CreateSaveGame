package com.company;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        GameCreate newGame = new GameCreate();
        GameProgress game1 = new GameProgress(5, 10, 3, 15.5);
        GameProgress game2 = new GameProgress(7, 15, 5, 100.3);
        GameProgress game3 = new GameProgress(3, 8, 13, 117.75);

        // Создание начального каталога в текущей папке
        File dir = new File("Games");
        dir.mkdir();

        // установка игры
        newGame.makeNewDir("Games/src");
        newGame.makeNewDir("Games/res");
        newGame.makeNewDir("Games/savegames");
        newGame.makeNewDir("Games/temp");
        newGame.makeNewDir("Games/src/main");
        newGame.makeNewDir("Games/src/test");
        newGame.makeNewFile("Games/src/main/Main.java");
        newGame.makeNewFile("Games/src/main/Utils.java");
        newGame.makeNewFile("Games/temp/temp.txt");
        newGame.buildLog();

        // сохранение игры
        game1.saveGame("Games/savegames/save1.dat", game1);
        game2.saveGame("Games/savegames/save2.dat", game2);
        game3.saveGame("Games/savegames/save3.dat", game3);

        // запаковка в архив zip
        List<String> files = new ArrayList<>();
        files.add("Games/savegames/save1.dat");
        files.add("Games/savegames/save2.dat");
        files.add("Games/savegames/save3.dat");
        newGame.zipFiles("Games/savegames/zip.zip", files);

        // загрузка игры
        newGame.openZip("Games/savegames/zip.zip", "Games/savegames");
        game1 = newGame.openProgress("Games/savegames/save2.dat");
        System.out.println(game1);

    }
}
