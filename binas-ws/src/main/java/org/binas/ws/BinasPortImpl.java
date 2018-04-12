package org.binas.ws;

import java.util.Collection;
import java.util.List;

import javax.jws.WebService;

import org.binas.domain.BinasManager;
import org.binas.station.ws.cli.StationClient;

import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDIRecord;


/**
 * This class implements the Web Service port type (interface). The annotations
 * below "map" the Java class to the WSDL definitions.
 */
 //TODO
@WebService(endpointInterface = "org.binas.ws.BinasPortType",
wsdlLocation = "binas.1_0.wsdl",
name ="BinasWebService",
portName = "BinasPort",
targetNamespace="http://ws.binas.org/",
serviceName = "BinasService"
)


public class BinasPortImpl implements BinasPortType {

	/**
	 * The Endpoint manager controls the Web Service instance during its whole
	 * lifecycle.
	 */
	private BinasManager endpointManager;

	/** Constructor receives a reference to the endpoint manager. */
	public BinasPortImpl(BinasManager endpointManager) {
		this.endpointManager = endpointManager;
	}

	@Override
	public List<StationView> listStations(Integer numberOfStations, CoordinatesView coordinates) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StationView getInfoStation(String stationId) throws InvalidStation_Exception {
	
//		
//		UDDINaming UDDIname = this.endpointManager.getUddiNaming();
//		String url = UDDIname.lookup(stationId);
//		StationClient s = new StationClient(url);
//		StationView sv = s.getInfo();
//		
////		Collection<String> stations = UDDIname.list("A46_%");
//		
////		for (String stationName : stations) {
////			if (stationName == stationId) {
////				StationClient s = new StationClient(stationId);
////			}
////		}
////		
//		
//		
//
//		
//		CoordinatesView coord = new CoordinatesView();
//		coord.setX(s.getCoordinates().getX());
//		coord.setY(s.getCoordinates().getY());
//		
//		sv.setAvailableBinas(s.getAvailableBinas());
//		sv.setCapacity(s.getMaxCapacity());
//		sv.setCoordinate(coord);
//		sv.setFreeDocks(s.getFreeDocks());
//		sv.setId(s.getId());
//		sv.setTotalGets(s.getTotalGets());
//		sv.setTotalReturns(s.getTotalReturns());
		
		return null;
	}

	@Override
	public int getCredit(String email) throws UserNotExists_Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public UserView activateUser(String email) throws EmailExists_Exception, InvalidEmail_Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void rentBina(String stationId, String email) throws AlreadyHasBina_Exception, InvalidStation_Exception,
			NoBinaAvail_Exception, NoCredit_Exception, UserNotExists_Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void returnBina(String stationId, String email)
			throws FullStation_Exception, InvalidStation_Exception, NoBinaRented_Exception, UserNotExists_Exception {
//		UDDINaming UDDIname = this.endpointManager.getUddiNaming();
//		String url = UDDIname.lookup(stationId);
//		StationClient s = new StationClient(url);
//		s.returnBina();
		
	}

	@Override
	public String testPing(String inputMessage) {
		UDDINaming UDDIname;
		String res = "";
		Collection<UDDIRecord> stations;
		
		try {
			UDDIname = this.endpointManager.getUddiNaming();
			stations = UDDIname.listRecords("A46_Station%");
			
			res += "Found " + stations.size() + "stations.\n";
			
			for (UDDIRecord stationName : stations) {
				System.out.println("conax");
				System.out.println(stationName);
			}
			
			
			System.out.println("PING");
			
			for (UDDIRecord stationName : stations) {
				res += "[Pinging Station" + stationName.getOrgName() + "][Awnser]";
				StationClient sc = new StationClient(stationName.getUrl());
				res += sc.testPing(inputMessage) + "\n";
			}
			System.out.println(res);
		}
		catch (Exception e){
			e.printStackTrace();
		}
		return res;
		
	}

	@Override
	public void testClear() {
		try {
			UDDINaming UDDIname = this.endpointManager.getUddiNaming();
			Collection<UDDIRecord> stations = UDDIname.listRecords("A46_%");
			StationClient sc;
			for (UDDIRecord stationName : stations) {
				sc = new StationClient(stationName.getUrl());
				sc.testClear();
			}
		
		}
		catch (Exception e){
			e.printStackTrace();
		}
		
	}

	@Override
	public void testInitStation(String stationId, int x, int y, int capacity, int returnPrize)
			throws BadInit_Exception {
		
		UDDINaming UDDIname;
		String stationURL;
		StationClient sc;
		
		try {
			UDDIname = this.endpointManager.getUddiNaming();
			stationURL = UDDIname.lookup(stationId);
			sc = new StationClient(stationURL);
			sc.testInit(x, y, capacity, returnPrize);
			
		}
		catch (Exception e){
			e.printStackTrace();
		}
		
		
	}

	@Override
	public void testInit(int userInitialPoints) throws BadInit_Exception {
		// TODO Auto-generated method stub
		
	}
	


}
