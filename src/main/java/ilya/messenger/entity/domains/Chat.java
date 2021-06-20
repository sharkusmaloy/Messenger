package ilya.messenger.entity.domains;

import ilya.messenger.entity.repository.instances.UserInstance;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Chat {
  private final UUID id;
  private final UUID owner;
  private final Instant creation;
  private final String title;
  private final boolean isPrivate;
  private Set<String> users;

  public Chat(UUID id, UUID owner, String title, Instant creation, boolean isPrivate) {
    this.id = id;
    this.owner = owner;
    this.creation = creation;
    this.title = title;
    this.isPrivate = isPrivate;
    if(isPrivate) users = new HashSet<>();
  }

  public UUID getId() {
    return id;
  }

  public UUID getOwnerId() {
    return owner;
  }

  public String getTitle() {
    return title;
  }

  public String getUsers() {
    if(!isPrivate) return null;
    return String.join(",", this.users);
  }

  public boolean isPrivate() {
    return isPrivate;
  }

  public Boolean addUser(UUID userID) {
    if(!isPrivate) return false;
    return users.add(userID.toString());
  }

  public boolean check(String name) {
    User user = UserInstance.getInstance().getUser(name);
    for (String id: users){
      if(id.equals(user.getId().toString())){
        return true;
      }
    }
    return false;
  }

  public void setUsers(Set<String> users) {
    this.users = users;
  }

  public Instant getCreationTime() {
    return creation;
  }
}
