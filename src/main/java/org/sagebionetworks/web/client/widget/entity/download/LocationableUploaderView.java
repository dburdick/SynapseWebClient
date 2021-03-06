package org.sagebionetworks.web.client.widget.entity.download;

import org.sagebionetworks.web.client.widget.SynapseWidgetView;

import com.google.gwt.user.client.ui.IsWidget;

public interface LocationableUploaderView extends IsWidget, SynapseWidgetView {

	/**
	 * Set the presenter.
	 * @param presenter
	 */
	public void setPresenter(Presenter presenter);

	public void createUploadForm(boolean showCancel);
	
	/**
	 * Presenter interface
	 */
	public interface Presenter {

		String getUploadActionUrl();

		void setExternalLocation(String path);
		
		public void closeButtonSelected();

		void handleSubmitResult(String resultHtml);

	}


}
