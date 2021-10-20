/* TicketFactory.java Class
 * Entity for Hiding Issue Implementation
 * Author: Sicelo Zitha (216140943)
 * Date: 4 June 2021
 */
package za.ac.cput.factory;

import za.ac.cput.entity.Ticket;

public class TicketFactory {
    public static Ticket buildTicket(String ticketId,
                                     String ticketDescription,
                                     String ticketDate)
    {
        return new Ticket.Builder()
                .ticketId(ticketId)
                .ticketDescription(ticketDescription)
                .ticketDate(ticketDate)
                .build();
    }

}
