package es.uc3m.telefonia.parsers;

import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.res.AssetManager;
import android.test.InstrumentationTestCase;
import es.uc3m.telefonia.expending.MyExpendingPeriod;
import es.uc3m.telefonia.expending.SingleFare;
import es.uc3m.telefonia.expending.SingleMyExpending;
import es.uc3m.telefonia.utilities.Constants;

public class TestMyExpendingParser extends InstrumentationTestCase {
	
	MyExpendingParser parser;
	String MY_EXPENDING_RIGHT_EXAMPLE = "my_expending_fake_example.xml";
	String MY_EXPENDING_WITH_NO_MOBILE_DATA = "my_expending_with_error_no_mobile.xml";
	String MY_EXPENDING_NO_ENTRY_NO_PERIODS = "my_expending_error_no_entry_no_period.xml";
	
	Context ctx;
	AssetManager assets;
	InputStream is;
	
	protected void setUp() throws Exception {
		super.setUp();
		
		ctx = this.getInstrumentation().getContext();
		assertNotNull(ctx);
		
		assets = ctx.getResources().getAssets();
		assertNotNull(assets);
		
		parser = new MyExpendingParser();
		assertNotNull(parser);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		
		is = null;
	}
	
	
	///// Tests /////
	public void testMyExpendingPeriodParser() throws Exception {
		MyExpendingPeriod periods = parse(MY_EXPENDING_RIGHT_EXAMPLE);
		
		checkLandlineDataParsed(periods, 3);
		checkMobilePhoneDataParsed(periods, 2);
	}
	
	// This test checks that when some user don't have any mobile phone there is no error, only that the correspondent 
	// array is empty
	public void testParserWithNoMobileData() throws Exception {
		MyExpendingPeriod periods = parse(MY_EXPENDING_WITH_NO_MOBILE_DATA);
		
		checkLandlineDataParsed(periods, 3);
		
		assertNotNull(periods.getAllMyExpendingPeriodsNames(Constants.MOBILE_PHONE_TYPE));
		assertTrue(periods.getAllMyExpendingPeriodsNames(Constants.MOBILE_PHONE_TYPE).isEmpty());
	}
	
	// This test checks that when a period is empty, that name of period is not added to the list in order not to show none data
	// When there isn't any period there is not error, the app show that it doesn't have any phone of that type associated
	public void testParserNoEntryAndNoPeriods() throws Exception {
		MyExpendingPeriod periods = parse(MY_EXPENDING_NO_ENTRY_NO_PERIODS);
		assertNotNull(periods);
		
		checkLandlineDataParsed(periods, 2);
		checkMobilePhoneDataParsed(periods, 0);
		
		assertTrue(periods.getAllMyExpendingPeriodsNames(Constants.MOBILE_PHONE_TYPE).isEmpty());
	}
		
	
	////////////////////
	
	
	private MyExpendingPeriod parse(String filename) throws IOException, XmlPullParserException {
		is = assets.open(filename);
		assertNotNull(is);
		
		MyExpendingPeriod periods = parser.parse(is);
		assertNotNull(periods);
		
		return periods;
	}
	
	
	private void checkLandlineDataParsed(MyExpendingPeriod periods, int numPeriods){
		
		assertNotNull(periods.getAllMyExpendingPeriodsNames(Constants.LANDLINE_TYPE));
		assertEquals(numPeriods, periods.getAllMyExpendingPeriodsNames(Constants.LANDLINE_TYPE).size());
		for (String name:  periods.getAllMyExpendingPeriodsNames(Constants.LANDLINE_TYPE)) {
			assertNotNull(name);
			
			assertNotNull(periods.getMyExpendingPeriod(Constants.LANDLINE_TYPE, name));
			for (SingleMyExpending expending: periods.getMyExpendingPeriod(Constants.LANDLINE_TYPE, name)) {
				assertNotNull(expending.getPhoneNumber());
				
				assertNotNull(expending.getFares());
				for (SingleFare fares: expending.getFares()) {
					assertNotNull(fares.getFareName());
					assertNotNull(fares.getFareCost());
				}
				
				assertNotNull(expending.getTotalCost());
			}

		}
	}
	
	private void checkMobilePhoneDataParsed(MyExpendingPeriod periods, int numPeriods){
		assertNotNull(periods.getAllMyExpendingPeriodsNames(Constants.MOBILE_PHONE_TYPE));
		assertEquals(numPeriods, periods.getAllMyExpendingPeriodsNames(Constants.MOBILE_PHONE_TYPE).size());
		for (String name:  periods.getAllMyExpendingPeriodsNames(Constants.MOBILE_PHONE_TYPE)) {
			assertNotNull(name);
			
			assertNotNull(periods.getMyExpendingPeriod(Constants.MOBILE_PHONE_TYPE, name));
			for (SingleMyExpending expending: periods.getMyExpendingPeriod(Constants.MOBILE_PHONE_TYPE, name)) {
				assertNotNull(expending.getPhoneNumber());
				
				assertNotNull(expending.getFares());
				for (SingleFare fares: expending.getFares()) {
					assertNotNull(fares.getFareName());
					assertNotNull(fares.getFareCost());
				}
				
				assertNotNull(expending.getTotalCost());
			}
		}

	}

}
