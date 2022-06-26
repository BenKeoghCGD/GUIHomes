package uk.co.benkeoghcgd.api.GUIHomes.Data;

import uk.co.benkeoghcgd.api.AxiusCore.API.AxiusPlugin;
import uk.co.benkeoghcgd.api.AxiusCore.API.DataHandler;

public class ConfigYML extends DataHandler {
    public ConfigYML(AxiusPlugin instance) {
        super(instance, "Config");
    }

    @Override
    protected void saveDefaults() {
        setData("defaults.maxHomes", 2);
    }
}
