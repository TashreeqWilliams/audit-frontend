package za.ac.cput.factory;

import za.ac.cput.entity.Auditor;

public class AuditorFactory {

    public static Auditor buildAuditor(
            String auditorID,
            String auditorFirstName,
            String auditorSurname,
            String cellphone) {

        return new Auditor.Builder()
                .auditorID(auditorID)
                .auditorFirstName(auditorFirstName)
                .auditorSurname(auditorSurname)
                .cellphone(cellphone)
                .build();
    }
}
