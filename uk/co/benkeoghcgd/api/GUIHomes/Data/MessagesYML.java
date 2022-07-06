package uk.co.benkeoghcgd.api.GUIHomes.Data;

import uk.co.benkeoghcgd.api.AxiusCore.API.AxiusPlugin;
import uk.co.benkeoghcgd.api.AxiusCore.API.DataHandler;

public class MessagesYML extends DataHandler {

    public MessagesYML(AxiusPlugin instance) {
        super(instance, "Messages");
    }

    @Override
    protected void saveDefaults() {
    }
}
