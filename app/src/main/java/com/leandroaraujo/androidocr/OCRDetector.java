package com.leandroaraujo.androidocr;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;

/**
 * Created by jonas.vieira on 05/02/2018.
 */

public class OCRDetector implements Detector.Processor<TextBlock> {

    @Override
    public void release() {

    }

    @Override
    public void receiveDetections(Detector.Detections<TextBlock> detections) {

    }
}
