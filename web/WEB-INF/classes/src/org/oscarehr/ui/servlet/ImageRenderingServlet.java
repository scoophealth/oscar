package org.oscarehr.ui.servlet;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.SocketException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager;
import org.oscarehr.casemgmt.dao.ClientImageDAO;
import org.oscarehr.common.model.Facility;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.SessionConstants;
import org.oscarehr.util.SpringUtils;

/**
 * This servlet requires a parameter called "source" which should signify where to get the image from. Examples include source=local_client, or source=hnr_client. Depending on the source, you may optionally need more parameters, as examples a local_client
 * may need a clientId=5 or a hnr_client may need linkingId=3. <br />
 * <br />
 * The structure of this class follows the structure of the Servlet class itself in the pattern of the service() -> (doPost/doGet/doDelete), from the doGet we fork to each specific source processor. <br />
 * <br />
 * This servlet assumes the image exists, for the most part this servlet is a "drop in" replacement for serving images from the HD directly, i.e. things like existence and appropriateness of the image should have already been checked. In general security
 * should also be checked before hand, we also check again here as security is a special case.
 */
public class ImageRenderingServlet extends HttpServlet {
	private static Logger logger = LogManager.getLogger(ImageRenderingServlet.class);
	private ClientImageDAO clientImageDAO = (ClientImageDAO) SpringUtils.getBean("clientImageDAO");
	private CaisiIntegratorManager caisiIntegratorManager=(CaisiIntegratorManager)SpringUtils.getBean("caisiIntegratorManager");
	
	public final void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			String source = request.getParameter("source");

			// for the most part each sub renderer is responsible for everything including
			// security checks. There's actually not too much point in having a shared
			// servlet except to save a little bit of work on registering servlets
			// and a little processing logic.
			if ("local_client".equals(source)) {
				renderLocalClient(request, response);
			} else if ("hnr_client".equals(source)) {
				renderHnrClient(request, response);
			} else {
				throw (new IllegalArgumentException("Unknown source type : " + source));
			}
		} catch (Exception e) {
			if (e.getCause() instanceof SocketException)
			{
				logger.warn("An error we can't handle that's expected infrequently. "+e.getMessage());
			}
			else
			{
				logger.error("Unexpected error. qs=" + request.getQueryString(), e);
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}
		}
	}

	/**
	 * This convenience method is only suitable for small images as image is obviously not streamed since it's passed in.
	 * 
	 * @param response
	 * @param image
	 * @param imageType image sub type of the contentType, i.e. "jpeg" "png"
	 * @throws IOException
	 */
	private static final void renderImage(HttpServletResponse response, byte[] image, String imageType) throws IOException {
		response.setContentType("image/" + imageType);
		response.setContentLength(image.length);
		BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
		bos.write(image);
		bos.flush();
	}

	private void renderHnrClient(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// this expects linkingId as a parameter

		// security check
		HttpSession session = request.getSession();
		Provider provider = (Provider) session.getAttribute(SessionConstants.LOGGED_IN_PROVIDER);
		if (provider == null) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return;
		}

		// get image
		Facility loggedInFacility=(Facility)session.getAttribute(SessionConstants.CURRENT_FACILITY);
		Integer linkingId=Integer.parseInt(request.getParameter("linkingId"));
		org.oscarehr.hnr.ws.client.Client hnrClient=caisiIntegratorManager.getHnrClient(loggedInFacility, provider, linkingId);

		if (hnrClient != null) {
			renderImage(response, hnrClient.getImage(), "jpeg");
		} else {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}

	private void renderLocalClient(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// this expects clientId as a parameter

		// security check
		HttpSession session = request.getSession();
		Provider provider = (Provider) session.getAttribute(SessionConstants.LOGGED_IN_PROVIDER);
		if (provider == null) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return;
		}

		// get image
		org.oscarehr.casemgmt.model.ClientImage clientImage = clientImageDAO.getClientImage(Integer.parseInt(request.getParameter("clientId")));
		if (clientImage != null && "jpg".equals(clientImage.getImage_type())) {
			renderImage(response, clientImage.getImage_data(), "jpeg");
		} else {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}

}
