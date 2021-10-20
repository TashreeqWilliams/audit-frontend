/* IssueFactory.java Class
 * Entity for Creating an Issue but Encapsulating Issue inner workings
 * Author: Athenkosi Zono (218030185)
 * Date: 4 June 2021
 */

package za.ac.cput.factory;

import za.ac.cput.entity.Issue;
import za.ac.cput.util.KeyGenerator;

public class IssueFactory {

    public static Issue createIssue(String issueDescription, String issueArea,
                                    String issueRaisedDate, String issueResolvedDate, int issueStatus,
                                    int isResolved, int isValidated){
        String issueId = KeyGenerator.genratedId();
        return new Issue.Builder().issueId(issueId).issueDescription(issueDescription).issueArea(issueArea)
                .issueRaisedDate(issueRaisedDate).issueResolvedDate(issueResolvedDate).issueStatus(issueStatus)
                .isResolved(isResolved).isValidated(isValidated).build();
    }

}
