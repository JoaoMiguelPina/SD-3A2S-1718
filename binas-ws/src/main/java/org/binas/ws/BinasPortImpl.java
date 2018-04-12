package org.binas.ws;

import java.util.Collection;
import java.util.List;

import javax.jws.WebService;

import org.binas.domain.BinasManager;
import org.binas.domain.User;
import org.binas.station.ws.NoSlotAvail_Exception;
import org.binas.station.ws.cli.StationClient;
import org.binas.station.ws.cli.StationClientException;

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
		
		UDDINaming UDDIname;
		StationClient s;
		StationClient closest;
		Collection<String> stations;
		Collection<StationClient> ordered = null;
		List<StationView> result = null;
		String[] stationsString;
		
		try {
			UDDIname = this.endpointManager.getUddiNaming();
			stations = UDDIname.list("A46_Station%");
			for(int i = 0; i < numberOfStations; i++) {
				stationsString = (String[]) stations.toArray();
				closest = new StationClient(stationsString[0]);
				for(String station : stations) {
					s = new StationClient(station);	
					if((Math.pow(s.getInfo().getCoordinate().getX(), 2) + Math.pow(s.getInfo().getCoordinate().getY(), 2)) < (Math.pow(closest.getInfo().getCoordinate().getX(), 2) + Math.pow(closest.getInfo().getCoordinate().getY(), 2)) ){
						closest = s;
					}		
				}
				stations.remove(closest.getWsURL());
				result.add(this.getInfoStation(closest.getWsName()));
			}
			
		} catch (UDDINamingException e) {
			System.out.println("There was an error while calling UDDINaming at listStations(). Check output: ");
			e.printStackTrace();
		}
		catch (StationClientException e) {
			System.out.println("There was an error while calling the StationClient at listStations(). Check output: ");
			e.printStackTrace();
		}
		catch (InvalidStation_Exception e) {
			System.out.println("The chosen Station is invalid at listStations(). Check output: ");
			e.printStackTrace();
		}
		
		return result;
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
			
		} catch (UDDINamingException e) {
			System.out.println("There was an error while calling UDDINaming at getInfoStation(). Check output: ");
			e.printStackTrace();
		}
		catch (StationClientException e) {
			System.out.println("There was an error while calling the StationClient at getInfoStation(). Check output: ");
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
		User user = null;
		int credit = 0;
		try {
			user = user.getUser(email);
			credit = user.getCredit();
		} catch (InvalidEmail_Exception e) {
			System.out.printf("Email syntax (" + e + ") not valid.");
		}
		return credit;
	}

	@Override
	public UserView activateUser(String email) throws EmailExists_Exception, InvalidEmail_Exception {
		User user = null;
		user = user.getUser(email);
		UserView userView = user.getUserView();
		return userView;
	}

	@Override
	public void rentBina(String stationId, String email) throws AlreadyHasBina_Exception, InvalidStation_Exception,
			NoBinaAvail_Exception, NoCredit_Exception, UserNotExists_Exception {
		
		StationClient s;
		try {
			s = new StationClient(this.endpointManager.getUddiUrl(), stationId);
			org.binas.station.ws.StationView sv = s.getInfo();
			User user = null;
			user = user.getUser(email);
			UserView userView = user.getUserView();
			
			if (sv.getAvailableBinas() == 0) throw new NoBinaAvail_Exception("There is no bina available at this station.", null);
			if (userView.getCredit() < 1) throw new NoCredit_Exception("User does not have enought credits to rent the bina.", null);
			else {
				s.getBina();
				user.setHasBina(true);
				user.removeOneCredit();
			}
		} catch (StationClientException e) {
			System.out.println("There was an error while calling the StationClient. Check output: " + e);
		} catch (InvalidEmail_Exception e) {
			System.out.println("The provided email (" + e + ") is invalid.");
		} catch (org.binas.station.ws.NoBinaAvail_Exception e) {
			System.out.println("There is no bina available at this station.");
		}
		
		
	}

	@Override
	public void returnBina(String stationId, String email)
			throws FullStation_Exception, InvalidStation_Exception, NoBinaRented_Exception, UserNotExists_Exception {
		
		StationClient s;
		try {
			s = new StationClient(this.endpointManager.getUddiUrl(), stationId);
			org.binas.station.ws.StationView sv = s.getInfo();
			
			User user = null;
			user = user.getUser(email);
			UserView uv = user.getUserView();
			
			if (user.doesHaveBina()) {
				if (sv.getFreeDocks() == 0) {
					throw new FullStation_Exception("This station is full", null);
				}
				else{
					int bonus = s.returnBina();
					user.setHasBina(false);
					user.addCredit(bonus);
				}
			}
		} catch (StationClientException e) {
			System.out.println("There was an error while calling the StationClient. Check output: " + e);
		} catch (InvalidEmail_Exception e) {
			System.out.println("The provided email (" + e + ") is invalid.");
		} catch (NoSlotAvail_Exception e) {
			System.out.println("There is no slot available at this station.");
		}
		
		throw new NoBinaRented_Exception(email, null);
		
		
		
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
			
		}catch (UDDINamingException e) {
			System.out.println("There was an error while calling UDDINaming at testInitStation(). Check output: ");
			e.printStackTrace();
		}
		catch (StationClientException e) {
			System.out.println("There was an error while calling the StationClient at testInitStation(). Check output: ");
			e.printStackTrace();
		}
		catch (org.binas.station.ws.BadInit_Exception e) {
			System.out.println("There was an error while trying to Init at testInitStation(). Check output: ");
			e.printStackTrace();
		}
		
		
	}

	@Override
	public void testInit(int userInitialPoints) throws BadInit_Exception {
		// TODO Auto-generated method stub
		
	}
	


}
