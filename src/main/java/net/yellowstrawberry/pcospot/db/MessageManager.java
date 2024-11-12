package net.yellowstrawberry.pcospot.db;

import com.github.f4b6a3.tsid.TsidCreator;
import net.yellowstrawberry.pcospot.db.repository.ServerRepository;
import net.yellowstrawberry.pcospot.object.server.Server;
import net.yellowstrawberry.pcospot.utils.SQLCommunicator;
import org.springframework.stereotype.Service;

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

    public static boolean isThereNewMessage(long lastSeen, long serverId) {
        return serverRepository.findById(serverId).map(server -> server.getLastUpdate() > lastSeen).orElse(false);
    }
}
