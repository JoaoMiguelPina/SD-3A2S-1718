package org.binas.station.ws.it;

import static org.junit.Assert.*;

import org.binas.station.ws.BadInit_Exception;
import org.binas.station.ws.NoBinaAvail_Exception;
import org.binas.station.ws.NoSlotAvail_Exception;
import org.binas.station.ws.StationView;
import org.junit.AfterClass;
import org.junit.Test;

public class GetInfoMethodTest extends BaseIT {
	

	@Test
	public void getInfo50() {
		StationView sv = new StationView();
		try {
			client.testInit(0, 0, 50, 0);
			for (int i=0; i<40; i++) {
				try {
					client.getBina();
				} catch (NoBinaAvail_Exception e) {
					e.printStackTrace();
				}
			}
			
			for (int i=0; i<30; i++) {
				try {
					client.returnBina();
				} catch (NoSlotAvail_Exception e) {
					e.printStackTrace();
				}
			}
			sv = client.getInfo();
			
			assertEquals(30, sv.getAvailableBinas());
			
			
		} catch (BadInit_Exception e) {
			System.out.println("There was an init error. Check output: ");
			e.printStackTrace();
		}
	}
	
	@Test
	public void getInfo100() {
		StationView sv = new StationView();
		try {
			client.testInit(0, 0, 100, 0);
			for (int i=0; i<40; i++) {
				try {
					client.getBina();
				} catch (NoBinaAvail_Exception e) {
					e.printStackTrace();
				}
			}
			
			for (int i=0; i<30; i++) {
				try {
					client.returnBina();
				} catch (NoSlotAvail_Exception e) {
					e.printStackTrace();
				}
			}
			sv = client.getInfo();
			
			assertEquals(90, sv.getAvailableBinas());
			
			
		} catch (BadInit_Exception e) {
			System.out.println("There was an init error. Check output: ");
			e.printStackTrace();
		}
	}
	
	@AfterClass
	public static void cleanup() {
		client.testClear();
	}
}
