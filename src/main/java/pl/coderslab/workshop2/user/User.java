package pl.coderslab.workshop2.user;

public class User {
  private long id;
  private String userName;
  private String email;
  private String password;

  public User(Long id, String username, String email, String password) {
    this.id = id;
    this.userName = username;
    this.email = email;
    this.password = password;
  }

  public User(String username, String email, String password) {
    this.userName = username;
    this.email = email;
    this.password = password;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public String toString() {
    return "User{" +
            "id=" + id +
            ", userName='" + userName + '\'' +
            ", email='" + email + '\'' +
            ", password='" + password + '\'' +
            '}';
  }
}
