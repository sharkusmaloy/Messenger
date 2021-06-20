package ilya.messenger.entity.repository.instances;

import ilya.messenger.entity.domains.Sender;
import ilya.messenger.entity.repository.services.MyStorageAgent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MessageInstance {

  private static MessageInstance instance;


  public static MessageInstance getInstance() {
    if (instance == null) {
      instance = new MessageInstance(MyStorageAgent.getInstance());
    }
    return instance;
  }

  public static MessageInstance getTestInstance(MyStorageAgent myStorageAgent) {
    instance = new MessageInstance(myStorageAgent);
    return instance;
  }

  private MyStorageAgent myStorageAgent;

  private List<Sender> senders;

  private MessageInstance(MyStorageAgent myStorageAgent) {
    this.myStorageAgent = myStorageAgent;
    senders = new ArrayList<>();
  }

  public void deleteMessage(Sender sender) {
    senders.remove(sender);
    myStorageAgent.deleteFrom(sender);
  }

  public void addMessage(Sender sender) {
    senders.add(sender);
    myStorageAgent.writeThrough(sender);
  }

  public List<Sender> getMessagesInChats(UUID chatId) {
    List<Sender> messagesInChats = new ArrayList<>();
    for (Sender sender : senders) {
      if (sender.getChatsId().equals(chatId)) {
        messagesInChats.add(sender);
      }
    }
    return messagesInChats;
  }

  public List<Sender> getMessagesByUser(UUID id) {

    List<Sender> messagesByUser = new ArrayList<>();

    for (Sender sender : senders) {
      if (sender.getAuthorId().equals(id)) {
        messagesByUser.add(sender);
      }
    }

    return messagesByUser;
  }

  public Sender getMessageById(UUID id) {
    for (Sender sender : senders) {
      if (sender.getId().equals(id)) {
        return sender;
      }
    }
    return null;
  }

  public void setSenders(List<Sender> senders) {
    this.senders = senders;
  }

  public int getTotalMessages() {
    return this.senders.size();
  }
}
