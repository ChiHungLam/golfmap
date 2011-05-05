package com.caspa.Golfmap.client;


//import java.util.ArrayList;


import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Window;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

public class Percorso {
	
	public class Buca{
		public String nome;
		public String metri;
		public int par;
		public double latitudineP;
		public double longitudineP;
		public double latitudineBuca;
		public double longitudineBuca;
		public String testo;
	}
	
	public Buca[] arrayBuca = new Buca[9]; 

	public void caricaPercorso(){
		  RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET,
				  "bormioGolf.xml?p="+Math.random());
	    try {
	      requestBuilder.sendRequest(null, new RequestCallback() {
	        public void onError(Request request, Throwable exception) {
	          //TODO gestire errore
	        }
	       public void onResponseReceived(Request request, Response response) {
	    	  parsaXML(response.getText());
	       }

	      });
	    } catch (RequestException ex) {
	      Window.alert(ex.toString());
	    }
	}
	
	private Buca[] parsaXML(String xml) {
		
		Document doc = XMLParser.parse(xml);
		Element el = doc.getDocumentElement();
		XMLParser.removeWhitespace(el);
		NodeList buche = el.getElementsByTagName("buca");

		
		for (int i = 0; i < buche.getLength(); i++) {
		//for (int i = 0; i < 1; i++) {
			
			Buca b = new Buca();
			Element buca = (Element) buche.item(i);
			XMLParser.removeWhitespace(buca);
			//result=result+" - "+buca.getFirstChild().getNodeName()+":"+
			b.nome=buca.getElementsByTagName("nome").item(0).getFirstChild().getNodeValue();
			b.metri=buca.getElementsByTagName("metri").item(0).getFirstChild().getNodeValue();
			b.par=Integer.parseInt(buca.getElementsByTagName("par").item(0).getFirstChild().getNodeValue());
			b.latitudineP=Double.parseDouble(buca.getElementsByTagName("latitudineP").item(0).getFirstChild().getNodeValue());
			b.longitudineP=Double.parseDouble(buca.getElementsByTagName("longitudineP").item(0).getFirstChild().getNodeValue());
			b.latitudineBuca=Double.parseDouble(buca.getElementsByTagName("latitudineBuca").item(0).getFirstChild().getNodeValue());
			b.longitudineBuca=Double.parseDouble(buca.getElementsByTagName("longitudineBuca").item(0).getFirstChild().getNodeValue());
			b.testo=buca.getElementsByTagName("testo").item(0).getFirstChild().getNodeValue();
			
			//inserisco la buca nell'array
			arrayBuca[i]=b;
		}
		
		//Window.alert(Double.toString(arrayBuca[1].latitudine));
		return arrayBuca;
	}
	
	public Buca getBuca(String nomeBuca){
		Buca res=new Buca();
		for (Buca b : arrayBuca) {
			if(nomeBuca.equals(b.nome))
			{
				res=b;
			}
		}
		return res;
	}
	
}
