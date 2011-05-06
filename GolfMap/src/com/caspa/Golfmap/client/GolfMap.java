package com.caspa.Golfmap.client;

//ddsfkkk


import com.caspa.Golfmap.client.Percorso.Buca;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.maps.client.InfoWindow;
import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.MapType;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.client.event.MapClickHandler;
import com.google.gwt.maps.client.event.MarkerClickHandler;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.geom.Point;
import com.google.gwt.maps.client.overlay.Icon;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.MarkerOptions;
import com.google.gwt.maps.client.overlay.Polyline;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.VerticalPanel;

/*
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class GolfMap implements EntryPoint {
	
  private MapWidget map;
  //TODO creare una costante numero buche per gli array
  private Marker[] arrMarkerPart = new Marker[9];
  private Marker[] arrMarkerPin = new Marker[9];
  private Polyline[] arrTracciati = new Polyline[9];
  private Percorso perc;
  private LatLng bormioGolf;
	
  // GWT module entry point method.
  public void onModuleLoad() {
	  
	   // chiave per caspapp.appspot.com
	   Maps.loadMapsApi("ABQIAAAASz2iIwsaW-4vfeDk4QNZZRQnYQ7qT6hrDtBCwEzlVx9INDoz7BSTvVx9k1ZGMkiyAkKWfHRiWcwjhg", "2", false, new Runnable() {
		      public void run() {
		        buildUi();
		      }
		    });  

  }
  
  
  
  public void buildUi(){
	  	
	    HorizontalPanel mainPanel = new HorizontalPanel();
	    VerticalPanel buttonsPanel = new VerticalPanel();
	    String [] buche = new String[9];
	    final Label latlong = new Label();
	    	    
	    perc=new Percorso();
	    perc.caricaPercorso();
	    
	    for (int i = 0; i < 9; i++) {
			buche[i]="buca "+(i+1);
		}
	    
	    //aggiungo i bottoni
	    for (String buca : buche) {
	    	
	    	final ToggleButton button = new ToggleButton(buca);
	    	button.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					
					if(!button.isDown())
					{
						nascondiBuca(button.getText());
					}
					else
					{
						mostraBuca(button.getText());
						
					}
					
				}

			});
	    	
			buttonsPanel.add(button);
		}
	    
	    //sistemo la mappa
	    //final MapWidget map = new MapWidget();
	    map=new MapWidget();
	    
	    //bormio golf latlong
	    bormioGolf = LatLng.newInstance(46.4730,10.3623);
	    map.panTo(bormioGolf);
	    map.setZoomLevel(17);
	    
	    //set satellite as default
	    map.addMapType(MapType.getSatelliteMap());
	    map.removeMapType(MapType.getNormalMap());
	    
	    map.setSize("550px", "550px");
	    map.setUIToDefault();
	    
	    
	    //creo marker onclick, per sviluppo
	    //TODO da rimuovere
	    /*map.addMapClickHandler(new MapClickHandler() {
	        public void onClick(MapClickEvent event) {
	        	LatLng point = event.getLatLng();
	        	
	        	Marker partenza = creaMarker(point);
	        	
	        	map.addOverlay(partenza);
	        }
	    });
	    */
	    buttonsPanel.add(latlong);
	    mainPanel.add(map);
	    mainPanel.add(buttonsPanel);

	    
	    
	    RootPanel.get("module_container").add(mainPanel);
  }
  
  public void nascondiBuca(String buca){
	  //TODO migliorare riferimento marker
	  String n = buca.replace("buca ", "");
	  int i = Integer.parseInt(n)-1;
	  
	  map.removeOverlay(arrMarkerPart[i]);
	  map.removeOverlay(arrMarkerPin[i]);
	  map.removeOverlay(arrTracciati[i]);
	  map.panTo(bormioGolf);
  }

  public void mostraBuca(String buca){
	  Buca b = perc.new Buca();
	  b = perc.getBuca(buca);
	  String n = buca.replace("buca ", "");
	  int i = Integer.parseInt(n)-1;

	  //Window.alert(b.nome);
	  arrMarkerPart[i]=creaPartenza(b);
	  arrMarkerPin[i]=creaPin(b);
	  LatLng puntoMedio = LatLng.newInstance((b.latitudineP+b.latitudineBuca)/2, (b.longitudineP+b.longitudineBuca)/2);
	  LatLng [] arpoints={LatLng.newInstance(b.latitudineP, b.longitudineP),puntoMedio,LatLng.newInstance(b.latitudineBuca, b.longitudineBuca)};
	  
	  arrTracciati[i] = new Polyline(arpoints);
	  
	  map.panTo(puntoMedio);
	  map.addOverlay(arrTracciati[i]);
	  map.addOverlay(arrMarkerPart[i]);
	  map.addOverlay(arrMarkerPin[i]);
	  
  }

  public Marker creaPin(final Buca buca){
	  
	  final Marker m;

	  MarkerOptions opzioniMarker = MarkerOptions.newInstance();
	  //Icon ic = Icon.newInstance();
	  //ic.setImageURL("pin.png");
	  //opzioniMarker.setIcon(ic);
	  LatLng point = LatLng.newInstance(buca.latitudineBuca, buca.longitudineBuca);
	  m = new Marker(point,opzioniMarker);

	  m.addMarkerClickHandler(new MarkerClickHandler() {
		  public void onClick(MarkerClickEvent event) {
			  InfoWindow info = map.getInfoWindow();
			  info.open(m,new InfoWindowContent(
        		  "<h3>"+buca.nome+"</h3>"+
        		  "<img src=\"img/buca1.jpg\"/><br>"+
        		  "<b>metri:"+buca.metri+"</b><br>"+
        		  "<p class=\"testoInfo\">"+buca.testo+"</p>"
        		  
        		  ));
		  }
      });

	  
	  return m;
  }
  
  public Marker creaPartenza(final Buca buca){
	  
	  final Marker m;

	  MarkerOptions opzioniMarker = MarkerOptions.newInstance();
	  Icon ic = Icon.newInstance();
	  ic.setImageURL("img/golfer.png");
	  ic.setIconAnchor(Point.newInstance(16, 16));
	  opzioniMarker.setIcon(ic);
	  
	  LatLng point = LatLng.newInstance(buca.latitudineP, buca.longitudineP);
	  
	  m = new Marker(point,opzioniMarker);
	  
	  return m;
  }
  
  public Marker creaMarker(LatLng point){
	  
	  //MarkerOptions opzioniMarker = MarkerOptions.newInstance();
	  //Icon ic = Icon.newInstance();
	  //ic.setImageURL("pin.png");
	  //opzioniMarker.setIcon(ic);
	  
	  final Marker m = new Marker(point);
	  final LatLng p = point;
	  m.addMarkerClickHandler(new MarkerClickHandler() {
	      public void onClick(MarkerClickEvent event) {
	          InfoWindow info = map.getInfoWindow();
	          info.open(m,new InfoWindowContent(p.toString()));
	        }
	      });
	  return m;
  }
  
}