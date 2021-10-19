package za.ac.cput.entity;

/* UserAccount.java Class
 * Entity for UserAccount
 * Author: Tashreeq Williams (217290671)
 * Date: 1st June 2021
 */

public class UserAccount {
private String userId;
private String email;
private String password;
private int loginStatus;
private String registerDate;

    private UserAccount(Builder builder) {
        this.userId = builder.userId;
        this.email = builder.email;
        this.password = builder.password;
        this.loginStatus = builder.loginStatus;
        this.registerDate = builder.registerDate;
    }

    public String getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public int getLoginStatus() {
        return loginStatus;
    }

    public String getRegisterDate() {
        return registerDate;
    }

    @Override
    public String toString() {
        return "UserAccount{" +
                "userId='" + userId + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", loginStatus=" + loginStatus +
                ", registerDate='" + registerDate + '\'' +
                '}';
    }

    public static class Builder{
    private String userId;
    private String email;
    private String password;
    private int loginStatus;
    private String registerDate;

    public Builder setuserId(String userId){
        this.userId = userId;
        return this;
    }
    public Builder setemail(String email){
        this.email = email;
        return this;
    }
    public Builder setpassword(String password){
        this.password = password;
        return this;
    }
    public Builder setloginStatus(int loginStatus){
        this.loginStatus = loginStatus;
        return this;
    }
    public Builder setregisterDate(String registerDate){
        this.registerDate = registerDate;
        return this;
    }
    public UserAccount build(){
        return new UserAccount(this);
    }
    private Builder copy(UserAccount userAccount){

        this.userId = userAccount.userId;
        this.email = userAccount.email;
        this.password = userAccount.password;
        this.loginStatus = userAccount.loginStatus;
        this.registerDate = userAccount.registerDate;

        return this;
}

    }

}
