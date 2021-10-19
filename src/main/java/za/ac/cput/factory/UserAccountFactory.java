package za.ac.cput.factory;
/*
        @Author : Tashreeq Williams
        -> UserAccountFactory
        Date: 4/6/2021

*/

import za.ac.cput.entity.UserAccount;


public class UserAccountFactory {
    public static UserAccount buildUserAccount(String userId,
                                               String email,
                                               String password,
                                               int LoginStatus,
                                               String registerDate)
    {

        return new UserAccount.Builder()


                .setuserId(userId)
                .setemail(email)
                .setpassword(password)
                .setloginStatus(LoginStatus)
                .setregisterDate(registerDate)
                .build();


    }
}
