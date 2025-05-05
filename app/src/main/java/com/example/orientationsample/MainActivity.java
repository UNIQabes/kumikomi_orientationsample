// OnCreateのgetDefaultSensorやregisterListenerは失敗していないのに、
// TYPE_ACCELEROMETERのセンサー値の変化がonSensorChangedで全く通知されない。 ...(1)
// また、mAccelerationValueを手動で初期化していない ...(2)
// (1)と(2)より、mAccelerationValueが実行中ずっと未初期化の状態になっている。
// そのため、158/186行目のgetRotationMatrixで失敗する。
//　そのため、getOrientationの値が一定の値となってしまっている。

package com.example.orientationsample;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.w3c.dom.Text;



//public class MainActivity extends AppCompatActivity implements SensorEventListener {
//    private CheckBox mCheckBoxOrientation;
//    private TextView mAzimuthText, mPitchText, mRollText;
//    private float[] mAccelerationValue = new float[3];
//    private float[] mGeoMagneticValue = new float[3];
//    private float[] mOrientationValue = new float[3];
//    private float[] mInRotationMatrix = new float[9];
//    private float[] mOutRotationMatrix = new float[9];
//    private float[] mInclinationMatrix = new float[9];
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
//        Sensor accelerationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//        Sensor magneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
//        sensorManager.registerListener(this, accelerationSensor, SensorManager.SENSOR_DELAY_UI);
//        sensorManager.registerListener(this, magneticSensor, SensorManager.SENSOR_DELAY_UI);
//        mAzimuthText = (TextView) findViewById(R.id.text_view_azimuth);
//        mRollText = (TextView) findViewById(R.id.text_view_roll);
//        mPitchText = (TextView) findViewById(R.id.text_view_pitch);
//        Button buttonOrientation = (Button) findViewById(R.id.button_orientation);
//        buttonOrientation.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View argO) {
//                if (!mCheckBoxOrientation.isChecked()) {
//                    SensorManager.getRotationMatrix(mInRotationMatrix,
//                            mInclinationMatrix, mAccelerationValue, mGeoMagneticValue);
//                    SensorManager.remapCoordinateSystem(mInRotationMatrix,
//                            SensorManager.AXIS_X, SensorManager.AXIS_Z, mOutRotationMatrix);
//                    SensorManager.getOrientation(mOutRotationMatrix, mOrientationValue);
//                    String azimuthText
//                            = String.valueOf(Math.floor(Math.toDegrees((double) mOrientationValue[0])));
//                    String pitchText
//                            = String.valueOf(Math.floor(Math.toDegrees((double) mOrientationValue[1])));
//                    String rollText = String.valueOf(Math.floor(Math.toDegrees((double) mOrientationValue[2])));
//                    mAzimuthText.setText(azimuthText);
//                    mPitchText.setText(pitchText);
//                    mRollText.setText(rollText);
//                }
//            }
//        });
//        mCheckBoxOrientation = (CheckBox) findViewById(R.id.checkbox_orientation);
//    }
//
//
//    @Override
//    public void onSensorChanged(SensorEvent sensorEvent) {
//        switch (sensorEvent.sensor.getType()) {
//            case Sensor.TYPE_MAGNETIC_FIELD:
//                mGeoMagneticValue = sensorEvent.values.clone();
//                break;
//            case Sensor.TYPE_ACCELEROMETER:
//                mAccelerationValue = sensorEvent.values.clone();
//                break;
//        }
//        if (mCheckBoxOrientation.isChecked()) {
//            SensorManager.getRotationMatrix(mInRotationMatrix,
//                    mInclinationMatrix, mAccelerationValue, mGeoMagneticValue);
//            SensorManager.remapCoordinateSystem(mInRotationMatrix,
//                    SensorManager.AXIS_X, SensorManager.AXIS_Z, mOutRotationMatrix);
//            SensorManager.getOrientation(mOutRotationMatrix, mOrientationValue);
//            String azimuthText
//                    = String.valueOf(Math.floor(Math.toDegrees((double) mOrientationValue[0])));
//            String pitchText
//                    = String.valueOf(Math.floor(Math.toDegrees((double) mOrientationValue[1])));
//            String rollText
//                    = String.valueOf(Math.floor(Math.toDegrees((double) mOrientationValue[2])));
//            mAzimuthText.setText(azimuthText);
//            mPitchText.setText(pitchText);
//            mRollText.setText(rollText);
//        }
//    }
//
//    @Override
//    public void onAccuracyChanged(Sensor sensor, int i) {
//    }
//}



