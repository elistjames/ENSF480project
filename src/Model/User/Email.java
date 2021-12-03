package Model.User;

public class Email {
    private int fromId;
    private int toId;
    private String message;

    public Email(int fromId, int toId, String message) {
        this.fromId = fromId;
        this.toId = toId;
        this.message = message;
    }

    public int getToID(){return toId;}
    public int getFromID(){return fromId;}
    public String getMessage(){return message;}
}
