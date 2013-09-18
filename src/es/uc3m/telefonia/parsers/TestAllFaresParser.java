package es.uc3m.telefonia.parsers;

import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.res.AssetManager;
import android.test.InstrumentationTestCase;
import es.uc3m.telefonia.expending.AllFares;
import es.uc3m.telefonia.expending.AllFlatFare;
import es.uc3m.telefonia.expending.AllRoamingFare;
import es.uc3m.telefonia.expending.AllVoiceFare;
import es.uc3m.telefonia.utilities.Constants;

public class TestAllFaresParser extends InstrumentationTestCase {
	
	AllFaresParser parser;
	String ALL_FARES_RIGHT_EXAMPLE = "all_fares_example.xml";
	String ALL_FARES_WITH_ERRORS_EXAMPLE = "all_fares_error.xml";
	
	Context ctx;
	AssetManager assets;
	InputStream is;
	
	protected void setUp() throws Exception {
		super.setUp();
		
		ctx = this.getInstrumentation().getContext();
		assertNotNull(ctx);
		
		assets = ctx.getResources().getAssets();
		assertNotNull(assets);
		
		parser = new AllFaresParser();
		assertNotNull(parser);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		
		is = null;
	}
	
	
	///// Tests /////
	public void testAllFaresParser() throws Exception {
		AllFares allFares = parse(ALL_FARES_RIGHT_EXAMPLE);
		checkFlatFareLandline(allFares);
		checkFlatFareDataMobilePhone(allFares);
		checkFlatFareVoiceDataMobilePhone(allFares);
		checkLandlineVoiceFare(allFares);
		checkRoamingFareData(allFares);
		checkRoamingFareVoice(allFares);
		checkMobilePhoneVoiceFare(allFares);
	}
	
	
	// Test that when some kind if type of fare is not present in the xml file there is not error
	// that info is not show since the array is empty
	public void testAllFaresParserWithErrors() throws Exception {
		AllFares allFares = parse(ALL_FARES_WITH_ERRORS_EXAMPLE);		
		assertNotNull(allFares);
	
		checkFlatFareLandline(allFares);
		checkFlatFareDataMobilePhone(allFares);
		checkFlatFareVoiceDataMobilePhone(allFares);
		checkLandlineVoiceFare(allFares);
		checkRoamingFareData(allFares);
		checkRoamingFareVoice(allFares);
		
		assertNotNull(allFares.getVoiceFare(Constants.MOBILE_PHONE_TYPE));
		assertTrue(allFares.getVoiceFare(Constants.MOBILE_PHONE_TYPE).isEmpty());
	}
		
	////////////////////
	
	
	private AllFares parse(String filename) throws IOException, XmlPullParserException {
		is = assets.open(filename);
		assertNotNull(is);
		
		AllFares allFares = parser.parse(is);
		assertNotNull(allFares);
		
		return allFares;
	}
	
	
	private void checkFlatFareLandline(AllFares allFares){
		
		assertNotNull(allFares.getFlatFareLandline());
		for (AllFlatFare fare: allFares.getFlatFareLandline()) {
			assertNotNull(fare.getFareName());
			assertNotNull(fare.getPhoneType());
			assertNotNull(fare.getCost());
		}
	}
		
	private void checkFlatFareVoiceDataMobilePhone(AllFares allFares){
		assertNotNull(allFares.getFlatFareVoiceDataMobilePhone());
		for (AllFlatFare fare: allFares.getFlatFareVoiceDataMobilePhone()) {
			assertNotNull(fare.getFareName());
			assertNotNull(fare.getPhoneType());
			assertNotNull(fare.getCost());
			assertNotNull(fare.getMobileFareType());
		}
	}
	
	private void checkFlatFareDataMobilePhone(AllFares allFares){
		assertNotNull(allFares.getFlatFareDataMobilePhone());
		for (AllFlatFare fare: allFares.getFlatFareDataMobilePhone()) {
			assertNotNull(fare.getFareName());
			assertNotNull(fare.getPhoneType());
			assertNotNull(fare.getCost());
			assertNotNull(fare.getMobileFareType());
		}
	}
	
	private void checkRoamingFareData(AllFares allFares){	
		assertNotNull(allFares.getRoamingFareData());
		for (AllRoamingFare fare: allFares.getRoamingFareVoice()) {
			assertNotNull(fare.getFareName());
			assertNotNull(fare.getPhoneType());
			assertNotNull(fare.getCost());
		}
	}
	
	private void checkRoamingFareVoice(AllFares allFares){
		assertNotNull(allFares.getRoamingFareVoice());
		for (AllRoamingFare fare: allFares.getRoamingFareData()) {
			assertNotNull(fare.getFareName());
			assertNotNull(fare.getPhoneType());
			assertNotNull(fare.getCost());
		}
	}
	
	private void checkLandlineVoiceFare(AllFares allFares){
		assertNotNull(allFares.getVoiceFare(Constants.LANDLINE_TYPE));
		for (AllVoiceFare voiceFare: allFares.getVoiceFare(Constants.LANDLINE_TYPE)) {
			assertNotNull(voiceFare.getFareName());
			assertNotNull(voiceFare.getPhoneType());
			assertNotNull(voiceFare.getCost());
		}
	}
	
	private void checkMobilePhoneVoiceFare(AllFares allFares){
		assertNotNull(allFares.getVoiceFare(Constants.MOBILE_PHONE_TYPE));
		for (AllVoiceFare voiceFare: allFares.getVoiceFare(Constants.MOBILE_PHONE_TYPE)) {
			assertNotNull(voiceFare.getFareName());
			assertNotNull(voiceFare.getPhoneType());
			assertNotNull(voiceFare.getCost());
		}
	}

}