public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private CheckBox mCheckBoxOrientation;
    private TextView mAzimuthText, mPitchText, mRollText;
    private float[] mAccelerationValue = new float[3];
    private float[] mGeoMagneticValue =new float[3];
    private  float[] mOrientationValue=new float[3];
    private float[] minRotationMatrix = new float[9];
    private  float[] mOutRotationMatrix=new float[9];
    private  float[] mInclinationMatrix=new float[9];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mAccelerationValue[0]=0;
        //mAccelerationValue[1]=0;
        //mAccelerationValue[2]=9.8f;
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor accelerationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Log.d("validation","acc_isexist:"+(accelerationSensor!=null));
        Sensor magneticSensor=sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        boolean ret;
        ret=sensorManager.registerListener(this,magneticSensor,SensorManager.SENSOR_DELAY_UI);
        Log.d("validation","mag_vali:"+ret);
        ret=sensorManager.registerListener(this,accelerationSensor,SensorManager.SENSOR_DELAY_UI);
        Log.d("validation","acc_vali:"+ret);

        mAzimuthText=(TextView) findViewById(R.id.text_view_azimuth);
        mRollText=(TextView) findViewById(R.id.text_view_roll);
        mPitchText=(TextView) findViewById(R.id.text_view_pitch);
        Button buttonOrientation = (Button) findViewById(R.id.button_orientation);
        buttonOrientation.setOnClickListener(new OnClickListener(){
            public void onClick(View arg0){
                if(!mCheckBoxOrientation.isChecked()){
                    Log.d("sensorevent2","書き換えます!");
                    boolean ret=SensorManager.getRotationMatrix(minRotationMatrix,mInclinationMatrix,mAccelerationValue,mGeoMagneticValue);
                    Log.d("sensorevent2",""+ret+minRotationMatrix[0]+minRotationMatrix[1]+minRotationMatrix[2]+minRotationMatrix[3]+minRotationMatrix[4]+minRotationMatrix[5]+minRotationMatrix[6]+minRotationMatrix[7]+minRotationMatrix[8]);
                    ret=SensorManager.remapCoordinateSystem(minRotationMatrix,SensorManager.AXIS_X,SensorManager.AXIS_Z,mOutRotationMatrix);
                    Log.d("sensorevent2",""+ret+mOutRotationMatrix[0]+mOutRotationMatrix[1]+mOutRotationMatrix[2]+mOutRotationMatrix[3]+mOutRotationMatrix[4]+mOutRotationMatrix[5]+mOutRotationMatrix[6]+mOutRotationMatrix[7]+mOutRotationMatrix[8]);
                    SensorManager.getOrientation(mOutRotationMatrix,mOrientationValue);
                    String azimuthText=String.valueOf(Math.floor(Math.toDegrees((double)mOrientationValue[0])));
                    String pitchText=String.valueOf(Math.floor(Math.toDegrees((double) mOrientationValue[1])));
                    String rollText=String.valueOf(Math.floor(Math.toDegrees((double) mOrientationValue[2])));
                    Log.d("sensorevent2",""+mOrientationValue[0]+mOrientationValue[1]+mOrientationValue[2]);
                    mAzimuthText.setText(azimuthText);
                    mPitchText.setText(pitchText);
                    mRollText.setText(rollText);
                }
            }
        });
        mCheckBoxOrientation = (CheckBox)findViewById(R.id.checkbox_orientation);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent){
        Log.d("sensorevent",sensorEvent.sensor.getStringType()+sensorEvent.values[0]+sensorEvent.values[1]+sensorEvent.values[2]);

        switch (sensorEvent.sensor.getType()){
            case Sensor.TYPE_MAGNETIC_FIELD:mGeoMagneticValue=sensorEvent.values.clone();break;
            case Sensor.TYPE_ACCELEROMETER:mAccelerationValue= sensorEvent.values.clone();break;
        }
        if(mCheckBoxOrientation.isChecked()){
            Log.d("sensorevent2","書き換えます!");
            boolean ret=SensorManager.getRotationMatrix(minRotationMatrix,mInclinationMatrix,mAccelerationValue,mGeoMagneticValue);
            Log.d("sensorevent2",""+ret+minRotationMatrix[0]+minRotationMatrix[1]+minRotationMatrix[2]+minRotationMatrix[3]+minRotationMatrix[4]+minRotationMatrix[5]+minRotationMatrix[6]+minRotationMatrix[7]+minRotationMatrix[8]);
            ret=SensorManager.remapCoordinateSystem(minRotationMatrix,SensorManager.AXIS_X,SensorManager.AXIS_Z,mOutRotationMatrix);
            Log.d("sensorevent2",""+ret+mOutRotationMatrix[0]+mOutRotationMatrix[1]+mOutRotationMatrix[2]+mOutRotationMatrix[3]+mOutRotationMatrix[4]+mOutRotationMatrix[5]+mOutRotationMatrix[6]+mOutRotationMatrix[7]+mOutRotationMatrix[8]);
            SensorManager.getOrientation(mOutRotationMatrix,mOrientationValue);
            String azimuthText=String.valueOf(Math.floor(Math.toDegrees((double)mOrientationValue[0])));
            String pitchText=String.valueOf(Math.floor(Math.toDegrees((double) mOrientationValue[1])));
            String rollText=String.valueOf(Math.floor(Math.toDegrees((double) mOrientationValue[2])));
            Log.d("sensorevent2",""+mOrientationValue[0]+mOrientationValue[1]+mOrientationValue[2]);
            mAzimuthText.setText(azimuthText);
            mPitchText.setText(pitchText);
            mRollText.setText(rollText);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i){

    }
}
