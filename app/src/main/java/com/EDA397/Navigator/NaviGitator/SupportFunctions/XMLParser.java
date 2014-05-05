package com.EDA397.Navigator.NaviGitator.SupportFunctions;

import android.util.Log;

import com.EDA397.Navigator.NaviGitator.Datatypes.PivotalProject;
import com.EDA397.Navigator.NaviGitator.Datatypes.PivotalStory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by sajfer on 2014-05-05.
 */
public class XMLParser {
    public static List<PivotalProject> parseProjects(String xml) {
        List<PivotalProject> projects = new ArrayList<PivotalProject>();
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(new InputSource(new StringReader(xml)));

            // normalize text representation
            doc.getDocumentElement().normalize();
            System.out.println("Root element of the doc is " + doc.getDocumentElement().getNodeName());

            NodeList listOfProjects = doc.getElementsByTagName("project");
            int totalProjects = listOfProjects.getLength();
            Log.d("PivotalFunctionality", "Total no of projects : " + totalProjects);

            for (int i = 0; i < listOfProjects.getLength(); i++) {

                Node firstProjectNode = listOfProjects.item(i);
                if (firstProjectNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element firstElement = (Element) firstProjectNode;

                    NodeList nameList = firstElement.getElementsByTagName("name");
                    NodeList idList = firstElement.getElementsByTagName("id");

                    Element nameElement = (Element) nameList.item(0);
                    Element idElement = (Element) idList.item(0);

                    NodeList textNameList = nameElement.getChildNodes();
                    NodeList textIdList = idElement.getChildNodes();

                    String name = ((Node) textNameList.item(0)).getNodeValue().trim();
                    Integer id = Integer.parseInt(((Node) textIdList.item(0)).getNodeValue().trim());
                    Log.d("PivotalFunctionality","title : " + name);
                    Log.d("PivotalFunctionality","id : " + id);
                    projects.add(new PivotalProject(name, id));
                }
            }//end of for loop with s var
            return projects;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static List<PivotalStory> parseStories(String xml) {
        List<PivotalStory> projects = new ArrayList<PivotalStory>();
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(new InputSource(new StringReader(xml)));

            // normalize text representation
            doc.getDocumentElement().normalize();
            System.out.println("Root element of the doc is " + doc.getDocumentElement().getNodeName());

            NodeList listOfProjects = doc.getElementsByTagName("project");
            int totalProjects = listOfProjects.getLength();
            Log.d("PivotalFunctionality","Total no of projects : " + totalProjects);

            for (int i = 0; i < listOfProjects.getLength(); i++) {

                Node firstProjectNode = listOfProjects.item(i);
                if (firstProjectNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element firstElement = (Element) firstProjectNode;

                    NodeList nameList = firstElement.getElementsByTagName("name");
                    NodeList idList = firstElement.getElementsByTagName("id");

                    Element nameElement = (Element) nameList.item(0);
                    Element idElement = (Element) idList.item(0);

                    NodeList textNameList = nameElement.getChildNodes();
                    NodeList textIdList = idElement.getChildNodes();

                    String name = ((Node) textNameList.item(0)).getNodeValue().trim();
                    Integer id = Integer.parseInt(((Node) textIdList.item(0)).getNodeValue().trim());
                    Log.d("PivotalFunctionality","title : " + name);
                    Log.d("PivotalFunctionality","id : " + id);
                    //projects.add(new PivotalStory(name, id));
                }
            }//end of for loop with s var
            return projects;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
