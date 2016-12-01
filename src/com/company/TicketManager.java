package com.company;

import java.util.*;

public class TicketManager {

    public static void main(String[] args) {

        // One list for unresolved tickets, one for resolved tickets.
        LinkedList<Ticket> ticketQueue = new LinkedList<>();
        LinkedList<Ticket> resolvedTickets = new LinkedList<>();

        Scanner scan = new Scanner(System.in);

        while(true){

            System.out.println("1. Enter Ticket\n2. Delete Ticket by ID\n3. Delete Ticket by Issue\n4. Find Ticket " +
                    "by Reporter\n5. Display All Tickets\n6. View Resolved Tickets\n7. Quit");
            Integer task = Integer.parseInt(scan.next());

            if (task == 1) {
                //Call addTickets, which will let us enter any number of new tickets
                addTickets(ticketQueue);

            } else if (task == 2) {
                //delete a ticket
                // Takes two new arguments - one for a list of matching tickets for a search term, and the list of
                // resolved tickets we'll add to once we delete the ticket.
                deleteTicket(ticketQueue, null, resolvedTickets);

            } else if (task == 3) {
                // Ask for the search term and find all matching tickets, then delete from those options.
                System.out.println("Enter the search term:");
                LinkedList<Ticket> matchingTickets = searchDescriptions(ticketQueue);
                deleteTicket(ticketQueue, matchingTickets, resolvedTickets);

            } else if (task == 4) {
                // Just displays a list of all tickets opened by a certain employee.
                System.out.println("Enter the name of the reporter:");
                LinkedList<Ticket> matchingTickets = searchByName(ticketQueue);
                printAllTickets(matchingTickets);

            } else if (task == 5) {
                // Display all the unresolved tickets.
                printAllTickets(ticketQueue);

            } else if (task == 6) {
                // Display all the resolved tickets.
                printAllTickets(resolvedTickets);
            }

            else if ( task == 7 ) {
                //Quit. Future prototype may want to save all tickets to a file
                System.out.println("Quitting program");
                break;
            }
            else {
                //this will happen for 3 or any other selection that is a valid int
                //TODO Program crashes if you enter anything else - please fix
                //Default will be print all tickets
                printAllTickets(ticketQueue);
            }
        }
    }

    protected static void deleteTicket(LinkedList<Ticket> ticketQueue, LinkedList<Ticket> matchingTickets,
                                       LinkedList<Ticket> resolvedTickets) {
        if (matchingTickets == null)
        {
            printAllTickets(ticketQueue);   // If there's no list of matching tickets, display all unresolved tickets.
        }

        else
        {
            printAllTickets(matchingTickets); // Display the list of matching tickets.
        }

        if (ticketQueue.size() == 0) {    //no tickets!
            System.out.println("No tickets to delete!\n");
            return;
        }

        Scanner deleteScanner = new Scanner(System.in);
        Scanner resScanner = new Scanner(System.in);

        //Loop over all tickets. Delete the one with this ticket ID
        outer: while (true)
        {
            System.out.println("Enter ID of ticket to delete");
            int deleteIDint;
            String deleteID = deleteScanner.next();

            try
            {
                // Make sure the input is an integer.
                deleteIDint = Integer.parseInt(deleteID);
            }

            catch (NumberFormatException nfe)
            {
                // If not, display a message and restart the while true loop.
                System.out.println("Enter an integer value.");
                continue outer;
            }

            boolean found = false;
            for (Ticket ticket : ticketQueue)
            {
                // Loop through all tickets looking for one with a matching ID.
                if (ticket.getTicketID() == deleteIDint)
                {
                    // When found, remove the ticket from the ticketQueue.
                    found = true;
                    ticketQueue.remove(ticket);
                    // Prompt the user to enter a reason for closing.
                    System.out.println("Enter the reason for the ticket being closed:");
                    String res = resScanner.nextLine();
                    // Create a new ResolvedTicket object and add it to the list of resolved tickets.
                    ResolvedTicket rt = new ResolvedTicket(ticket.getDescription(), ticket.getPriority(),
                            ticket.getReporter(), ticket.getDateReported(), ticket.getTicketID(), res);
                    resolvedTickets.add(rt);
                    System.out.println(String.format("Ticket %d deleted", deleteIDint));
                    break; //don't need loop any more.
                }
            }
            if (!found)
            {
                // If the ID was not found, restart the while true loop.
                System.out.println("Ticket ID not found, no ticket deleted");
                continue outer;
            }

            break;
        }
        printAllTickets(ticketQueue);  //print updated list
    }


