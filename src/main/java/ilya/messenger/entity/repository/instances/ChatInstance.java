package ilya.messenger.entity.repository.instances;

import ilya.messenger.entity.domains.Chat;
import ilya.messenger.entity.repository.services.MyStorageAgent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChatInstance {

  private static ChatInstance instance;

  public static ChatInstance getInstance() {
    if (instance == null) {
      instance = new ChatInstance(MyStorageAgent.getInstance());
    }
    return instance;
  }

  public static ChatInstance getTestInstance(MyStorageAgent myStorageAgent) {
    instance = new ChatInstance(myStorageAgent);
    return instance;
  }

  private MyStorageAgent myStorageAgent;

  private List<Chat> chats;

  private ChatInstance(MyStorageAgent myStorageAgent) {
    this.myStorageAgent = myStorageAgent;
    chats = new ArrayList<>();
  }


  public List<Chat> getAllChats() {
    return chats;
  }

  public void updateChats(Chat conv) {
    myStorageAgent.writeThrough(conv);
  }

  public void addChats(Chat chat) {
    chats.add(chat);
    myStorageAgent.writeThrough(chat);
  }

  public boolean isTitleTaken(String title) {
    for (Chat chat : chats) {
      if (chat.getTitle().equalsIgnoreCase(title)) {
        return true;
      }
    }
    return false;
  }

  public Chat getChatsWithTitle(String title) {
    for (Chat chat : chats) {
      if (chat.getTitle().equals(title)) {
        return chat;
      }
    }
    return null;
  }

  public Chat getChatsById(UUID id) {
    for (Chat chat : chats) {
      if (chat.getId().equals(id)) {
        return chat;
      }
    }
    return null;
  }

  public void setChats(List<Chat> chats) {
    this.chats = chats;
  }
}
