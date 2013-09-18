package es.uc3m.telefonia.parsers;

import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.res.AssetManager;
import android.test.InstrumentationTestCase;
import es.uc3m.telefonia.expending.GroupExpendingOrganic;
import es.uc3m.telefonia.expending.SingleFare;
import es.uc3m.telefonia.expending.SingleGroupExpending;
import es.uc3m.telefonia.utilities.Constants;

public class TestGroupExpendingParser extends InstrumentationTestCase {
	
	GroupExpendingParser parser;
	String GROUP_EXPENDING_RIGHT_EXAMPLE = "group_expending_fake_example.xml";
	String GROUP_EXPENDING_WITH_NO_PERIOD_NO_MOBILE = "group_expending_error_no_period_no_mobile.xml";
//	String GROUP_EXPENDING_NO_ENTRY_NO_PERIODS = "group_expending_error_no_entry_no_period.xml";
	
	Context ctx;
	AssetManager assets;
	InputStream is;
	
	protected void setUp() throws Exception {
		super.setUp();
		
		ctx = this.getInstrumentation().getContext();
		assertNotNull(ctx);
		
		assets = ctx.getResources().getAssets();
		assertNotNull(assets);
		
		parser = new GroupExpendingParser();
		assertNotNull(parser);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		
		is = null;
	}
	
	
	///// Tests /////
	public void testParser() throws Exception {
		GroupExpendingOrganic organics = parse(GROUP_EXPENDING_RIGHT_EXAMPLE);
		
		// landline
		assertNotNull(organics.getAllOrganicNames(Constants.LANDLINE_TYPE));
		assertEquals(2, organics.getAllOrganicNames(Constants.LANDLINE_TYPE).size());
		for (String organicName:  organics.getAllOrganicNames(Constants.LANDLINE_TYPE)) {
			assertNotNull(organicName);
				
			checkLandlineDataParsed(organics, organicName);
		}
		
		// mobile phone
		assertNotNull(organics.getAllOrganicNames(Constants.MOBILE_PHONE_TYPE));
		assertEquals(2, organics.getAllOrganicNames(Constants.MOBILE_PHONE_TYPE).size());
		for (String organicName:  organics.getAllOrganicNames(Constants.MOBILE_PHONE_TYPE)) {
			assertNotNull(organicName);
				
			checkMobilePhoneDataParsed(organics, organicName);
		}

	}
	
	
	// This test checks that when some user don't have any mobile phone there is no error, only that the correspondent 
	// array is empty
	// When there isn't any period there is not error, the app show that it doesn't have any phone of that type associated
	public void testParserWithNoPeriodNoMobileData() throws Exception {
		GroupExpendingOrganic organics = parse(GROUP_EXPENDING_WITH_NO_PERIOD_NO_MOBILE);
		assertNotNull(organics);
		
		// landline
		assertNotNull(organics.getAllOrganicNames(Constants.LANDLINE_TYPE));
		assertEquals(3, organics.getAllOrganicNames(Constants.LANDLINE_TYPE).size());
		
		for(int i = 0; i < 2; i++) {
			String organicName= organics.getAllOrganicNames(Constants.LANDLINE_TYPE).get(i);
			assertNotNull(organicName);
			checkLandlineDataParsed(organics, organicName);
		}
		
		String organicName_2 = organics.getAllOrganicNames(Constants.LANDLINE_TYPE).get(2);
		assertTrue(organics.getAllPeriodsNamesByOrganic(Constants.LANDLINE_TYPE, organicName_2).isEmpty());
		
		
		// mobile phone
		assertNotNull(organics.getAllOrganicNames(Constants.MOBILE_PHONE_TYPE));
		assertTrue(organics.getAllOrganicNames(Constants.MOBILE_PHONE_TYPE).isEmpty());
	}	
	
	////////////////////
	
	
	private GroupExpendingOrganic parse(String filename) throws IOException, XmlPullParserException {
		is = assets.open(filename);
		assertNotNull(is);
		
		GroupExpendingOrganic organics = parser.parse(is);
		assertNotNull(organics);
		
		return organics;
	}
	
	
	private void checkLandlineDataParsed(GroupExpendingOrganic organics, String organicName){
		
		assertNotNull(organics.getAllPeriodsNamesByOrganic(Constants.LANDLINE_TYPE, organicName));
		for (String periodName: organics.getAllPeriodsNamesByOrganic(Constants.LANDLINE_TYPE, organicName)) {
			assertNotNull(periodName);
			
			assertNotNull(organics.getPeriod(Constants.LANDLINE_TYPE, organicName, periodName));
			for (SingleGroupExpending period: organics.getPeriod(Constants.LANDLINE_TYPE, organicName, periodName)) {
				assertNotNull(period.getPhoneNumber());
				
				assertNotNull(period.getFares());
				for (SingleFare fares: period.getFares()) {
					assertNotNull(fares.getFareName());
					assertNotNull(fares.getFareCost());
				}
				
				assertNotNull(period.getTotalCost());
			}

		}
	}
	
	private void checkMobilePhoneDataParsed(GroupExpendingOrganic organics, String organicName){

		assertNotNull(organics.getAllPeriodsNamesByOrganic(Constants.MOBILE_PHONE_TYPE, organicName));
		for (String periodName: organics.getAllPeriodsNamesByOrganic(Constants.MOBILE_PHONE_TYPE, organicName)) {
			assertNotNull(periodName);
			
			assertNotNull(organics.getPeriod(Constants.MOBILE_PHONE_TYPE, organicName, periodName));
			for (SingleGroupExpending period: organics.getPeriod(Constants.MOBILE_PHONE_TYPE, organicName, periodName)) {
				assertNotNull(period.getPhoneNumber());
				
				assertNotNull(period.getFares());
				for (SingleFare fares: period.getFares()) {
					assertNotNull(fares.getFareName());
					assertNotNull(fares.getFareCost());
				}
				
				assertNotNull(period.getTotalCost());
			}

		}

	}

}
