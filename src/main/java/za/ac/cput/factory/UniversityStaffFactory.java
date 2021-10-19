/* Assignment 3.java
 Entity for the UniversityStaff
 Author: Avuyile Xozumthi (218331827)
 Date: 04 june 2021
*/
package za.ac.cput.factory;

import za.ac.cput.entity.UniversityStaff;

public class UniversityStaffFactory {
    public static UniversityStaff buildUniversityStaff(String staffID,
                                                       String staffFirstName,
                                                       String staffSurname,
                                                       String cellphone)
    {
        return new UniversityStaff.Builder()

                .staffFirstName(staffFirstName)
                .staffSurname(staffSurname)
                .cellphone(cellphone)
                .staffID(staffID)
                .Build();
    }
}



    

