package pt.lsts.asa;

import pt.lsts.asa.settings.Profile;
import pt.lsts.asa.settings.Settings;
import pt.lsts.asa.util.FileOperations;
import pt.lsts.imc.IMCDefinition;

import android.app.Application;
import android.content.Context;
import android.media.AudioManager;
import android.util.Log;

/**
 * Class extending application that does a single startup for the application
 * needed to initialize ACCU state object
 * 
 * @author sharp
 *
 */
public class App extends Application {

	private static AudioManager audioManager;

	public static AudioManager getAudioManager() {
		return audioManager;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

		IMCDefinition.getInstance();
		ASA.getInstance(this);
		ASA.getInstance().load();
		ASA.getInstance().start();
		Log.i("App", "Global ASA Object Initialized");

		initSettings();

	}

	public void initSettings() {
		Settings.getSettings();
		FileOperations.copySpecificAsset(getBaseContext(), "default_settings.csv");
		if (Settings.getAll().isEmpty()) {// if no previous settings, set the
											// defaults
			Profile.restoreDefaults();
		}
	}

}
