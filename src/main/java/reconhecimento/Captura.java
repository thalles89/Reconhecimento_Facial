/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reconhecimento;

import java.awt.event.KeyEvent;
import java.util.Scanner;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Rect;
import org.bytedeco.opencv.opencv_core.RectVector;
import org.bytedeco.opencv.opencv_core.Scalar;
import org.bytedeco.opencv.opencv_core.Size;

import static org.bytedeco.opencv.global.opencv_imgcodecs.imwrite;
import static org.bytedeco.opencv.global.opencv_imgproc.COLOR_BGRA2GRAY;
import static org.bytedeco.opencv.global.opencv_imgproc.cvtColor;
import static org.bytedeco.opencv.global.opencv_imgproc.rectangle;
import static org.bytedeco.opencv.global.opencv_imgproc.resize;

import org.bytedeco.opencv.opencv_java;
import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;

/**
 * @author Jones
 */
public class Captura {

    public static void main(String[] args) {
        Loader.load(opencv_java.class);
        KeyEvent tecla = null;
        OpenCVFrameConverter.ToMat converteMat = new OpenCVFrameConverter.ToMat();
        OpenCVFrameGrabber camera = new OpenCVFrameGrabber(0);

        try {
            camera.start();
        } catch (FrameGrabber.Exception e) {
            throw new RuntimeException(e);
        }

        CascadeClassifier detectorFace = new CascadeClassifier("src/main/resources/haarcascade_frontalface_alt.xml");

        CanvasFrame cFrame = new CanvasFrame("Preview", CanvasFrame.getDefaultGamma() / camera.getGamma());
        Frame frameCapturado = null;
        Mat imagemColorida = new Mat();
        int numeroAmostras = 25;
        int amostra = 1;
        System.out.println("Digite seu id: ");
        Scanner cadastro = new Scanner(System.in);
        int idPessoa = cadastro.nextInt();

        while (true) {
            try {
                if ((frameCapturado = camera.grab()) == null) break;
            } catch (FrameGrabber.Exception e) {
                throw new RuntimeException(e);
            }
            imagemColorida = converteMat.convert(frameCapturado);
            Mat imagemCinza = new Mat();
            System.out.println(imagemCinza);
            System.out.println(imagemColorida);
            cvtColor(imagemColorida, imagemCinza, COLOR_BGRA2GRAY);
            RectVector facesDetectadas = new RectVector();

            detectorFace.detectMultiScale(
                    imagemCinza,
                    facesDetectadas,
                    1.1, 1, 0,
                    new Size(150, 150),
                    new Size(500, 500));

            if (tecla == null) {
                try {
                    tecla = cFrame.waitKey(5);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            for (int i = 0; i < facesDetectadas.size(); i++) {
                Rect dadosFace = facesDetectadas.get(0);
                rectangle(imagemColorida, dadosFace, new Scalar(0, 0, 255, 0));
                Mat faceCapturada = new Mat(imagemCinza, dadosFace);
                resize(faceCapturada, faceCapturada, new Size(160, 160));
                if (tecla == null) {
                    try {
                        tecla = cFrame.waitKey(5);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                if (tecla != null) {
                    if (tecla.getKeyChar() == 'q') {
                        if (amostra <= numeroAmostras) {
                            imwrite("src\\fotos\\pessoa." + idPessoa + "." + amostra + ".jpg", faceCapturada);
                            System.out.println("Foto " + amostra + " capturada\n");
                            amostra++;
                        }
                    }
                    tecla = null;
                }
            }
            if (tecla == null) {
                try {
                    tecla = cFrame.waitKey(20);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            if (cFrame.isVisible()) {
                cFrame.showImage(frameCapturado);
            }

            if (amostra > numeroAmostras) {
                break;
            }
        }
        cFrame.dispose();
        try {
            camera.stop();
        } catch (FrameGrabber.Exception e) {
            throw new RuntimeException(e);
        }
    }
}
