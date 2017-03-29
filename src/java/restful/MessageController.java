/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restful;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;

/**
 *
 * @author Tyler
 */
// Michael help me with this
@ApplicationScoped
public class MessageController {

    private SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
    List<Message> message;

    public MessageController() {
        message = new ArrayList<>();
    }

    public JsonArray getallJson() {
        JsonArrayBuilder json = Json.createArrayBuilder();
        for (Message m : message) {
            json.add(m.toJSON());
        }
        return json.build();
    }

    public Message getById(int id) {
        try {
            Connection conn = DBUtils.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Message");
            while (rs.next()) {
                Message m = new Message();
                m.setAuthor(rs.getString("author"));
                m.setMessageId(rs.getInt("messageId"));
                m.setContents(rs.getString("content"));
                m.setTitle(rs.getString("title"));
                m.setSentTime(rs.getDate("sentTime"));
                return m;
            }

        } catch (SQLException ex) {
            Logger.getLogger(MessageController.class.getName()).log(Level.SEVERE, null, ex);
        }

        /*for (Message m : message) {
            if (m.getMessageId() == id) {
                return m;
            }
        }*/
        return null;
    }

    public JsonObject getByIdJson(int id) {
        Message m = getById(id);
        if (m != null) {
            return getById(id).toJSON();
        } else {
            return null;
        }
    }

    public JsonArray getByDateJson(Date from, Date to) {
        JsonArrayBuilder json = Json.createArrayBuilder();
        for (Message m : message) {
            if ((m.getSentTime().after(from) && m.getSentTime().before(to))
                    || m.getSentTime().equals(from) || m.getSentTime().equals(to)) {
                json.add(m.toJSON());
            }
        }
        return json.build();
    }

        private void getDBUtils() {
       
        try {
            Connection conn = DBUtils.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Message");
            while (rs.next()) {
                Message m = new Message();
                m.setAuthor(rs.getString("author"));
                m.setMessageId(rs.getInt("messageId"));
                m.setContents(rs.getString("content"));
                m.setTitle(rs.getString("title"));
                m.setSentTime(rs.getDate("sentTime"));
            
            }

        } catch (SQLException ex) {
            Logger.getLogger(MessageController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String add() {
        Message m = new Message();
        try {
            Connection conn = DBUtils.getConnection();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("INSERT INTO products VALUES (" + m.getMessageId() + ",'"  + m.getTitle() + ",'" + m.getContents() + "','" + m.getAuthor() +  "','" + m.getSentTime() +"')");
            getDBUtils();
            return "index";
        } catch (SQLException ex) {
            Logger.getLogger(MessageController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "index";
    }
    public boolean deleteById(int id) {
        Message m = getById(id);
        if (m != null) {
            message.remove(m);
            return true;
        } else {
            return false;
        }
    }

    public JsonObject edit(int id, JsonObject json) {
        Message m = getById(id);
        m.setTitle(json.getString("title", ""));
        m.setContents(json.getString("contents", ""));
        m.setAuthor(json.getString("author", ""));
        String time = json.getString("sentTime", "");
        try {
            m.setSentTime(sd.parse(time));
        } catch (ParseException ex) {
            m.setSentTime(new Date());
            Logger.getLogger(Message.class.getName()).log(Level.SEVERE, null, "Failed Parsing Date: " + time);
        }
        return m.toJSON();
    }

}
