package org.binas.ws;

import java.util.Collection;
import java.util.List;

import javax.jws.WebService;

import org.binas.domain.BinasManager;
import org.binas.domain.User;
import org.binas.station.ws.cli.StationClient;

import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINamingException;
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
	
		UDDINaming UDDIname;
		StationClient s;
		org.binas.station.ws.StationView svS = new org.binas.station.ws.StationView();
		StationView svB = new StationView();
		
		try {
			UDDIname = this.endpointManager.getUddiNaming();
			String url = UDDIname.lookup(stationId);
			s = new StationClient(url);
			svS = s.getInfo();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		CoordinatesView coord = new CoordinatesView();
		coord.setX(svS.getCoordinate().getX());
		coord.setY(svS.getCoordinate().getY());
		
		svB.setAvailableBinas(svS.getAvailableBinas());
		svB.setCapacity(svS.getCapacity());
		svB.setCoordinate(coord);
		svB.setFreeDocks(svS.getFreeDocks());
		svB.setId(svS.getId());
		svB.setTotalGets(svS.getTotalGets());
		svB.setTotalReturns(svS.getTotalReturns());
		
		return svB;
	}

	@Override
	public int getCredit(String email) throws UserNotExists_Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public UserView activateUser(String email) throws EmailExists_Exception, InvalidEmail_Exception {
		User user = null;
		user.getUser(email);
		UserView userView = user.getUserView();
		return userView;
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
			
			res += "Found " + stations.size() + " stations.\n";
			
			for (UDDIRecord stationName : stations) {
				System.out.println(stationName);
			}
			
			for (UDDIRecord stationName : stations) {
				res += "[Pinging Station" + stationName.getOrgName() + "][Aswser]";
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
