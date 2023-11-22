package org.example;

import java.util.Random;
import java.util.Scanner;

public class Program {

    private static final char DOT_HUMAN = 'X';
    private static final char DOT_AI = '0';
    private static final char DOT_EMPTY = '*';

    private static final int WIN_COUNT = 4;

    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();

    private static char[][] field;
    private static int fieldSizeX;
    private static int fieldSizeY;


    public static void main(String[] args) {
        fieldSizeY = 5;
        fieldSizeX = 5;
        while (true) {
            initialize();
            printField();
            while (true) {
                humanTurn(fieldSizeX, fieldSizeY);
                printField();
                if (checkGameState(DOT_HUMAN, "Вы победили!"))
                    break;
                cleverAiTurn();        // заменил метод на метод с "умным" компьютером
                printField();
                if (checkGameState(DOT_AI, "Победил компьютер!"))
                    break;
            }
            System.out.print("Желаете сыграть еще раз? (Y - да): ");
            if (!scanner.next().equalsIgnoreCase("Y"))
                break;
        }
    }

    /**
     * Инициализация игрового поля
     */
    static void initialize() {


        field = new char[fieldSizeY][fieldSizeX];
        for (int y = 0; y < fieldSizeY; y++) {
            for (int x = 0; x < fieldSizeX; x++) {
                field[y][x] = DOT_EMPTY;
            }
        }
    }

    /**
     * Печать текущего состояния игрового поля
     */
    private static void printField() {
        System.out.print("+");
        for (int i = 0; i < fieldSizeX; i++) {
            System.out.print("-" + (i + 1));
        }
        System.out.println("-");

        for (int y = 0; y < fieldSizeY; y++) {
            System.out.print(y + 1 + "|");
            for (int x = 0; x < fieldSizeX; x++) {
                System.out.print(field[y][x] + "|");
            }
            System.out.println();
        }

        for (int i = 0; i < fieldSizeX * 2 + 2; i++) {
            System.out.print("-");
        }
        System.out.println();
    }

    /**
     * Ход игрока (человека)
     */
    static void humanTurn(int fieldSizeX, int fieldSizeY) {
        int x;
        int y;

        do {
            System.out.printf("Введите координаты хода X (от 1 до %d) и Y от (1 до %d)\nчерез пробел: ",
                    fieldSizeX, fieldSizeY);
            x = scanner.nextInt() - 1;
            y = scanner.nextInt() - 1;
        }
        while (!isCellValid(x, y) || !isCellEmpty(x, y));

        field[y][x] = DOT_HUMAN;
    }

    /**
     * Ход игрока (компьютера)
     */
    static void aiTurn() {
        int x;
        int y;

        do {
            x = random.nextInt(fieldSizeX);
            y = random.nextInt(fieldSizeY);
        }
        while (!isCellEmpty(x, y));

        field[y][x] = DOT_AI;
    }

    /**
     * Проверка, является ли ячейка игрового поля пустой
     *
     * @param x
     * @param y
     * @return
     */
    static boolean isCellEmpty(int x, int y) {
        return field[y][x] == DOT_EMPTY;
    }

    /**
     * Проверка доступности ячейки игрового поля
     *
     * @param x
     * @param y
     * @return
     */
    static boolean isCellValid(int x, int y) {
        return x >= 0 && x < fieldSizeX && y >= 0 && y < fieldSizeY;
    }


    /**
     * Метод проверки состояния игры
     *
     * @param dot фишка игрока
     * @param s   победный слоган
     * @return результат проверки состояния игры
     */
    static boolean checkGameState(char dot, String s) {
        if (checkWin(dot, WIN_COUNT)) {
            System.out.println(s);
            return true;
        }
        if (checkDraw()) {
            System.out.println("Ничья!");
            return true;
        }
        return false; // Игра продолжается
    }

    /**
     * Проверка на ничью
     *
     * @return
     */
    static boolean checkDraw() {
        for (int y = 0; y < fieldSizeY; y++) {
            for (int x = 0; x < fieldSizeX; x++) {
                if (isCellEmpty(x, y))
                    return false;
            }
        }
        return true;
    }

