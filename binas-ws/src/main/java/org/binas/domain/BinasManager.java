package org.binas.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

import javax.xml.ws.Response;

import org.binas.domain.exception.BadInitException;
import org.binas.domain.exception.InsufficientCreditsException;
import org.binas.domain.exception.InvalidEmailException;
import org.binas.domain.exception.StationNotFoundException;
import org.binas.domain.exception.UserAlreadyExistsException;
import org.binas.domain.exception.UserAlreadyHasBinaException;
import org.binas.domain.exception.UserHasNoBinaException;
import org.binas.domain.exception.UserNotFoundException;
import org.binas.station.ws.BadInit_Exception;
import org.binas.station.ws.BalanceView;
import org.binas.station.ws.GetBalanceResponse;
import org.binas.station.ws.NoBinaAvail_Exception;
import org.binas.station.ws.NoSlotAvail_Exception;
import org.binas.station.ws.SetBalanceResponse;
import org.binas.station.ws.cli.StationClient;
import org.binas.station.ws.cli.StationClientException;
import org.binas.ws.StationView;
import org.binas.ws.UserNotExists_Exception;

import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINamingException;
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDIRecord;

/**
 * BinasManager class 
 * 
 * Class that have the methods used to get/Return Bina, beginning a station, querying all stations, etc.
 *
 */
public class BinasManager {
	/**
	 * UDDI server URL
	 */
	private String uddiURL = null;

	/**
	 * Station name
	 */
	private String stationTemplateName = null;

	private int nStations = 3;
	
	private int quorum = 2;
	
	// Singleton -------------------------------------------------------------

	private BinasManager() {
	}

	/**
	 * SingletonHolder is loaded on the first execution of Singleton.getInstance()
	 * or the first access to SingletonHolder.INSTANCE, not before.
	 */
	private static class SingletonHolder {
		private static final BinasManager INSTANCE = new BinasManager();
	}

	public static synchronized BinasManager getInstance() {
		return SingletonHolder.INSTANCE;
	}

	// Binas Logic ----------------------------------------------------------

	public User createUser(String email) throws UserAlreadyExistsException, InvalidEmailException {
		return UsersManager.getInstance().RegisterNewUser(email);
	}

	public User getUser(String email) throws UserNotFoundException {
		return UsersManager.getInstance().getUser(email);
	}
	
