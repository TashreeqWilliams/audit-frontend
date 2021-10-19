/* Ticket.java Class
 * Entity for Ticket
 * Author: Sicelo Zitha (216140943)
 * Date: 1 June 2021
 */
package za.ac.cput.entity;


public class Ticket {
    private String ticketId;
    private String ticketDescription;
    private String ticketDate;
    private String ticketIssue;

    private Ticket(Builder builder) {
        this.ticketId = builder.ticketId;
        this.ticketDescription = builder.ticketDescription;
        this.ticketDate = builder.ticketDate;
        this.ticketIssue = builder.ticketIssue;
    }

    public String getTicketId() {
        return ticketId;
    }

    public String getTicketDescription() {
        return ticketDescription;
    }

    public String getTicketDate() {
        return ticketDate;
    }

    public String getTicketIssue() {
        return ticketIssue;
    }

    @Override
    public String toString() {
        return "Builder{" +
                "ticketId='" + ticketId + '\'' +
                ", ticketDescription='" + ticketDescription + '\'' +
                ", ticketDate='" + ticketDate + '\'' +
                ", ticketIssue='" + ticketIssue + '\'' +
                '}';
    }

    public static class Builder {

        private String ticketId, ticketDescription, ticketDate, ticketIssue;

        public Builder ticketIssue(String ticketIssue) {
            this.ticketIssue = ticketIssue;
            return this;
        }

        public Builder ticketDate(String ticketDate) {
            this.ticketDate = ticketDate;
            return this;
        }

        public Builder ticketDescription(String ticketDescription) {
            this.ticketDescription = ticketDescription;
            return this;
        }

        public Builder ticketId(String ticketId) {
            this.ticketId = ticketId;
            return this;
        }

        public Ticket build() {
            return new Ticket(this);
        }

        public Builder copy(Ticket ticket) {
            this.ticketId = ticket.ticketId;
            this.ticketDescription = ticket.ticketDescription;
            this.ticketIssue = ticket.ticketIssue;
            this.ticketDate = ticket.ticketDate;
            return this;
        }

    }
}
