package androiderob.com.superthread;

public class Item {

    private int id;
    private int x;
    private int y;

    public Item() {
    }

    public int getId() {
        return id;
    }

    public Item setId(int id) {
        this.id = id;
        return this;
    }

    public int getX() {
        return x;
    }

    public Item setX(int x) {
        this.x = x;
        return this;
    }

    public int getY() {
        return y;
    }

    public Item setY(int y) {
        this.y = y;
        return this;
    }

    public void heavyOperation() {
        for (int i = 0; i < 100; i++) {
            this.y++;
            this.x++;
        }
    }

    public String toString() {
        return "ID: " + String.valueOf(id) + " X: " + String.valueOf(x) + " Y: " + String.valueOf(y);
    }
}
