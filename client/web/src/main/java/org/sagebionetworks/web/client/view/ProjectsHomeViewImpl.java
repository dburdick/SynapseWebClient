package org.sagebionetworks.web.client.view;

import org.sagebionetworks.web.client.DisplayUtils;
import org.sagebionetworks.web.client.IconsImageBundle;
import org.sagebionetworks.web.client.SageImageBundle;
import org.sagebionetworks.web.client.events.CancelEvent;
import org.sagebionetworks.web.client.events.CancelHandler;
import org.sagebionetworks.web.client.events.EntityUpdatedEvent;
import org.sagebionetworks.web.client.events.EntityUpdatedHandler;
import org.sagebionetworks.web.client.widget.editpanels.NodeEditor;
import org.sagebionetworks.web.client.widget.footer.Footer;
import org.sagebionetworks.web.client.widget.header.Header;
import org.sagebionetworks.web.client.widget.header.Header.MenuItems;
import org.sagebionetworks.web.client.widget.table.QueryServiceTable;
import org.sagebionetworks.web.client.widget.table.QueryServiceTableResourceProvider;
import org.sagebionetworks.web.client.widget.table.QueryTableFactory;
import org.sagebionetworks.web.shared.NodeType;
import org.sagebionetworks.web.shared.QueryConstants.ObjectType;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class ProjectsHomeViewImpl extends Composite implements ProjectsHomeView {

	public interface ProjectsHomeViewImplUiBinder extends UiBinder<Widget, ProjectsHomeViewImpl> {}

	@UiField
	SimplePanel header;
	@UiField
	SimplePanel footer;
	@UiField
	SimplePanel tablePanel;
	@UiField
	SimplePanel createProjectButtonPanel;
		
	private Presenter presenter;
	private QueryServiceTable queryServiceTable;
	private QueryServiceTableResourceProvider queryServiceTableResourceProvider;
	private IconsImageBundle icons;
	private NodeEditor nodeEditor;
	private Header headerWidget;
	private Window startProjectWindow;
	private QueryTableFactory queryTableFactory;

	private final int INITIAL_QUERY_TABLE_OFFSET = 0;
	private final int QUERY_TABLE_LENGTH = 20;
	
	@Inject
	public ProjectsHomeViewImpl(ProjectsHomeViewImplUiBinder binder,
			Header headerWidget, Footer footerWidget, IconsImageBundle icons,
			SageImageBundle imageBundle,
			QueryServiceTableResourceProvider queryServiceTableResourceProvider,
			final NodeEditor nodeEditor, QueryTableFactory tableProvider) {		
		initWidget(binder.createAndBindUi(this));

		this.queryServiceTableResourceProvider = queryServiceTableResourceProvider;
		this.icons = icons;
		this.nodeEditor = nodeEditor;
		this.headerWidget = headerWidget;
		this.queryTableFactory = tableProvider;
		
		header.add(headerWidget.asWidget());
		footer.add(footerWidget.asWidget());
		headerWidget.setMenuItemActive(MenuItems.PROJECTS);

	}


	@Override
	public void setPresenter(final Presenter presenter) {
		this.presenter = presenter;		
		headerWidget.refresh();
				
		addQueryTable();
		
		Button createProjectButton = new Button("Start a Project", AbstractImagePrototype.create(icons.addSquare16()));
		createProjectButton.addSelectionListener(new SelectionListener<ButtonEvent>() {			
			@Override
			public void componentSelected(ButtonEvent ce) {								
				startProjectWindow = new Window();  
				startProjectWindow.setSize(600, 240);
				startProjectWindow.setPlain(true);
				startProjectWindow.setModal(true);
				startProjectWindow.setBlinkModal(true);
				startProjectWindow.setHeading("Start a Project");
				startProjectWindow.setLayout(new FitLayout());								
				nodeEditor.addCancelHandler(new CancelHandler() {					
					@Override
					public void onCancel(CancelEvent event) {
						startProjectWindow.hide();
					}
				});
				nodeEditor.addPersistSuccessHandler(new EntityUpdatedHandler() {					
					@Override
					public void onPersistSuccess(EntityUpdatedEvent event) {
						startProjectWindow.hide();
						queryServiceTable.refreshFromServer();
					}
				});
				nodeEditor.setPlaceChanger(presenter.getPlaceChanger());
				startProjectWindow.add(nodeEditor.asWidget(NodeType.PROJECT), new FitData(4));						
				startProjectWindow.show();			
			}
		});
		createProjectButtonPanel.clear();
		createProjectButtonPanel.add(createProjectButton);		
	}

	private void addQueryTable() {
		this.queryServiceTable = new QueryServiceTable(queryServiceTableResourceProvider, ObjectType.project, true, 1000, 487, presenter.getPlaceChanger());		
		// Start on the first page and trigger a data fetch from the server
		queryServiceTable.pageTo(INITIAL_QUERY_TABLE_OFFSET, QUERY_TABLE_LENGTH);
		tablePanel.clear();
		tablePanel.add(queryServiceTable.asWidget());		
	}

	

	@Override
	public void showErrorMessage(String message) {
		DisplayUtils.showErrorMessage(message);
	}

	@Override
	public void showLoading() {
	}


	@Override
	public void showInfo(String title, String message) {
		DisplayUtils.showInfo(title, message);
	}


	@Override
	public void clear() {
		if(startProjectWindow != null) startProjectWindow.hide();
	}

}
