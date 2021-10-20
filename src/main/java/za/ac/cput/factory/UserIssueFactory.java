package za.ac.cput.factory;

import za.ac.cput.entity.UserIssue;

public class UserIssueFactory {

    public static UserIssue createUserIssue(String userId, String issueId){
        if(userId.isEmpty() || userId == null || issueId.isEmpty() || issueId == null)
            return new UserIssue.Builder().build();
        return new UserIssue.Builder().userId(userId).issueId(issueId).build();
    }
}