    /**
     * Проверка победы игрока
     *
     * @param dot фишка игрока
     * @return признак победы
     */
    static boolean checkWin(char dot, int winCount) {
        boolean summary;
        for (int y = 0; y <= fieldSizeY - winCount; y++) {
            for (int x = 0; x <= fieldSizeX - winCount; x++) {
                summary = check3(x, y, dot, winCount); // диагональ вниз вправо
                if (summary) return true;
            }
        }
        for (int y = 0; y <= fieldSizeY - winCount; y++) {
            for (int x = 0; x < fieldSizeX; x++) {
                summary = check2(x, y, dot, winCount); // вертикаль вниз
                if (summary) return true;
            }
        }
        for (int y = 0; y < fieldSizeY; y++) {
            for (int x = 0; x <= fieldSizeX - winCount; x++) {
                summary = check1(x, y, dot, winCount); // горизонталь вправо
                if (summary) return true;
            }
        }
        for (int y = winCount - 1; y < fieldSizeY; y++) {
            for (int x = 0; x <= fieldSizeX - winCount; x++) {
                summary = check4(x, y, dot, winCount); // диагональ вправо вверх
                if (summary) return true;
            }
        }
        return false;
    }

    /**
     * @param x
     * @param y
     * @param dot      - фишка игрока
     * @param winCount - необходимое число подряд идущих фишек для победы
     * @return
     */
    static boolean check1(int x, int y, char dot, int winCount) {
        boolean summary = true;
        for (int temp = 0; temp < winCount; temp++) {
            summary = summary && field[x + temp][y] == dot;
        }
        return summary;
    }

    /**
     * Проверка по вертикали вниз на предмет ситуации "Победа"
     *
     * @param x
     * @param y
     * @param dot      - фишка игрока
     * @param winCount - необходимое число подряд идущих фишек для победы
     * @return
     */
    static boolean check2(int x, int y, char dot, int winCount) {
        boolean summary = true;
        for (int temp = 0; temp < winCount; temp++) {
            summary = summary && field[x][y + temp] == dot;
        }
        return summary;
    }


    /**
     * Проверка по диагонали вправо вниз на предмет ситуации "Победа"
     *
     * @param x
     * @param y
     * @param dot      - фишка игрока
     * @param winCount - необходимое число подряд идущих фишек для победы
     * @return
     */
    static boolean check3(int x, int y, char dot, int winCount) {
        boolean summary = true;
        for (int temp = 0; temp < winCount; temp++) {
            summary = summary && field[x + temp][y + temp] == dot;
        }
        return summary;
    }

    static boolean check4(int x, int y, char dot, int winCount) {
        boolean summary = true;
        for (int temp = 0; temp < winCount; temp++) {
            summary = summary && field[x + temp][y - temp] == dot;
        }
        return summary;
    }

    /**
     * ход умного компьютера, при котором он не дает поставить тетий крестик
     * но если человек все-таки ставит третий крестик, то компьютер почти 100% проиграл
     * Таким образом получен небольшой уровень сложности для человека
     */
    static void cleverAiTurn() {
        int x;
        int y;
        int count = 0; // счетчик умных ходов Ai за один ход
        for (y = 0; y < fieldSizeY; y++) {
            for (x = 0; x < fieldSizeX; x++) {
                if (isCellEmpty(x, y) && count == 0) {
                    field[y][x] = DOT_HUMAN;    // предполагаемый ход человека
                    if (checkWin(DOT_HUMAN, WIN_COUNT - 1)) {  // проверка трех крестиков подряд
                        field[y][x] = DOT_AI;
                        count = 1;
                        break;
                    } else {
                        field[y][x] = DOT_EMPTY; // если победы нет, снова обнуляем ячейку
                    }
                }
            }
        }
            if (count == 0) {
                do {
                    x = random.nextInt(fieldSizeX);
                    y = random.nextInt(fieldSizeY);
                }
                while (!isCellEmpty(x, y));
                field[y][x] = DOT_AI;
            }



    }

}
