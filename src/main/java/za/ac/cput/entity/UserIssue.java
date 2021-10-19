package za.ac.cput.entity;

public class UserIssue {

    private String userIssueId;
    private String userId;
    private String issueId;

    private UserIssue(){}

    private UserIssue(Builder builder){
        this.userIssueId = builder.userIssueId;
        this.userId = builder.userId;
        this.issueId = builder.issueId;
    }

    public String getUserIssueId() {
        return userIssueId;
    }

    public String getUserId() {
        return userId;
    }

    public String getIssueId() {
        return issueId;
    }

    @Override
    public String toString() {
        return "UserIssue{" +
                "userIssueId='" + userIssueId + '\'' +
                ", userId='" + userId + '\'' +
                ", issueId='" + issueId + '\'' +
                '}';
    }

    public static class Builder{
        private String userIssueId, userId, issueId;

        public Builder userIssueId (String userIssueId){
            this.userIssueId = userIssueId;
            return this;
        }

        public Builder userId (String userId){
            this.userId = userId;
            return this;
        }

        public Builder issueId (String issueId){
            this.issueId = issueId;
            return this;
        }

        public Builder copy(UserIssue userIssue){
            this.userIssueId = userIssue.userIssueId;
            this.userId = userIssue.userId;
            this.issueId = userIssue.issueId;
            return this;
        }

        public UserIssue build(){
            return new UserIssue(this);
        }

    }
}
