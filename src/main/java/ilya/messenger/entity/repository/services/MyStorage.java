package ilya.messenger.entity.repository.services;

import ilya.messenger.entity.domains.*;
import com.google.appengine.api.datastore.*;
import com.google.appengine.api.datastore.Query.SortDirection;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.mindrot.jbcrypt.BCrypt;

public class MyStorage {

  private DatastoreService datastore;

  public MyStorage() {
    datastore = DatastoreServiceFactory.getDatastoreService();
  }

  public List<User> loadUsers() {
    List<User> users = new ArrayList<>();
    Query query = new Query("chat-users");
    PreparedQuery results = datastore.prepare(query);

    for (Entity entity : results.asIterable()) {
      try {
        UUID uuid = UUID.fromString((String) entity.getProperty("uuid"));
        String userName = (String) entity.getProperty("username");
        String password = (String) entity.getProperty("password_hash");
        Instant creationTime = Instant.parse((String) entity.getProperty("creation_time"));
        User user = new User(uuid, userName, password, creationTime);
        String bio = (String) entity.getProperty("about_me");
        String email = (String) entity.getProperty("email");
        user.setBio(bio);
        user.setEmail(email);
        if (BCrypt.checkpw("admcheck", user.getPassword().toString())) {
          user.setAdmin(true);
        }
        users.add(user);
      } catch (Exception e) {

      }
    }

    return users;
  }

  public List<Chat> loadChats() {

    List<Chat> chats = new ArrayList<>();

    Query query = new Query("chat-chats").addSort("creation_time", SortDirection.ASCENDING);
    PreparedQuery results = datastore.prepare(query);

    for (Entity entity : results.asIterable()) {
      try {
        UUID uuid = UUID.fromString((String) entity.getProperty("uuid"));
        UUID ownerUuid = UUID.fromString((String) entity.getProperty("owner_uuid"));
        String title = (String) entity.getProperty("title");
        Instant creationTime = Instant.parse((String) entity.getProperty("creation_time"));
        Boolean isPrivate = Boolean.valueOf((String) entity.getProperty("isPrivate"));
        Chat chat =
            new Chat(uuid, ownerUuid, title, creationTime, isPrivate);
        if (isPrivate) {
          Set<String> users =
              new HashSet<String>(
                  Arrays.asList(((String) (entity.getProperty("users"))).split(",")));
          chat.setUsers(users);
        }
        chats.add(chat);
      } catch (Exception e) {

      }
    }

    return chats;
  }

  public List<Sender> loadMessages() {

    List<Sender> senders = new ArrayList<>();

    Query query = new Query("chat-senders").addSort("creation_time", SortDirection.ASCENDING);
    PreparedQuery results = datastore.prepare(query);

    for (Entity entity : results.asIterable()) {
      try {
        UUID uuid = UUID.fromString((String) entity.getProperty("uuid"));
        UUID chatUuid = UUID.fromString((String) entity.getProperty("conv_uuid"));
        UUID authorUuid = UUID.fromString((String) entity.getProperty("author_uuid"));
        Instant creationTime = Instant.parse((String) entity.getProperty("creation_time"));
        String content = (String) entity.getProperty("content");
        Boolean isPrivate = Boolean.valueOf((String) entity.getProperty("isPrivate"));
        Sender sender =
            new Sender(uuid, chatUuid, authorUuid, isPrivate, content, creationTime);
        senders.add(sender);
      } catch (Exception e) {

      }
    }

    return senders;
  }

  public void writeThrough(User user) {
    Entity userEntity = new Entity("chat-users", user.getId().toString());
    userEntity.setProperty("uuid", user.getId().toString());
    userEntity.setProperty("username", user.getName());
    userEntity.setProperty("email", user.getEmail());
    userEntity.setProperty("password_hash", user.getPassword());
    userEntity.setProperty("creation_time", user.getCreationTime().toString());
    userEntity.setProperty("about_me", user.getBio());
    datastore.put(userEntity);
  }

  public void writeThrough(Sender sender) {
    Entity messageEntity = new Entity("chat-messages", sender.getId().toString());
    messageEntity.setProperty("uuid", sender.getId().toString());
    messageEntity.setProperty("conv_uuid", sender.getChatsId().toString());
    messageEntity.setProperty("author_uuid", sender.getAuthorId().toString());
    messageEntity.setProperty("isPrivate", String.valueOf(sender.isPrivate()));
    messageEntity.setProperty("content", sender.getContent());
    messageEntity.setProperty("creation_time", sender.getCreationTime().toString());
    datastore.put(messageEntity);
  }

  public void writeThrough(Chat chat) {
    Entity chatEntity = new Entity("chat-chats", chat.getId().toString());
    chatEntity.setProperty("uuid", chat.getId().toString());
    chatEntity.setProperty("owner_uuid", chat.getOwnerId().toString());
    chatEntity.setProperty("title", chat.getTitle());
    chatEntity.setProperty("creation_time", chat.getCreationTime().toString());
    chatEntity.setProperty("isPrivate", String.valueOf(chat.isPrivate()));
    if (chat.isPrivate()) chatEntity.setProperty("users", chat.getUsers());
    datastore.put(chatEntity);
  }

  public void deleteFrom(Sender sender) {
    Query query = new Query("chat-messages").addSort("creation_time", SortDirection.ASCENDING);
    PreparedQuery results = datastore.prepare(query);
    for (Entity entity : results.asIterable()) {
      String id = (String) entity.getProperty("uuid");
      if (id.equals(sender.getId().toString())) {
        datastore.delete(entity.getKey());
      }
    }
  }
}
