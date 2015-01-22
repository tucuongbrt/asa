package pt.lsts.asa.fragments;

import android.support.v4.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import pt.lsts.asa.ASA;
import pt.lsts.asa.R;
import pt.lsts.asa.listenners.sharedPreferences.ManualIndicatorsPreferencesListenner;
import pt.lsts.asa.settings.Settings;
import pt.lsts.asa.subscribers.ManualIndicatorsFragmentIMCSubscriber;

/**
 * Created by jloureiro on 1/14/15.
 */
public class ManualIndicatorsFragment extends Fragment {

    private FragmentActivity fragmentActivity = null;
    private TextView leftTextView = null;
    private TextView rightTextView = null;
    private TextView centerTextView = null;
    private ManualIndicatorsFragmentIMCSubscriber manualIndicatorsFragmentIMCSubscriber = null;
    private ScheduledFuture handle;
    private final ScheduledExecutorService scheduler = Executors
            .newScheduledThreadPool(1);
    private Runnable runnable;
    private int interval= (Settings.getInt("comms_timeout_interval_in_seconds", 60) * 1000);

    public ManualIndicatorsFragment() {
        // Required empty public constructor
    }

    public ManualIndicatorsFragment(FragmentActivity fragmentActivity){
        this.fragmentActivity=fragmentActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.manual_indicators_fragment, container, false);

        findViews(v);
        setTextViewsColors();
        init();

        return v;
    }

    public void init(){
        initIMCSubscriber();
        initPreferencesListenner();
        setRunnalbe();
        startScheduler(true);
    }

    public void initPreferencesListenner(){
        ManualIndicatorsPreferencesListenner manualIndicatorsPreferencesListenner = new ManualIndicatorsPreferencesListenner(this);
        ASA.getInstance().getBus().register(manualIndicatorsPreferencesListenner);
    }

    public void initIMCSubscriber(){
        manualIndicatorsFragmentIMCSubscriber = new ManualIndicatorsFragmentIMCSubscriber(this);
        ASA.getInstance().addSubscriber(manualIndicatorsFragmentIMCSubscriber);
    }

    public void findViews(View v){
        leftTextView = (TextView) v.findViewById(R.id.leftTextView);
        rightTextView = (TextView) v.findViewById(R.id.rightTextView);
        centerTextView = (TextView) v.findViewById(R.id.centerTextView);
    }

    public void setTextViewsColors(){
        setBackgroundColors();
        setTextColors();
    }

    public void setBackgroundColors(){
        leftTextView.setBackgroundColor(Color.parseColor("#000000"));//Black
        rightTextView.setBackgroundColor(Color.parseColor("#000000"));//Black
        centerTextView.setBackgroundColor(Color.parseColor("#000000"));//Black
    }

    public void setTextColors(){
        leftTextView.setTextColor(Color.parseColor("#FFFFFF"));//White
        rightTextView.setTextColor(Color.parseColor("#FFFFFF"));//White
        centerTextView.setTextColor(Color.parseColor("#FFFFFF"));//White
    }

    @Override
    public void onResume(){
        super.onResume();
    }


    public void setLeftTextView(final String text){
        fragmentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                leftTextView.setText(text);
            }
        });
    }

    public void setRightTextView(final String text){
        fragmentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                rightTextView.setText(text);
            }
        });
    }

    public void setInterval(int interval) {
        this.interval = interval;
        startScheduler(true);
    }

    public void setCenterTextViewVisibility(final boolean visibility){//View.INVISIBLE View.VISIBLE
        fragmentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (visibility==true){
                    centerTextView.setVisibility(View.VISIBLE);
                    setRightTextView(" " + "Alt:" + " " + "---" + " ");
                    setLeftTextView(" " + "IAS:" + " " + "---" + " ");
                }
                if (visibility==false){
                    centerTextView.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    public void setRunnalbe() {
        runnable = new Runnable() {
            @Override
            public void run() {
                setCenterTextViewVisibility(true);
            }
        };
    }

    public void startScheduler(Boolean visibility){
        setCenterTextViewVisibility(visibility);
        if (handle!=null)
            handle.cancel(true);
        handle = scheduler.scheduleAtFixedRate(runnable,interval,interval,TimeUnit.MILLISECONDS);
    }

}