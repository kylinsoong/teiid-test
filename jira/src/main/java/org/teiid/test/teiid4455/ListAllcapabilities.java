package org.teiid.test.teiid4455;

import org.teiid.query.optimizer.capabilities.SourceCapabilities.Capability;

public class ListAllcapabilities {

    public static void main(String[] args) {

        Capability[] capabilities = Capability.values();
        for(Capability c : capabilities) {
            System.out.println(c);
        }
    }

}
