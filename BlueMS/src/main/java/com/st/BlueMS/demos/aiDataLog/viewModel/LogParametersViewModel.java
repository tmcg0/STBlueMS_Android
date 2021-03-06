package com.st.BlueMS.demos.aiDataLog.viewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;
import android.support.v4.math.MathUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LogParametersViewModel extends ViewModel {

    public static final float MIN_ENVIRONMENTAL_SAMPLING_HZ = 0.1f;
    public static final float MAX_ENVIRONMENTAL_SAMPLING_HZ = 1.0f;
    public static final float ENVIRONMENTAL_SAMPLING_STEP_HZ = 0.1f;
    public static final float DEFAULT_ENVIRONMENTAL_SAMPLING_HZ = 1.0f;

    public static final float INERTIAL_SAMPLING_VALUES[] = new float[]{13.0f,26.0f,52.0f,104.0f};

    public static final float MIN_INERTIAL_SAMPLING_HZ = INERTIAL_SAMPLING_VALUES[0];
    public static final float MAX_INERTIAL_SAMPLING_HZ = INERTIAL_SAMPLING_VALUES[3];
    public static final int DEFAULT_INERTIAL_INDEX = 3;

    public static final float AUDIO_VOLUME_VALUES[] = new float[]{0.5f,1.0f,1.5f,2.0f};
    public static final float MIN_AUDIO_VOLUME = AUDIO_VOLUME_VALUES[0];
    public static final float MAX_AUDIO_VOLUME = AUDIO_VOLUME_VALUES[3];
    public static final int DEFAULT_AUDIO_INDEX = 1;


    private List<SelectableFeature> mFeatures;
    private MutableLiveData<Float> environmentalSamplingFrequency = new MutableLiveData<>();
    private MutableLiveData<Float> inertialSamplingFrequency = new MutableLiveData<>();
    private MutableLiveData<Float> audioVolume = new MutableLiveData<>();

    private static List<SelectableFeature> buildSelectableFeatures(){
        List<SelectableFeature> list = new ArrayList<>();
        list.add(new SelectableFeature("Temperature (HTS221)", 0x00010000));
        list.add(new SelectableFeature("Temperature (LPS22HB)",0x00040000));
        list.add(new SelectableFeature("Pressure",0x00100000));
        list.add(new SelectableFeature("Humidity",0x00080000));
        list.add(new SelectableFeature("Accelerometer",0x00800000));
        list.add(new SelectableFeature("Gyroscope",0x00400000));
        list.add(new SelectableFeature("Magnetometer",0x00200000));
        list.add(new SelectableFeature("Audio",0x08000000));
        return list;
    }

    public LogParametersViewModel() {
        mFeatures = buildSelectableFeatures();
        environmentalSamplingFrequency.setValue(DEFAULT_ENVIRONMENTAL_SAMPLING_HZ);
        inertialSamplingFrequency.setValue(INERTIAL_SAMPLING_VALUES[DEFAULT_INERTIAL_INDEX]);
        audioVolume.setValue(AUDIO_VOLUME_VALUES[DEFAULT_AUDIO_INDEX]);
    }

    public void setEnvironmentalSamplingFrequency(float value){
        environmentalSamplingFrequency.setValue(
                MathUtils.clamp(value,MIN_ENVIRONMENTAL_SAMPLING_HZ,MAX_ENVIRONMENTAL_SAMPLING_HZ));
    }

    public void setAudioVolume(float value){
        audioVolume.setValue(
                MathUtils.clamp(value,MIN_AUDIO_VOLUME, MAX_AUDIO_VOLUME));
    }

    public void setInertialSamplingFrequency(float value){
        inertialSamplingFrequency.setValue(
                MathUtils.clamp(value,MIN_INERTIAL_SAMPLING_HZ,MAX_INERTIAL_SAMPLING_HZ));
    }

    public float getEnvironmentalSamplingFrequencyOrDefault(){
        Float freq = environmentalSamplingFrequency.getValue();
        if(freq==null)
            return DEFAULT_ENVIRONMENTAL_SAMPLING_HZ;
        return freq;
    }

    public float getAudioSamplingFrequencyOrDefault(){
        Float audioVolume = this.audioVolume.getValue();
        if(audioVolume==null)
            return AUDIO_VOLUME_VALUES[DEFAULT_AUDIO_INDEX];
        return audioVolume;
    }

    public float getInertialSamplingFrequencyOrDefault(){
        Float freq = inertialSamplingFrequency.getValue();
        if(freq==null)
            return INERTIAL_SAMPLING_VALUES[DEFAULT_INERTIAL_INDEX];
        return freq;
    }

    public LiveData<Float> getEnvironmentalSamplingFrequency(){
        return  environmentalSamplingFrequency;
    }

    public LiveData<Float> getInertialSamplingFequency(){
        return inertialSamplingFrequency;
    }

    public List<SelectableFeature> getSupportedFeature(){
        return Collections.unmodifiableList(mFeatures);
    }

    private @Nullable SelectableFeature findItemWithName(CharSequence name){
        for(SelectableFeature f : mFeatures){
            if(f.name.equals(name))
                return f;
        }
        return null;
    }

    public void selectFeatureWithName(CharSequence name){
        SelectableFeature f = findItemWithName(name);
        if(f!=null){
            f.isSelected=true;
        }
    }

    public void deselectFeatureWithName(CharSequence name){
        SelectableFeature f = findItemWithName(name);
        if(f!=null){
            f.isSelected=false;
        }
    }

    public long getSelectedFeatureMask(){
        long mask = 0;
        for( SelectableFeature f : mFeatures){
            if(f.isSelected)
                mask |= f.mask;
        }
        return mask;
    }

    public LiveData<Float> getAudioSamplingFrequency() {
        return audioVolume;
    }
}
