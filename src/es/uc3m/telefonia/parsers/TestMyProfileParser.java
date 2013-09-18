package es.uc3m.telefonia.parsers;

import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.res.AssetManager;
import android.test.InstrumentationTestCase;
import es.uc3m.telefonia.expending.MyProfile;
import es.uc3m.telefonia.expending.SingleProfile;
import es.uc3m.telefonia.utilities.Constants;

public class TestMyProfileParser extends InstrumentationTestCase {
	
	MyProfileParser parser;
	String MY_PROFILE_RIGHT_EXAMPLE = "my_profile_fake_example.xml";
	String MY_PROFILE_WITH_ERRORS_EXAMPLE = "my_profile_error_no_fare_no_mobile.xml";
	
	Context ctx;
	AssetManager assets;
	InputStream is;
	
	protected void setUp() throws Exception {
		super.setUp();
		
		ctx = this.getInstrumentation().getContext();
		assertNotNull(ctx);
		
		assets = ctx.getResources().getAssets();
		assertNotNull(assets);
		
		parser = new MyProfileParser();
		assertNotNull(parser);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		
		is = null;
	}
	
	
	///// Tests /////
	public void testMyProfileParser() throws Exception {
		MyProfile myProfile = parse(MY_PROFILE_RIGHT_EXAMPLE);
		
		
		checkLandlineDataParsed(myProfile, 2);
		checkMobilePhoneDataParsed(myProfile, 7);
	}
	
	
	// Test that when some kind if type of phone is not present in the xml file there is not error
	// that info is not show since the array is empty
	// If there isn't a fare, the entry is not process so the count of profiles in landline is only 1
	public void testMyProfileParserWithErrors() throws Exception {
		MyProfile myProfile = parse(MY_PROFILE_WITH_ERRORS_EXAMPLE);		
	
		checkLandlineDataParsed(myProfile, 1);
		
		assertNotNull(myProfile.getProfiles(Constants.MOBILE_PHONE_TYPE));
		assertTrue(myProfile.getProfiles(Constants.MOBILE_PHONE_TYPE).isEmpty());
	}
		
	////////////////////
	
	
	private MyProfile parse(String filename) throws IOException, XmlPullParserException {
		is = assets.open(filename);
		assertNotNull(is);
		
		MyProfile myProfile = parser.parse(is);
		assertNotNull(myProfile);
		
		return myProfile;
	}
	
	private void checkLandlineDataParsed(MyProfile myProfile, int numOfProfiles){
		
		assertNotNull(myProfile.getProfiles(Constants.LANDLINE_TYPE));
		assertEquals(numOfProfiles, myProfile.getProfiles(Constants.LANDLINE_TYPE).size());
		
		for (SingleProfile profile: myProfile.getProfiles(Constants.LANDLINE_TYPE)) {
			assertNotNull(profile);
			assertNotNull(profile.getProfileNumber());
			assertNotNull(profile.getVoiceProfile());
			
			assertNotNull(profile.getProfileFares());
			for (String fares: profile.getProfileFares()) {
				assertNotNull(fares);
			};		
		}
	}
	
	private void checkMobilePhoneDataParsed(MyProfile myProfile, int numOfProfiles){
	
		assertNotNull(myProfile.getProfiles(Constants.MOBILE_PHONE_TYPE));
		assertEquals(numOfProfiles, myProfile.getProfiles(Constants.MOBILE_PHONE_TYPE).size());
		
		for (SingleProfile profile: myProfile.getProfiles(Constants.MOBILE_PHONE_TYPE)) {
			assertNotNull(profile);
			assertNotNull(profile.getProfileNumber());
			assertNotNull(profile.getVoiceProfile());
			assertNotNull(profile.getDataProfile());
			
			assertNotNull(profile.getProfileFares());
			for (String fares: profile.getProfileFares()) {
				assertNotNull(fares);
			};		
		}

	}

}
