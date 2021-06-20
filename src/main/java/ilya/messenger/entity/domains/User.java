package ilya.messenger.entity.domains;

import org.mindrot.jbcrypt.BCrypt;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class User {
  private final UUID id;
  private final String name;
  private final Instant creation;
  private boolean admin;
  private String email;
  private String bio;
  private String password;

  public User(UUID id, String name, String password, Instant creation) {
    this.id = id;
    this.name = name;
    this.password = password;
    this.creation = creation;
    this.admin = false;
    this.bio = "";
    this.email = "";
  }

  public UUID getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Instant getCreationTime() {
    return creation;
  }

  public boolean isAdmin() {
    return admin;
  }

  public void setAdmin(boolean status) {
    this.admin = status;
  }

  public String getBio() {
    return bio;
  }

  public void setBio(String bio) {
    this.bio = bio;
  }

  public String getEmail() { return email; }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String newPassword) {
    String password = BCrypt.hashpw(newPassword, BCrypt.gensalt());
    this.password = password;
  }
}