	public void rentBina(String stationId, String email) throws UserNotFoundException, InsufficientCreditsException, UserAlreadyHasBinaException, StationNotFoundException, NoBinaAvail_Exception {
		User user = getUser(email);
		synchronized (user) {
			//validate user can rent
			user.validateCanRentBina();

			try {
				writeBalance(email, -1);
			} catch (UserNotExists_Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//apply rent action to user
			user.effectiveRent();
		}
	}
	
	public void returnBina(String stationId, String email) throws UserNotFoundException, NoSlotAvail_Exception, UserHasNoBinaException, StationNotFoundException {
		User user = getUser(email);
		synchronized (user) {
			//validate user can rent
			user.validateCanReturnBina();
			
			//validate station can rent
			StationClient stationCli = getStation(stationId);
			int prize = stationCli.returnBina();
			
			try {
				writeBalance(email, prize);
			} catch (UserNotExists_Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//apply rent action to user
			user.effectiveReturn(prize);
		}		
	}

	public StationClient getStation(String stationId) throws StationNotFoundException {

		Collection<String> stations = this.getStations();
		String uddiUrl = BinasManager.getInstance().getUddiURL();
		
		for (String s : stations) {
			try {
				StationClient sc = new StationClient(uddiUrl, s);
				org.binas.station.ws.StationView sv = sc.getInfo();
				String idToCompare = sv.getId();
				if (idToCompare.equals(stationId)) {
					return sc;
				}
			} catch (StationClientException e) {
				continue;
			}
		}
		
		throw new StationNotFoundException();
	}
	
	
	// UDDI ------------------------------------------------------------------

	public void initUddiURL(String uddiURL) {
		setUddiURL(uddiURL);
	}

	public void initStationTemplateName(String stationTemplateName) {
		setStationTemplateName(stationTemplateName);
	}

	public String getUddiURL() {
		return uddiURL;
	}

	private void setUddiURL(String url) {
		uddiURL = url;
	}

	private void setStationTemplateName(String sn) {
		stationTemplateName = sn;
	}

	public String getStationTemplateName() {
		return stationTemplateName;
	}

	/**
	 * Get list of stations for a given query
	 * 
	 * @return List of stations
	 */
	public Collection<String> getStations() {
		Collection<UDDIRecord> records = null;
		Collection<String> stations = new ArrayList<String>();
		try {
			UDDINaming uddi = new UDDINaming(uddiURL);
			records = uddi.listRecords(stationTemplateName + "%");
			for (UDDIRecord u : records)
				stations.add(u.getOrgName());
		} catch (UDDINamingException e) {
		}
		return stations;
	}

	public void reset() {
		UsersManager.getInstance().reset();
	}

	public void init(int userInitialPoints) throws BadInitException {
		if(userInitialPoints < 0) {
			throw new BadInitException();
		}
		UsersManager.getInstance().init(userInitialPoints);
	}

	/**
	 * 
	 * Inits a Station with a determined ID, coordinates, capacity and returnPrize
	 * 
	 * @param stationId
	 * @param x
	 * @param y
	 * @param capacity
	 * @param returnPrize
	 * @throws BadInitException
	 * @throws StationNotFoundException
	 */
	public void testInitStation(String stationId, int x, int y, int capacity, int returnPrize) throws BadInitException, StationNotFoundException {
		//validate station can rent
		StationClient stationCli;
		try {
			stationCli = getStation(stationId);
			stationCli.testInit(x, y, capacity, returnPrize);
		} catch (BadInit_Exception e) {
			throw new BadInitException(e.getMessage());
		}
		
	}

	public int getNStations() {
		return nStations;
	}

	public void setNStations(int nStations) {
		this.nStations = nStations;
		updateQuorum(nStations);
	}
	
	public void updateQuorum(int nStations) {
		this.quorum = nStations/2 + 1;
	}

	public int getQuorum() {
		return quorum;
	}
	
	public synchronized BalanceView readBalance(String email) throws UserNotExists_Exception, StationNotFoundException {
		
		int received = 0;
		List<Response<GetBalanceResponse>> responses = new ArrayList<Response<GetBalanceResponse>>();
		BalanceView bv = new BalanceView();
		bv.setTag(0);
		bv.setValue(0);
		
		for(int i = 1; i <= this.nStations; i++) {	
			StationClient stationCli = getStation("A46_Station"+i);
			responses.add(stationCli.getBalanceAsync(email));	
		}
		
		while(received < this.quorum) {
			for(Response<GetBalanceResponse> response : responses) {
				if(response.isDone()) {
					received++;
					try {
						if(response.get().getBalanceView() != null) {
							if(response.get().getBalanceView().getTag() > bv.getTag()) {
								bv.setTag(response.get().getBalanceView().getTag());
								bv.setValue(response.get().getBalanceView().getValue());
							}
						}
					}
					catch (InterruptedException e) {
						continue;
					} 
					catch (ExecutionException e) {
						responses.remove(response);
						break;
					}
					
					responses.remove(response);	
					break;
				}
		
			}
		}
		return bv;
	}
	
	public synchronized void writeBalance(String email, int value) throws UserNotExists_Exception, StationNotFoundException {
		BalanceView bv = readBalance(email);
		int newTag = bv.getTag() + 1;
		int received = 0;
		List<Response<SetBalanceResponse>> responses = new ArrayList<Response<SetBalanceResponse>>();
		
		for(int i = 1; i <= this.nStations; i++) {	
			StationClient stationCli = getStation("A46_Station"+i);
			responses.add(stationCli.setBalanceAsync(email, bv.getValue() + value, newTag));	
		}
		
		while(received < this.quorum) {
			for(Response<SetBalanceResponse> response : responses) {
				if(response.isDone()) {
					received++;
					responses.remove(response);	
					break;
				}
		
			}
		}
	}

}
