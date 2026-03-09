package lab3Sockets;
/*
 * Test different brands of plugs.
 * 
 * Daniel Olusegun 
 * 24362731
 * 16/02/2006
 */ 

/**
 * Test class demonstrating usage of plugs and adapters.
 * Shows compatibility between German and UK plugs
 * using the Adapter Pattern.
 */
public class TestPlugs {

    /**
     * Main method to run adapter demonstrations.
     *
     * @param args command line arguments (unused)
     */
    public static void main(String[] args) {

        System.out.println("German → German:");
        GermanPlugConnector germanPlug = new ZestPlug();
        GermanElectricalSocket germanSocket = new GermanElectricalSocket();
        germanSocket.plugIn(germanPlug);

        System.out.println("\nGerman → UK (adapter):");
        UKPlugConnector germanToUK =
                new GermanToUKPlugConnectorAdapter(germanPlug);

        UKElectricalSocket ukSocket = new UKElectricalSocket();
        ukSocket.plugIn(germanToUK);

        System.out.println("\nUK → UK:");
        UKPlugConnector ukPlug = new FurutechPlug();
        ukSocket.plugIn(ukPlug);

        System.out.println("\nUK → German (adapter):");
        GermanPlugConnector ukToGerman =
                new UKToGermanPlugConnectorAdapter(ukPlug);

        germanSocket.plugIn(ukToGerman);
    }
}
