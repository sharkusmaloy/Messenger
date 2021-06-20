package ilya.messenger.entity.domains;

import java.time.Instant;
import java.util.UUID;

public class Sender {

  private final UUID id;
  private final UUID chat;
  private final UUID author;
  private boolean isPrivate;
  private final String content;
  private final Instant creation;

  public Sender(UUID id, UUID chat, UUID author, boolean isPrivate, String content, Instant creation) {
    this.id = id;
    this.chat = chat;
    this.author = author;
    this.isPrivate = isPrivate;
    this.content = content;
    this.creation = creation;
  }

  public UUID getId() {
    return id;
  }

  public UUID getChatsId() {
    return chat;
  }

  public UUID getAuthorId() {
    return author;
  }

  public boolean isPrivate() {
    return isPrivate;
  }

  public String getContent() {
    return content;
  }

  public Instant getCreationTime() {
    return creation;
  }

  public void setIsPrivate(Boolean isPrivate) {
    this.isPrivate = isPrivate;
  }

}
