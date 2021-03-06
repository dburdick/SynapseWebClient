package org.sagebionetworks.web.unitclient.widget.breadcrumb;

import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNotNull;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.management.RuntimeErrorException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.sagebionetworks.ResourceEncoder;
import org.sagebionetworks.ResourceUtils;
import org.sagebionetworks.repo.model.Entity;
import org.sagebionetworks.repo.model.EntityHeader;
import org.sagebionetworks.repo.model.EntityPath;
import org.sagebionetworks.repo.model.RegisterConstants;
import org.sagebionetworks.repo.model.Study;
import org.sagebionetworks.schema.adapter.AdapterFactory;
import org.sagebionetworks.schema.adapter.JSONObjectAdapter;
import org.sagebionetworks.schema.adapter.JSONObjectAdapterException;
import org.sagebionetworks.schema.adapter.org.json.AdapterFactoryImpl;
import org.sagebionetworks.schema.adapter.org.json.JSONObjectAdapterImpl;
import org.sagebionetworks.web.client.EntitySchemaCache;
import org.sagebionetworks.web.client.EntitySchemaCacheImpl;
import org.sagebionetworks.web.client.EntityTypeProvider;
import org.sagebionetworks.web.client.GlobalApplicationState;
import org.sagebionetworks.web.client.SynapseClientAsync;
import org.sagebionetworks.web.client.security.AuthenticationController;
import org.sagebionetworks.web.client.transform.NodeModelCreator;
import org.sagebionetworks.web.client.widget.breadcrumb.Breadcrumb;
import org.sagebionetworks.web.client.widget.breadcrumb.BreadcrumbView;
import org.sagebionetworks.web.server.servlet.SynapseClientImpl;
import org.sagebionetworks.web.shared.EntityWrapper;
import org.sagebionetworks.web.test.helper.AsyncMockStubber;
import org.sagebionetworks.web.unitclient.RegisterConstantsStub;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class BreadcrumbTest {
		
	Breadcrumb breadcrumb;
	BreadcrumbView mockView;
	NodeModelCreator mockNodeModelCreator;
	AuthenticationController mockAuthenticationController;
	GlobalApplicationState mockGlobalApplicationState;
	SynapseClientAsync mockSynapseClient;
	EntityTypeProvider entityTypeProvider;
	
	@SuppressWarnings("unchecked")
	@Before
	public void setup() throws UnsupportedEncodingException, JSONObjectAdapterException{		
		mockView = Mockito.mock(BreadcrumbView.class);
		mockNodeModelCreator = mock(NodeModelCreator.class);
		mockAuthenticationController = mock(AuthenticationController.class);
		mockGlobalApplicationState = mock(GlobalApplicationState.class);
		mockSynapseClient = mock(SynapseClientAsync.class);
		
		String registryJson = SynapseClientImpl.getEntityTypeRegistryJson();
		AsyncMockStubber.callSuccessWith(registryJson).when(mockSynapseClient).getEntityTypeRegistryJSON(any(AsyncCallback.class));
		// Load the register from the classpath.
		
		entityTypeProvider = new EntityTypeProvider(new RegisterConstantsStub(), new AdapterFactoryImpl(), new EntitySchemaCacheImpl(new AdapterFactoryImpl()));		
		
		breadcrumb = new Breadcrumb(mockView, mockSynapseClient, mockGlobalApplicationState, mockAuthenticationController, mockNodeModelCreator, entityTypeProvider);
		
		
		verify(mockView).setPresenter(breadcrumb);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testAsWidgetEntity() throws Exception {
		Entity entity = new Study();
		entity.setId("3");
		entity.setUri("path/dataset/3");
		List<EntityHeader> pathHeaders = new ArrayList<EntityHeader>();
		
		EntityHeader rootHeader = new EntityHeader();
		rootHeader.setId("1");
		rootHeader.setName("root");
		pathHeaders.add(rootHeader);
		
		EntityHeader projHeader = new EntityHeader();
		projHeader.setId("2");
		projHeader.setName("project");
		pathHeaders.add(projHeader);
		
		EntityHeader dsHeader = new EntityHeader();
		dsHeader.setId("3");
		dsHeader.setName("ds");
		pathHeaders.add(dsHeader);
		
		EntityPath entityPath = new EntityPath();
		entityPath.setPath(pathHeaders);
		JSONObjectAdapter pathAdapter = new JSONObjectAdapterImpl();
		entityPath.writeToJSONObject(pathAdapter);
				
		EntityWrapper entityWrapper = new EntityWrapper(pathAdapter.toJSONString(), EntityPath.class.getName(), null);	
		
		// Fail path service call
//		reset(mockView);		
//		AsyncMockStubber.callFailureWith(new Throwable("error message")).when(mockSynapseClient).getEntityPath(eq(entity.getId()), any(AsyncCallback.class)); // fail for get Path
//		when(mockNodeModelCreator.createEntity(any(EntityWrapper.class), eq(EntityPath.class))).thenReturn(entityPath);
//		breadcrumb.asWidget(entityPath);
//		verify(mockView).showErrorMessage(anyString());
		
		// fail model creation
		reset(mockView);			
		AsyncMockStubber.callSuccessWith(entityWrapper).when(mockSynapseClient).getEntityPath(eq(entity.getId()), any(AsyncCallback.class));
		when(mockNodeModelCreator.createEntity(any(EntityWrapper.class), eq(EntityPath.class))).thenReturn(null); // null model return
		breadcrumb.asWidget(null);
		verify(mockView).setLinksList(any(List.class), (String)isNull());
		
		// success test
		reset(mockView);			
		AsyncMockStubber.callSuccessWith(entityWrapper).when(mockSynapseClient).getEntityPath(eq(entity.getId()), any(AsyncCallback.class));
		when(mockNodeModelCreator.createEntity(entityWrapper, EntityPath.class)).thenReturn(entityPath);
		breadcrumb.asWidget(entityPath);
		verify(mockView).setLinksList(any(List.class), (String)isNotNull());				
	}
	
	@Test
	public void testAsWidget(){
		assertNull(breadcrumb.asWidget());
	}
	
	
}
