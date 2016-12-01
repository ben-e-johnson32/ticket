package com.company;

import java.util.Date;
public class Ticket {

    // Changed these to protected to allow access from subclass.
    protected int priority;
    protected String reporter; //Stores person or department who reported issue
    protected String description;
    protected Date dateReported;

    //STATIC Counter - accessible to all Ticket objects.
    //If any Ticket object modifies this counter, all Ticket objects will have the modified value
    //Make it private - only Ticket objects should have access
    private static int staticTicketIDCounter = 1;
    //The ID for each ticket - instance variable. Each Ticket will have it's own ticketID variable
    protected int ticketID;

    public Ticket(String desc, int p, String rep, Date date) {
        this.description = desc;
        this.priority = p;
        this.reporter = rep;
        this.dateReported = date;
        this.ticketID = staticTicketIDCounter;
        staticTicketIDCounter++;
    }

    protected int getPriority() {
        return priority;
    }

    public int getTicketID() {
        return ticketID;
    }

    public String getDescription() { return description; }

    public String getReporter() { return reporter; }

    public Date getDateReported() { return dateReported; }

    public String toString(){
        return("ID= " + this.ticketID + " Issued: " + this.description + " Priority: " + this.priority + " Reported by: "
                + this.reporter + " Reported on: " + this.dateReported);
    }


}

class ResolvedTicket extends Ticket
{
    // The two extra fields needed for resolved tickets.
    private Date resolvedDate;
    private String resolutionDescription;

    //The ID for each ticket - instance variable. Each Ticket will have it's own ticketID variable
    private int ticketID;

    public ResolvedTicket(String desc, int p, String rep, Date date, int ID, String resolutionDescription) {
        // Call the superclass' constructor and then fill in the resolution date and description. Set the ID to the same
        // ID as the unresolved version of the ticket.
        super(desc, p, rep, date);
        this.resolvedDate = new Date();
        this.resolutionDescription = resolutionDescription;
        this.ticketID = ID;
    }

    public String toString()
    {
        // Override the superclass' toString method.
        return "ID = " + this.ticketID + " Issued: " + this.description + " Reported by: " + this.reporter + " Reported " +
                "on: " + this.dateReported + " Resolution: " + this.resolutionDescription + " Resolved on: " + this.resolvedDate;
    }
}

