package ilya.messenger.entity.repository.services;

import ilya.messenger.entity.domains.Chat;
import ilya.messenger.entity.domains.Sender;
import ilya.messenger.entity.domains.User;

import java.util.List;

public class MyStorageAgent {

  private static MyStorageAgent instance;

  private final MyStorage myStorage;

  public static MyStorageAgent getInstance() {
    if (instance == null) {
      instance = new MyStorageAgent(new MyStorage());
    }
    return instance;
  }

  static MyStorageAgent getTestInstance(MyStorage mockMyStorage) {
    return new MyStorageAgent(mockMyStorage);
  }

  private MyStorageAgent(MyStorage myStorage) {
    this.myStorage = myStorage;
  }

  public List<User> loadUsers(){
    return myStorage.loadUsers();
  }

  public List<Chat> loadChats(){
    return myStorage.loadChats();
  }

  public List<Sender> loadMessages() {
    return myStorage.loadMessages();
  }

  public void writeThrough(User user) {
    myStorage.writeThrough(user);
  }

  public void writeThrough(Chat chat) {
    myStorage.writeThrough(chat);
  }

  public void writeThrough(Sender sender) {
    myStorage.writeThrough(sender);
  }

  public void deleteFrom(Sender sender) {
    myStorage.deleteFrom(sender);
  }

}
