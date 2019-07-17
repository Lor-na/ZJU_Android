package com.bytedance.camera.demo;

import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.spec.ECField;
import java.util.Arrays;
import java.util.List;

import static com.bytedance.camera.demo.utils.Utils.MEDIA_TYPE_IMAGE;
import static com.bytedance.camera.demo.utils.Utils.MEDIA_TYPE_VIDEO;
import static com.bytedance.camera.demo.utils.Utils.getOutputMediaFile;

public class CustomCameraActivity extends AppCompatActivity {

    private SurfaceView mSurfaceView;
    private Camera mCamera;

    private Button mRecordBtn;
    private Button mDelayBtn;
    private Button mPicBtn;
    private Button mFacingBtn;
    private Button mPauseBtn;

    private int CAMERA_TYPE = Camera.CameraInfo.CAMERA_FACING_BACK;

    private boolean isRecording = false;
    private boolean onFlashLight = false;
    private boolean onPause = false;

    private int rotationDegree = 0;

    private int DELAY_TIME = 10000;
    private int TIME_INTERVAL = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_custom_camera);

        mCamera = getCamera(CAMERA_TYPE);

        mSurfaceView = findViewById(R.id.img);
        //todo 给SurfaceHolder添加Callback
        SurfaceHolder mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try{
                    mCamera.setPreviewDisplay(holder);
                    mCamera.startPreview();
                }catch (Exception e){
                    Toast.makeText(CustomCameraActivity.this, "Preview in suface failed",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
            }
        });

        mPicBtn = findViewById(R.id.btn_picture);
        mPicBtn.setOnClickListener(v -> {
            //todo 拍一张照片
            mCamera.takePicture(null, null, mPicture);
        });

        mRecordBtn = findViewById(R.id.btn_record);
        mRecordBtn.setOnClickListener(v -> {

            //todo 录制，第一次点击是start，第二次点击是stop

            final List<Button> buttonList = Arrays.asList(mPicBtn, mFacingBtn, mDelayBtn);

            if (isRecording) {
                //todo 停止录制
                releaseMediaRecorder();
                mRecordBtn.setText("Record");
                enableButton(buttonList);
                mPauseBtn.setVisibility(View.GONE);
                isRecording = false;
            } else {
                //todo 录制
                if(prepareVideoRecorder()){
                    isRecording = true;
                    disableButton(buttonList);
                    mRecordBtn.setText("Stop");
                    mPauseBtn.setVisibility(View.VISIBLE);
                }
            }
        });

        mFacingBtn = findViewById(R.id.btn_facing);
        mFacingBtn.setOnClickListener(v -> {
            //todo 切换前后摄像头
            if(CAMERA_TYPE == Camera.CameraInfo.CAMERA_FACING_FRONT){
                mCamera = getCamera(Camera.CameraInfo.CAMERA_FACING_BACK);
            } else {
                mCamera = getCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);
            }
            startPreview(mSurfaceHolder);
        });

        findViewById(R.id.btn_zoom).setOnClickListener(v -> {
            //todo 调焦，需要判断手机是否支持
            mCamera.cancelAutoFocus();
            mCamera.getParameters().setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            mCamera.autoFocus(new Camera.AutoFocusCallback() {
                @Override
                public void onAutoFocus(boolean success, Camera camera) {

                }
            });
        });

        findViewById(R.id.btn_light).setOnClickListener(v -> {
            Camera.Parameters parameter = mCamera.getParameters();
            if(onFlashLight){
                parameter.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                onFlashLight = false;
            } else {
                parameter.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                onFlashLight = true;
            }
            mCamera.setParameters(parameter);
        });

        mDelayBtn = findViewById(R.id.btn_delay);
        mDelayBtn.setOnClickListener(v -> {

            final List<Button> buttonList = Arrays.asList(mPicBtn, mRecordBtn, mFacingBtn, mDelayBtn);
            disableButton(buttonList);

            new CountDownTimer(DELAY_TIME, TIME_INTERVAL){
                @Override
                public void onTick(long millisUntilFinished) {
                    mDelayBtn.setText(millisUntilFinished / TIME_INTERVAL + "");
                }

                @Override
                public void onFinish() {
                    mDelayBtn.setText("delay");
                    mCamera.takePicture(null, null, mPicture);
                    enableButton(buttonList);
                }
            }.start();
        });

        mPauseBtn = findViewById(R.id.btn_pause);
        mPauseBtn.setOnClickListener(v -> {
            if(onPause){
                mPauseBtn.setText("pause");
                mMediaRecorder.resume();
                onPause = false;
            } else {
                mPauseBtn.setText("resume");
                mMediaRecorder.pause();
                onPause = true;
            }
        });
        mPauseBtn.setVisibility(View.GONE);
    }

    private void enableButton(final List<Button> buttonList){
        for(int i = 0; i < buttonList.size(); i++){
            buttonList.get(i).setEnabled(true);
        }
    }

    private void disableButton(final List<Button> buttonList){
        for(int i = 0; i < buttonList.size(); i++){
            buttonList.get(i).setEnabled(false);
        }
    }

    public Camera getCamera(int position) {
        CAMERA_TYPE = position;
        if (mCamera != null) {
            releaseCameraAndPreview();
        }
        Camera cam = Camera.open(position);

        //todo 摄像头添加属性，例是否自动对焦，设置旋转方向等

        cam.getParameters().setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);

        rotationDegree = getCameraDisplayOrientation(position);
        cam.setDisplayOrientation(rotationDegree);

        return cam;
    }


    private static final int DEGREE_90 = 90;
    private static final int DEGREE_180 = 180;
    private static final int DEGREE_270 = 270;
    private static final int DEGREE_360 = 360;

    private int getCameraDisplayOrientation(int cameraId) {
        android.hardware.Camera.CameraInfo info =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = DEGREE_90;
                break;
            case Surface.ROTATION_180:
                degrees = DEGREE_180;
                break;
            case Surface.ROTATION_270:
                degrees = DEGREE_270;
                break;
            default:
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % DEGREE_360;
            result = (DEGREE_360 - result) % DEGREE_360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + DEGREE_360) % DEGREE_360;
        }
        return result;
    }


    private void releaseCameraAndPreview() {
        //todo 释放camera资源
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
    }

    Camera.Size size;

    private void startPreview(SurfaceHolder holder) {
        //todo 开始预览
        try{
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (Exception e) {
            Toast.makeText(CustomCameraActivity.this, "Preview in suface failed",
                    Toast.LENGTH_SHORT).show();
        }
    }


    private MediaRecorder mMediaRecorder;

    private boolean prepareVideoRecorder() {
        //todo 准备MediaRecorder
        mMediaRecorder = new MediaRecorder();

        mCamera.unlock();
        mMediaRecorder.setCamera(mCamera);

        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));

        mMediaRecorder.setOutputFile(getOutputMediaFile(MEDIA_TYPE_VIDEO).toString());

        mMediaRecorder.setPreviewDisplay(mSurfaceView.getHolder().getSurface());
        mMediaRecorder.setOrientationHint(rotationDegree);

        try{
            mMediaRecorder.prepare();
            mMediaRecorder.start();
        } catch (Exception e) {
            releaseMediaRecorder();
            Toast.makeText(CustomCameraActivity.this, "Prepare recorder failed",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    private void releaseMediaRecorder() {
        //todo 释放MediaRecorder
        mMediaRecorder.stop();
        mMediaRecorder.reset();
        mMediaRecorder.release();
        mMediaRecorder = null;
        mCamera.lock();
    }


    private Camera.PictureCallback mPicture = (data, camera) -> {
        File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
        if (pictureFile == null) {
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            fos.write(data);
            fos.close();
            Toast.makeText(CustomCameraActivity.this, "Picture saved successfully",
                    Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.d("mPicture", "Error accessing file: " + e.getMessage());
        }

        mCamera.startPreview();
    };


    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) h / w;

        if (sizes == null) return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = Math.min(w, h);

        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

}
