package ilya.messenger.servlets;

import ilya.messenger.entity.domains.Chat;
import ilya.messenger.entity.domains.Sender;
import ilya.messenger.entity.domains.User;
import ilya.messenger.entity.repository.instances.ChatInstance;
import ilya.messenger.entity.repository.instances.MessageInstance;
import ilya.messenger.entity.repository.instances.UserInstance;
import ilya.messenger.entity.repository.services.MyStorageAgent;

import java.util.List;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ApplicationBoot implements ServletContextListener {

  @Override
  public void contextInitialized(ServletContextEvent sce) {
    try {
      List<User> users = MyStorageAgent.getInstance().loadUsers();
      UserInstance.getInstance().setUsers(users);
      if (users.isEmpty()) UserInstance.getInstance().addAdmin();

      List<Chat> chats = MyStorageAgent.getInstance().loadChats();
      ChatInstance.getInstance().setChats(chats);

      List<Sender> senders = MyStorageAgent.getInstance().loadMessages();
      MessageInstance.getInstance().setSenders(senders);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void contextDestroyed(ServletContextEvent sce) {}
}
