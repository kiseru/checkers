abstract public class Piece {
    private int x; // буква - координата доски
    private int y; // цифра - координата доски
    private String colour; // цвет фигуры

    abstract void moveRight(); // ход вправо по диагонали

    abstract void moveLeft(); // ход влево по диагонали

    abstract void eatRight(); // поедание вправо по диагонали

    abstract void eatLeft(); // поедание влево по диагонали
}