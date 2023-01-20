/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reconhecimento;

import org.opencv.face.EigenFaceRecognizer;
import org.opencv.face.FaceRecognizer;

/**
 *
 * @author Jones
 */
public class TesteJavaCV {
    public static void main(String args[]) {
        FaceRecognizer r = EigenFaceRecognizer.create();
    }
}