    protected static void addTickets(LinkedList<Ticket> ticketQueue) {
        Scanner sc = new Scanner(System.in);
        boolean moreProblems = true;
        String description, reporter;
        Date dateReported = new Date(); //Default constructor creates Date with current date/time
        int priority;

        while (moreProblems){
            System.out.println("Enter problem");
            description = sc.nextLine();
            System.out.println("Who reported this issue?");
            reporter = sc.nextLine();
            System.out.println("Enter priority of " + description);
            priority = Integer.parseInt(sc.nextLine());

            Ticket t = new Ticket(description, priority, reporter, dateReported);
            //ticketQueue.add(t);
            addTicketInPriorityOrder(ticketQueue, t);

            printAllTickets(ticketQueue);

            System.out.println("More tickets to add?");
            String more = sc.nextLine();
            if (more.equalsIgnoreCase("N")) {
                moreProblems = false;
            }
        }
    }


    protected static void addTicketInPriorityOrder(LinkedList<Ticket> tickets, Ticket newTicket){

        //Logic: assume the list is either empty or sorted

        if (tickets.size() == 0 ) {//Special case - if list is empty, add ticket and return
            tickets.add(newTicket);
            return;
        }

        //Tickets with the HIGHEST priority number go at the front of the list. (e.g. 5=server on fire)
        //Tickets with the LOWEST value of their priority number (so the lowest priority) go at the end

        int newTicketPriority = newTicket.getPriority();

        for (int x = 0; x < tickets.size() ; x++) {    //use a regular for loop so we know which element we are looking at

            //if newTicket is higher or equal priority than the this element, add it in front of this one, and return
            if (newTicketPriority >= tickets.get(x).getPriority()) {
                tickets.add(x, newTicket);
                return;
            }
        }

        //Will only get here if the ticket is not added in the loop
        //If that happens, it must be lower priority than all other tickets. So, add to the end.
        tickets.addLast(newTicket);
    }


    protected static void printAllTickets(LinkedList<Ticket> tickets) {
        System.out.println(" ------- All open tickets ----------");

        for (Ticket t : tickets ) {
            System.out.println(t); //Write a toString method in Ticket class
            //println will try to call toString on its argument
        }
        System.out.println(" ------- End of ticket list ----------");

    }

    protected static LinkedList<Ticket> searchDescriptions(LinkedList<Ticket> tickets)
    {
        // Returns a list of tickets that match a search term.
        LinkedList<Ticket> matchingTickets = new LinkedList<>();
        Scanner scan = new Scanner(System.in);
        String searchTerm = scan.nextLine();

        // Loop through all unresolved tickets and add all containing the search term in their description.
        for (Ticket t : tickets)
        {
            if (t.getDescription().contains(searchTerm))
            {
                matchingTickets.add(t);
            }
        }

        return matchingTickets;
    }

    protected static LinkedList<Ticket> searchByName(LinkedList<Ticket> tickets)
    {
        // Returns a list of tickets opened by a certain employee - basically the same as the above method.
        LinkedList<Ticket> matchingTickets = new LinkedList<>();
        Scanner scan = new Scanner(System.in);
        String searchTerm = scan.nextLine();


        for (Ticket t : tickets)
        {
            if (t.getReporter().contains(searchTerm))
            {
                matchingTickets.add(t);
            }
        }

        return matchingTickets;
    }
}

