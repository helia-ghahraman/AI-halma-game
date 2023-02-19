
public class Tile {
    byte x, y;
    byte color = 0;     // 0 is empty 2 is red and 1 is blue
    byte zone = 0;      // 0 is none, 2 is red zone and 1 is blue zone

    public Tile(byte x, byte y) {
        this.x = x;
        this.y = y;
    }
}
