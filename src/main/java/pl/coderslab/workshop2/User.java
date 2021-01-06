package pl.coderslab.workshop2;

public class User {
  private long id = 0;
  private String userName;
  private String email;
  private String password;

  public User(String userName, String email, String password) {
    this.userName = userName;
    this.email = email;
    this.password = password;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
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
