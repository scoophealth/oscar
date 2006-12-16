package org.oscarehr.PMmodule.integrator.message;

import javax.resource.spi.ActivationSpec;

import org.jencks.JCAConnector;
import org.jencks.JCAContainer;

public class JCAConnectorFactory {

    private JCAContainer jcaContainer;
    private ActivationSpec activationSpec;
    private String ref;

    

    public ActivationSpec getActivationSpec() {
		return activationSpec;
	}

	public void setActivationSpec(ActivationSpec activationSpec) {
		this.activationSpec = activationSpec;
	}

	public JCAContainer getJcaContainer() {
		return jcaContainer;
	}

	public void setJcaContainer(JCAContainer jcaContainer) {
		this.jcaContainer = jcaContainer;
	}

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	public JCAConnector startConsumption() throws Exception {

        JCAConnector jcaConnector = new JCAConnector();
        jcaConnector.setJcaContainer(jcaContainer);
        jcaConnector.setActivationSpec(activationSpec);
        jcaConnector.setRef(ref);

        // start consumption
        jcaConnector.setBeanFactory(jcaContainer.getApplicationContext()); 
        jcaConnector.afterPropertiesSet();

        return jcaConnector;

    }

    public void stopConsumption(JCAConnector jcaConnector) throws Exception {

        // stop consumption
        jcaConnector.destroy();
        jcaConnector = null;
    }

}