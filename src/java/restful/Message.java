/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restful;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonObject;

/**
 *
 * @author Tyler
 */
// Michael help me with this
public class Message {

    private String contents;
    private int messageId;
    private String author;
    private String title;
    private Date sentTime;
    private SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

    public Message(JsonObject json) {
        messageId = json.getInt("messageId", 0);
        title = json.getString("title", "");
        contents = json.getString("contents", "");
        author = json.getString("author", "");
        String time = json.getString("sentTime", "");
        try {
            sentTime = sd.parse(time);
        } catch (ParseException ex) {
            sentTime = new Date();
            Logger.getLogger(Message.class.getName()).log(Level.SEVERE, null, "Failed Parsing Date: " + time);
        }
    }

    public Message() {

    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getSentTime() {
        return sentTime;
    }

    public void setSentTime(Date sentTime) {
        this.sentTime = sentTime;
    }

    public JsonObject toJSON() {
        String time = sd.format(sentTime);
        return Json.createObjectBuilder()
                .add("messageId", messageId)
                .add("title", title)
                .add("contents", contents)
                .add("author", author)
                .add("sentTime", time)
                .build();
    }

}
