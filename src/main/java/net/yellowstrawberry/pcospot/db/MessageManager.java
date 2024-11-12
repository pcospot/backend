package net.yellowstrawberry.pcospot.db;

import com.github.f4b6a3.tsid.TsidCreator;
import net.yellowstrawberry.pcospot.db.repository.ServerRepository;
import net.yellowstrawberry.pcospot.object.server.Server;
import net.yellowstrawberry.pcospot.utils.SQLCommunicator;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Service
public class MessageManager {

    private static ServerRepository serverRepository;

    public MessageManager(ServerRepository serverRepository) {
        MessageManager.serverRepository = serverRepository;
    }

    private static boolean check(long server) {
        Optional<Server> so = serverRepository.findById(server);
        return so.map(value -> value.getEnd() < System.currentTimeMillis()).orElse(true);
    }

    public static boolean message(long server, long channel, long author, long reply, String content) {
        if(check(server)) return false;
        serverRepository.findById(server).ifPresent((s) -> s.setLastUpdate(System.currentTimeMillis()));

        // TODO: Log
        return SQLCommunicator.executeUpdate(
                "INSERT INTO `message` (`id`, `server`, `channel`, `author`, `content`, `reply`) VALUES (?,?,?,?,?,?)",
                TsidCreator.getTsid().toLong(), server, channel, author, content, reply
        ) > 0;
    }

    public static boolean edit(long server, long channel, long messageId, long requestee, String content) {
        if(check(server)) return false;
        serverRepository.findById(server).ifPresent((s) -> s.setLastUpdate(System.currentTimeMillis()));

        // TODO: Log
        return SQLCommunicator.executeUpdate(
                "UPDATE `message` SET `content`=? WHERE `id`=? AND `channel`=? AND `author`=?",
                content, messageId, channel, requestee
        ) > 0;
    }

    public static boolean delete(long server, long channel, long messageId, long requestee) {
        if(check(server)) return false;
        serverRepository.findById(server).ifPresent((s) -> s.setLastUpdate(System.currentTimeMillis()));

        // TODO: Log
        return SQLCommunicator.executeUpdate(
                "DELETE FROM `message` WHERE `channel`=? AND `id`=?",
                channel, messageId
        ) > 0;
    }

    public static JSONArray loadMessages(long server, long channel, long before, long after, int max) {
        if(serverRepository.findById(server).isEmpty()) return null;

        JSONArray messages = new JSONArray();

        try(ResultSet set = SQLCommunicator.executeQuery(
                "SELECT * FROM WHERE `server`=? AND `channel`=? AND `on`<=? AND ?<=`on` LIMIT ?;",
                server, channel, before, after, max
        )) {
            while (set.next()) {
                JSONObject message = new JSONObject();
                message.put("id", set.getLong("id"));
                message.put("author", set.getLong("author"));
                message.put("content", set.getLong("content"));
                message.put("timestamp", set.getLong("on"));
                message.put("reply", set.getLong("reply"));

                messages.put(message);
            }

            return messages;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isThereNewMessage(long lastSeen, long serverId) {
        return serverRepository.findById(serverId).map(server -> server.getLastUpdate() > lastSeen).orElse(false);
    }
}
