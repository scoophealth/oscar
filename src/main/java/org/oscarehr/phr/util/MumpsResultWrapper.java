/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. 
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */


package org.oscarehr.phr.util;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.oscarehr.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public final class MumpsResultWrapper {

	public static class Entry {
		public String questionId;
		public String questionText;
		public ArrayList<String> answers = new ArrayList<String>();
	}

	private ArrayList<Entry> entries = new ArrayList<Entry>();

	private MumpsResultWrapper() {
		// internal use only, everyone else should be using getMumpsResultWrapper()
	}

	public static MumpsResultWrapper getMumpsResultWrapper(String xml) throws IOException, SAXException, ParserConfigurationException {
		if (xml == null) return (null);

		MumpsResultWrapper mumpsResultWrapper = new MumpsResultWrapper();

		Document doc = XmlUtils.toDocument(xml);
		Node rootNode=doc.getFirstChild();
		Node resultsNode = XmlUtils.getChildNode(rootNode, "Results");

		NodeList nodeList = resultsNode.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Entry entry = getEntry(nodeList.item(i));
			if (entry != null) {
				mumpsResultWrapper.entries.add(entry);
			}
		}

		return (mumpsResultWrapper);
	}

	public ArrayList<Entry> getEntries() {
    	return entries;
    }

	private static Entry getEntry(Node node) {
		if (!"IndivoSurveyQuestion".equals(node.getNodeName())) return(null);

		Entry entry = new Entry();
		entry.questionId = XmlUtils.getChildNodeTextContents(node, "QuestionId");
		entry.questionText = XmlUtils.getChildNodeTextContents(node, "QuestionText");

		Node answerNode = XmlUtils.getChildNode(node, "QuestionAnswer");
		NodeList nodeList = answerNode.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node answerValueNode=nodeList.item(i);
			
			if (!"AnswerValue".equals(answerValueNode.getNodeName())) continue;
			
			String answer = answerValueNode.getTextContent();
			if (answer != null) {
				entry.answers.add(answer);
			}
		}

		return(entry);
	}
	
	/**
	 * @return the entry or null if not exist
	 */
	public Entry getEntryByQuestionId(String questionId)
	{
		for (Entry entry : entries)
		{
			if (questionId.equals(entry.questionId)) return(entry);
		}
		
		return(null);
	}
	
	public ArrayList<String> getAnswersByQuestionId(String questionId)
	{
		Entry entry=getEntryByQuestionId(questionId);
		
		if (entry==null) return(null);
		else return(entry.answers);
	}

	public String getFirstAnswerByQuestionId(String questionId)
	{
		ArrayList<String> answers=getAnswersByQuestionId(questionId);

		if (answers==null || answers.size()<1) return(null);
		else return(answers.get(0));
	}
}
