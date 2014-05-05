package com.EDA397.Navigator.NaviGitator.Activities;

import android.os.AsyncTask;
import android.util.Log;
import com.EDA397.Navigator.NaviGitator.Datatypes.PivotalProject;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

/**
 * Created by sajfer on 2014-05-01.
 */
public class PivotalFunctionality {

    private static PivotalFunctionality instance;
    private String url = "https://www.pivotaltracker.com/services/v3";
    private String token = "";

    private PivotalFunctionality() {

    }

    /**
     * Return the current instance of PivotalFunctionality
     * @return The current instance of PivotalFunctionality
     */
    public static PivotalFunctionality getInstance() {
        if (instance == null) {
            instance = new PivotalFunctionality();
            Log.d("PivotalFunctionality", "Instance Created");
        }
        Log.d("PivotalFunctionality", "Instance Returned");
        return instance;
    }

    /**
     * Executing the asynctask for pivotal login
     * @param userName
     * @param password
     * @return boolean if user is logged into pivotaltracker
     */
    public Boolean pivotalLogin(String userName, String password) {
        try{
            Log.d("PivotalFunctionality", "Login");
            LoginPivotal task = new LoginPivotal();
            task.execute(userName, password);
            return task.get();
        } catch ( Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Executing the asynctask for get pivotaltracker projects
     * @return boolean if getting the pivotaltracker projects are successful
     */
    public List<PivotalProject> getPivotalProjects() {
        try{
            Log.d("PivotalFunctionality", "getprojects");
            getProjects task = new getProjects();
            task.execute();
            return task.get();
        } catch ( Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<PivotalProject> parseXML(String xml) {
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

    /**
     * Executing the asynctask for get pivotaltracker stories
     * @return boolean if getting the pivotaltracker stories are successful
     */
    public Boolean getPivotalStories() {
        try{
            Log.d("PivotalFunctionality", "getprojects");
            getStories task = new getStories();
            task.execute();
            return task.get();
        } catch ( Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Async task to Authenticate a user against Pivotal tracker
     */
    private class LoginPivotal extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... str) {
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost post = new HttpPost(url + "/tokens/active");
                List<NameValuePair> np = new ArrayList<NameValuePair>();
                np.add(new BasicNameValuePair("username", str[0]));
                np.add(new BasicNameValuePair("password", str[1]));

                post.setEntity(new UrlEncodedFormEntity(np));

                //Response
                HttpResponse resp = httpclient.execute(post);
                Log.d("PivotalFunctionality", "RESPONSE CODE" + resp.getStatusLine().getStatusCode());
                String responseString = new BasicResponseHandler().handleResponse(resp);
                int start = responseString.indexOf("<guid>")+6;
                int end = responseString.indexOf("</guid>");
                token = responseString.substring(start, end);
                if(resp.getStatusLine().getStatusCode() == 200) {
                    Log.d("PivotalFunctionality", "Token: " + token);
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("PivotalFunctionality", "Login failed");
                return false;
            }
        }
    }

    /**
     * Async task to get the projects from pivotaltracker
     */
    private class getProjects extends AsyncTask<Void, Void, List<PivotalProject>> {


        @Override
        protected List<PivotalProject> doInBackground(Void... arg0) {
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet get = new HttpGet(url + "/projects");
                get.setHeader("X-TrackerToken", token);

                //Response
                HttpResponse resp = httpclient.execute(get);

                String responseString = new BasicResponseHandler().handleResponse(resp);

                Log.d("PivotalFunctionality", "RESPONSE FROM GETPROJECTS \n " + responseString);
                return parseXML(responseString);
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("PivotalFunctionality", "failed get projects");
                return null;
            }
        }
    }

    /**
     * Async task to get the stories from pivotaltracker
     */
    private class getStories extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... arg0) {
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet get = new HttpGet(url + "/projects/1043912/stories");
                get.setHeader("X-TrackerToken", token);

                //Response
                HttpResponse resp = httpclient.execute(get);
                String responseString = new BasicResponseHandler().handleResponse(resp);

                Log.d("PivotalFunctionality", "RESPONSE GETSTORIES " + responseString);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("PivotalFunctionality", "failed get stories");
                return null;
            }
        }
    }

}
