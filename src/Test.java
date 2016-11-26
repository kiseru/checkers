import com.checkers.CheckerBoard;
import com.checkers.Colour;
import com.checkers.User;

public class Test {
    public static void main(String[] args) {
        CheckerBoard.createBoard();
        User user = new User("Alex", Colour.WHITE);
        CheckerBoard.show();
        while (true) {
            user.makeTurn();
        }
    }
}
