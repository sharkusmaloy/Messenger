package ilya.messenger.entity.repository.instances;

import ilya.messenger.entity.domains.User;
import ilya.messenger.entity.repository.services.MyStorageAgent;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.mindrot.jbcrypt.BCrypt;

public class UserInstance {

  private static UserInstance instance;

  public static UserInstance getInstance() {
    if (instance == null) {
      instance = new UserInstance(MyStorageAgent.getInstance());
    }
    return instance;
  }

  public static UserInstance getTestInstance(MyStorageAgent myStorageAgent) {
    instance = new UserInstance(myStorageAgent);
    instance.addAdmin();
    return instance;
  }

  private MyStorageAgent myStorageAgent;

  private List<User> users;

  private UserInstance(MyStorageAgent myStorageAgent) {
    this.myStorageAgent = myStorageAgent;
    users = new ArrayList<>();
  }

  public void addUser(String username, String password, boolean admin) {
    String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
    User user = new User(UUID.randomUUID(), username, hashedPassword, Instant.now());
    user.setAdmin(admin);
    this.addUser(user);
  }

  public User getUser(String username) {
    for (User user : users) {
      if (user.getName().equals(username)) {
        return user;
      }
    }
    return null;
  }

  public User getUser(UUID id) {
    for (User user : users) {
      if (user.getId().equals(id)) {
        return user;
      }
    }
    return null;
  }

  public User getUserUUIDString(String id) {
    for (User user : users) {
      if (user.getId().toString().equals(id)) {
        return user;
      }
    }
    return null;
  }

  public void addUser(User user) {
    users.add(user);
    myStorageAgent.writeThrough(user);
  }

  public void updateUser(User user) {
    myStorageAgent.writeThrough(user);
  }

  public boolean isUserRegistered(String username) {
    for (User user : users) {
      if (user.getName().equalsIgnoreCase(username)) {
        return true;
      }
    }
    return false;
  }

  public void setUsers(List<User> users) {
    for (User user : users) {
      this.users.add(user);
    }
  }

  public List<User> getUsers() {
    return users;
  }

  public ArrayList<User> getAdmins() {
    ArrayList<User> admins = new ArrayList<>();
    for (User user : users) {
      if (user.isAdmin()) {
        admins.add(user);
      }
    }
    return admins;
  }

  public void addAdmin() {
    instance.addUser("admcheck", "admcheck", true);
  }
}
