package org.sagebionetworks.web.client;

import org.sagebionetworks.web.shared.users.UserData;
import org.sagebionetworks.web.shared.users.UserRegistration;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface UserAccountServiceAsync {

	void sendPasswordResetEmail(String emailAddress, AsyncCallback<Void> callback);
	
	void sendSetApiPasswordEmail(String emailAddress, AsyncCallback<Void> callback);

	void setPassword(String email, String newPassword, AsyncCallback<Void> callback);

	/**
	 * This needs to be replaced with a Synapse Java Client call
	 */
	@Deprecated
	void initiateSession(String username, String password, boolean explicitlyAcceptsTermsOfUse, AsyncCallback<UserData> callback);

	/**
	 * This needs to be replaced with a Synapse Java Client call
	 */
	@Deprecated
	void getUser(String sessionToken, AsyncCallback<UserData> callback);	

	void createUser(UserRegistration userInfo, AsyncCallback<Void> callback);
	
	/**
	 * This needs to be replaced with a Synapse Java Client call
	 */
	@Deprecated
	void updateUser(String firstName, String lastName, String displayName, AsyncCallback<Void> callback);

	void terminateSession(String sessionToken, AsyncCallback<Void> callback);

	/**
	 * This needs to be replaced with a Synapse Java Client call
	 */
	@Deprecated
	void ssoLogin(String sessionToken, AsyncCallback<Boolean> callback);

	void getPrivateAuthServiceUrl(AsyncCallback<String> callback);

	void getPublicAuthServiceUrl(AsyncCallback<String> callback);
	
	void getSynapseWebUrl(AsyncCallback<String> callback);
	
	/**
	 * This needs to be replaced with a Synapse Java Client call
	 */
	@Deprecated
	void getTermsOfUse(AsyncCallback<String> callback);

}
