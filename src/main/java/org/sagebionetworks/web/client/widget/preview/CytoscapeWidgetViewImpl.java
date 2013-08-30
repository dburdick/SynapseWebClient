package org.sagebionetworks.web.client.widget.preview;

import java.util.ArrayList;
import java.util.List;

import org.sagebionetworks.web.client.ClientProperties;
import org.sagebionetworks.web.client.DisplayUtils;
import org.sagebionetworks.web.client.SageImageBundle;
import org.sagebionetworks.web.client.resources.ResourceLoader;
import org.sagebionetworks.web.client.resources.WebResource;

import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.LayoutData;
import com.extjs.gxt.ui.client.widget.layout.MarginData;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class CytoscapeWidgetViewImpl extends LayoutContainer implements CytoscapeWidgetView {

	private static final LayoutData TOP3_LEFT3 = new MarginData(3, 0, 0, 3);
	private static int sequence = 0;
	private Presenter presenter;
	private ResourceLoader resourceLoader;
	private LayoutContainer container;
	private HTML loadingContainer;
	private String graphJson;
	private boolean cytoscapeLoaded = false;
	
	@Inject
	public CytoscapeWidgetViewImpl(ResourceLoader resourceLoader, SageImageBundle sageImageBundle) {
		this.resourceLoader = resourceLoader;		
		container = new LayoutContainer();
		container.setId(getNewId());
		container.setHeight(400);
		container.setWidth(400);
		container.setBorders(true);
		this.add(container);
		
		loadingContainer = new HTML(DisplayUtils.getLoadingHtml(sageImageBundle, "Loading Cytoscape"));
		graphJson = null;
	}

	@Override
	public void configure(String graphJson) {
		this.graphJson = graphJson;
		buildGraph();
	}	

	@Override
	public void onRender(Element parent, int index) {
		super.onRender(parent, index);	
		// load Cytoscape Javascript
		List<WebResource> list = new ArrayList<WebResource>();
		list.add(ClientProperties.CYTOSCAPE_JS);
		list.add(ClientProperties.CYTOSCAPE_FACTORY_JS);
		resourceLoader.requires(list, new AsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				cytoscapeLoaded = true;
				buildGraph();
			}
			@Override
			public void onFailure(Throwable caught) {
			}
		});		
	}

	private void buildGraph() {	
		if(graphJson != null && cytoscapeLoaded) {
			JavaScriptObject graph = parseJson(graphJson);
			initCytoscape(container.getId(), graphJson);
		}
	}
	
	/*
	 * Takes in a trusted JSON String and evals it.
	 * @param JSON String that you trust
	 * @return JavaScriptObject that you can cast to an Overlay Type
	 */
	public static native JavaScriptObject parseJson(String jsonStr) /*-{
	  return eval( "(" + jsonStr + ")");
	}-*/;
	
	private static native void initCytoscape(String containerId, String graphJson) /*-{	
		function callback(cy) {
			elementsJson = "{ \"data\": { \"id\": \"d\", \"name\": \"Dave\", \"weight\": 70, \"height\": 150 }, \"group\": \"nodes\" },{ \"data\": { \"source\": \"d\", \"target\": \"e\" }, \"group\":\"edges\" }";
			$wnd.cyLoad(cy, elementsJson);
		}
		
		$wnd.getCytoscapeInstance(containerId, graphJson, callback);		
	}-*/;

	@Override
	public Widget asWidget() {
		this.layout(true);
		return this;
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void showLoading() {
		container.add(loadingContainer, TOP3_LEFT3);
		container.layout(true);
	}

	@Override
	public void clear() {
		container.removeAll();
	}

	@Override
	public void showInfo(String title, String message) {
		DisplayUtils.showInfo(title, message);
	}

	@Override
	public void showErrorMessage(String message) {
		this.removeAll();
		HTML alert = new HTML();
		alert.addStyleName("alert");
		alert.setText(message);
		this.add(alert);
		this.layout();
	}

	/*
	 * Private Methods
	 */
	private String getNewId() {
		return "elems" + ++sequence;
	}
	
}
