package main;

import gameLogic.Play;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int x, y;
        System.out.println();
        System.out.println("Dobrodosli u Sanke");
        System.out.println("Unesite sirinu i visinu vase ploce!");
        try (Scanner scanner = new Scanner(System.in)) {
            x = scanner.nextInt();
            y = scanner.nextInt();
            Play snake = new Play(x, y);
            snake.start();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}
