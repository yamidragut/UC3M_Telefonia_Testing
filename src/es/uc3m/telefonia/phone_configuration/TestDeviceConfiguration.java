package es.uc3m.telefonia.phone_configuration;

import java.util.ArrayList;

import android.os.Build;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.jayway.android.robotium.solo.Solo;

import es.uc3m.telefonia.R;
import es.uc3m.telefonia.utilities.Constants;

public class TestDeviceConfiguration extends ActivityInstrumentationTestCase2<DeviceConfiguration> {

	private Solo solo;
	private String WRONG_ACTIVITY = "Wrong activity";
	
	private String bannerText = "Configuración y Soporte > Configuración del Smartphone";
	
	private String [] nextActivityBannerText = {"Introducir SIM",
												"Activar teléfono",
												"Configuración Eduroam",
												"Correo electrónico",
												"VPN",
												"Activar/Desactivar datos móviles"};
	
	private String BANNER_TEXT_SEPARATION = " > ";
	
	public TestDeviceConfiguration() {
		super(DeviceConfiguration.class);
	}
	
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		solo = new Solo(getInstrumentation(), getActivity());
	}
	
	
	@Override
	public void tearDown() throws Exception {
		solo.finishOpenedActivities();
	}
	
	
	public void testBanner() {
		
		// Check we are in the correct activity
		solo.assertCurrentActivity(WRONG_ACTIVITY, DeviceConfiguration.class);
		checkBannerText(bannerText);

	}
	
	
	public void testListOptions() {
		
		ListView list = (ListView) solo.getView(R.id.assistance_list);
		
		// From 1 to count + 1 the iteration because 0 is the listView
		for(int i=1; i < list.getAdapter().getCount()+1; i++){
			
			// Has to be here in order to use the method in class DeviceConfiguration
			int [] expectedImageIds = getExpectedImageIds(i-1);
			
			assertEquals(nextActivityBannerText[i-1], ((TextView) solo.getViews(list).get(i)).getText());
			
		    solo.clickOnView(solo.getViews(list).get(i));
		    solo.waitForActivity(DeviceConfigurationPager.class, 5000);
		    
		    solo.assertCurrentActivity(WRONG_ACTIVITY, DeviceConfigurationPager.class);
		    checkBannerText(bannerText + BANNER_TEXT_SEPARATION + nextActivityBannerText[i-1]);
			int [] actualImageIds = ( (DeviceConfigurationPager) solo.getCurrentActivity()).getImageIds();
			
			for(int j = 0; j < expectedImageIds.length; j++) {
				assertEquals(expectedImageIds[j], actualImageIds[j]);
			}
			
		    solo.goBack();
		}
		
	}
	
	
	private void checkBannerText(String expectedText) {
		// Check the banner text is correct
		TextView bannerTextView = (TextView) solo.getView(R.id.banner_text);
		assertEquals(expectedText, bannerTextView.getText());
	}
	
	
	private int [] getExpectedImageIds(int listSelectionIndex) {
		
		int imageIds [];

		switch(listSelectionIndex) {
		
			case 0:
				if(Build.MODEL.toLowerCase().contains(Constants.HTC_DESIRE_MODEL)) {
					imageIds = new int[] {R.drawable.htc_sim1, R.drawable.htc_sim2};
				}
					
				else 
					imageIds = new int[] {R.drawable.gs2_sim1, R.drawable.gs2_sim2, 
							R.drawable.gs2_sim3, R.drawable.gs2_sim4};	
				
				break;
			
			case 1:
				imageIds = new int [] {R.drawable.gs2_device_activation};
				break;
				
			case 2:
				imageIds = new int [] {R.drawable.gs2_eduroam1, R.drawable.gs2_eduroam2, 
						R.drawable.gs2_eduroam3, R.drawable.gs2_eduroam4, R.drawable.gs2_eduroam5};
				break;
				
			case 3:
				imageIds = new int [] {R.drawable.gs2_email1, R.drawable.gs2_email2, 
						R.drawable.gs2_email3, R.drawable.gs2_email4, R.drawable.gs2_email5,
						R.drawable.gs2_email6, R.drawable.gs2_email7};
				break;	
				
			case 4:
				imageIds = new int [] {R.drawable.gs2_vpn1, R.drawable.gs2_vpn2, R.drawable.gs2_vpn3,
						R.drawable.gs2_vpn4, R.drawable.gs2_vpn5, R.drawable.gs2_vpn6,
						R.drawable.gs2_vpn7, R.drawable.gs2_vpn8};
				break;
				
			case 5:
				imageIds = new int [] {R.drawable.gs2_network1, R.drawable.gs2_network2, R.drawable.gs2_network3};
				break;
			
			default:
				int[] defaultError = new int [1];
				return defaultError;
		}
		
		return imageIds;
		
	}

}
