package org.apache.xml.security.encryption;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.codec.binary.Base64;
import org.apache.xml.security.utils.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Converts <code>String</code>s into <code>Node</code>s and visa versa.
 */
public class DocumentSerializer extends AbstractSerializer {

	/**
	 * @param source
	 * @param ctx
	 * @return the Node resulting from the parse of the source
	 * @throws XMLEncryptionException
	 */
	public Node deserialize(byte[] source, Node ctx)
			throws XMLEncryptionException {
		try {
			return attemptDeserialize(source, ctx);
		} catch (Exception e) {
			return attemptDeserialize(Base64.encodeBase64(source), ctx);
		}
	}

	private Node attemptDeserialize(byte[] source, Node ctx)
			throws XMLEncryptionException {
		byte[] fragment = createContext(source, ctx);
		return deserialize(ctx, new InputSource(new ByteArrayInputStream(
				fragment)));
	}

	/**
	 * @param source
	 * @param ctx
	 * @return the Node resulting from the parse of the source
	 * @throws XMLEncryptionException
	 */
	@Deprecated
	public Node deserialize(String source, Node ctx)
			throws XMLEncryptionException {
		String fragment = createContext(source, ctx);
		return deserialize(ctx, new InputSource(new StringReader(fragment)));
	}

	/**
	 * @param ctx
	 * @param inputSource
	 * @return the Node resulting from the parse of the source
	 * @throws XMLEncryptionException
	 */
	private Node deserialize(Node ctx, InputSource inputSource)
			throws XMLEncryptionException {
		try {
			DocumentBuilder db = XMLUtils.createDocumentBuilder(false,
					secureValidation);
			Document d = db.parse(inputSource);

			Document contextDocument = null;
			if (Node.DOCUMENT_NODE == ctx.getNodeType()) {
				contextDocument = (Document) ctx;
			} else {
				contextDocument = ctx.getOwnerDocument();
			}

			Element fragElt = (Element) contextDocument.importNode(
					d.getDocumentElement(), true);
			DocumentFragment result = contextDocument.createDocumentFragment();
			Node child = fragElt.getFirstChild();
			while (child != null) {
				fragElt.removeChild(child);
				result.appendChild(child);
				child = fragElt.getFirstChild();
			}
			return result;
		} catch (SAXException se) {
			throw new XMLEncryptionException("empty", se);
		} catch (ParserConfigurationException pce) {
			throw new XMLEncryptionException("empty", pce);
		} catch (IOException ioe) {
			throw new XMLEncryptionException("empty", ioe);
		}
	}

}
