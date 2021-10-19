/* Issue.java Class
 * Entity for Issue
 * Author: Athenkosi Zono (218030185)
 * Date: 1 June 2021
 */

package za.ac.cput.entity;

import java.io.Serializable;

public class Issue implements Serializable {

    private String issueId;
    private String issueDescription, issueArea, issueRaisedDate, issueResolvedDate;
    private boolean issueStatus, isResolved, isValidated;

    private Issue(){}

    private Issue(Builder builder){
        this.issueId = builder.issueId;
        this.issueDescription = builder.issueDescription;
        this.issueArea = builder.issueArea;
        this.issueRaisedDate = builder.issueRaisedDate;
        this.issueResolvedDate = builder.issueResolvedDate;
        this.issueStatus = builder.issueStatus;
        this.isResolved = builder.isResolved;
        this.isValidated = builder.isValidated;
    }

    public String getIssueId() {
        return issueId;
    }

    public String getIssueDescription() {
        return issueDescription;
    }

    public String getIssueArea() {
        return issueArea;
    }

    public String getIssueRaisedDate() {
        return issueRaisedDate;
    }

    public String getIssueResolvedDate() {
        return issueResolvedDate;
    }

    public boolean isIssueStatus() {
        return issueStatus;
    }

    public boolean isResolved() {
        return isResolved;
    }

    public boolean isValidated() {
        return isValidated;
    }

    @Override
    public String toString() {
        return "Issue{" +
                "issueId='" + issueId + '\'' +
                ", issueDescription='" + issueDescription + '\'' +
                ", issueArea='" + issueArea + '\'' +
                ", issueRaisedDate='" + issueRaisedDate + '\'' +
                ", issueResolvedDate='" + issueResolvedDate + '\'' +
                ", issueStatus=" + issueStatus +
                ", isResolved=" + isResolved +
                ", isValidated=" + isValidated +
                '}';
    }

    public static class Builder {
        private String issueId, issueDescription, issueArea, issueRaisedDate, issueResolvedDate;
        private boolean issueStatus, isResolved, isValidated;

        public Builder issueId(String issueId){
            this.issueId = issueId;
            return this;
        }

        public Builder issueDescription(String issueDescription){
            this.issueDescription = issueDescription;
            return this;
        }

        public Builder issueArea(String issueArea){
            this.issueArea = issueArea;
            return this;
        }

        public Builder issueRaisedDate(String issueRaisedDate){
            this.issueRaisedDate = issueRaisedDate;
            return this;
        }

        public Builder issueResolvedDate(String issueResolvedDate){
            this.issueResolvedDate = issueResolvedDate;
            return this;
        }

        public Builder issueStatus(boolean issueStatus){
            this.issueStatus = issueStatus;
            return this;
        }

        public Builder isResolved(boolean isResolved){
            this.isResolved = isResolved;
            return this;
        }

        public Builder isValidated(boolean isValidated){
            this.isValidated = isValidated;
            return this;
        }

        public Builder copy(Issue issue){
            this.issueId = issue.issueId;
            this.issueDescription = issue.issueDescription;
            this.issueArea = issue.issueArea;
            this.issueRaisedDate = issue.issueRaisedDate;
            this.issueResolvedDate = issue.issueResolvedDate;
            this.issueStatus = issue.issueStatus;
            this.isResolved = issue.isResolved;
            this.isValidated = issue.isValidated;
            return this;
        }

        public Issue build(){
            return new Issue(this);
        }

    }
}
