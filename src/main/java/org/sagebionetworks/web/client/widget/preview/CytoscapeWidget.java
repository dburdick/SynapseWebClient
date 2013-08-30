package org.sagebionetworks.web.client.widget.preview;

import org.sagebionetworks.web.client.GlobalApplicationState;
import org.sagebionetworks.web.client.SynapseClientAsync;
import org.sagebionetworks.web.client.security.AuthenticationController;
import org.sagebionetworks.web.client.widget.SynapseWidgetPresenter;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class CytoscapeWidget implements CytoscapeWidgetView.Presenter, SynapseWidgetPresenter {

	private CytoscapeWidgetView view;
	private SynapseClientAsync synapseClient;
	private GlobalApplicationState globalApplicationState;
	private AuthenticationController authenticationController;

	@Inject
	public CytoscapeWidget(CytoscapeWidgetView view, SynapseClientAsync synapseClient,
			GlobalApplicationState globalApplicationState,
			AuthenticationController authenticationController) {
		this.view = view;
		this.synapseClient = synapseClient;
		this.globalApplicationState = globalApplicationState;
		this.authenticationController = authenticationController;
		view.setPresenter(this);
	}

	public void configure() {		
		String graphJson = getFakeJSON();
		view.configure(graphJson);
	}
	
	private String getFakeJSON() {
		return "{ \"elements\": [ { \"data\": { \"id\": \"j\", \"name\": \"Jerry\", \"weight\": 65, \"height\": 174 }, \"group\": \"nodes\" }, { \"data\": { \"id\": \"e\", \"name\": \"Elaine\", \"weight\": 48, \"height\": 160 }, \"group\": \"nodes\" }, { \"data\": { \"id\": \"k\", \"name\": \"Kramer\", \"weight\": 75, \"height\": 185 }, \"group\": \"nodes\" }, { \"data\": { \"id\": \"g\", \"name\": \"George\", \"weight\": 70, \"height\": 150 }, \"group\": \"nodes\" }, { \"data\": { \"source\": \"j\", \"target\": \"e\" }, \"group\":\"edges\" }, { \"data\": { \"source\": \"j\", \"target\": \"k\" }, \"group\":\"edges\" }, { \"data\": { \"source\": \"j\", \"target\": \"g\" }, \"group\":\"edges\" }, { \"data\": { \"source\": \"e\", \"target\": \"j\" }, \"group\":\"edges\" }, { \"data\": { \"source\": \"e\", \"target\": \"k\" }, \"group\":\"edges\" }, { \"data\": { \"source\": \"k\", \"target\": \"j\" }, \"group\":\"edges\" }, { \"data\": { \"source\": \"k\", \"target\": \"e\" }, \"group\":\"edges\" }, { \"data\": { \"source\": \"k\", \"target\": \"g\" }, \"group\":\"edges\" }, { \"data\": { \"source\": \"g\", \"target\": \"j\" }, \"group\":\"edges\" } ]}";
	}

	@Override
	public Widget asWidget() {
		view.setPresenter(this);
		return view.asWidget();
	}

}
