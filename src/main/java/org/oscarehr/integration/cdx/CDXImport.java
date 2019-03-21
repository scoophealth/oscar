package org.oscarehr.integration.cdx;

import ca.uvic.leadlab.obibconnector.facades.receive.*;
import ca.uvic.leadlab.obibconnector.impl.receive.mock.ReceiveDocMock;

public class CDXImport {

    private IReceiveDoc receiver = new ReceiveDocMock();


    public void importNewDocs() {

        String[] docIds;

        try {

            docIds = receiver.pollNewDocIDs();

            for (String id : docIds) {

                IDocument doc = receiver.retrieveDocument(id);

                storeDocument(doc);

            }
        } catch (Exception e) {
            // log exception and error
        }

    }

    private void storeDocument(IDocument doc) {
    }

}
