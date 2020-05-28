import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message implements Serializable {

    private Client receiver;
    private Client sender;
    private String text;
    private String time = getCurrentTime();


    public Message(Client receiver, Client sender, String text) {
        this.receiver = receiver;
        this.sender = sender;
        this.text = text;
//        time = getCurrentTime();
    }

    public Message(String text) {
        this.text = text;
    }

    public Message(Client sender, String msg) {
        this.sender = sender;
        this.text = msg;

    }

    private String getCurrentTime() {
        String currTime;
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a zzz");
        currTime = sdf.format(date);
        return currTime;
    }

    public Client getReceiver() {
        return receiver;
    }

    public Client getSender() {
        return sender;
    }

    public String getText() {
        return text;
    }


    public void setReceiver(Client receiver) {
        this.receiver = receiver;
    }

    public void setSender(Client sender) {
        this.sender = sender;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public java.lang.String toString() {

        if (sender == null)
            return text;

        return String.format("[%s] %s: %s", time, sender, text);

    }

}
