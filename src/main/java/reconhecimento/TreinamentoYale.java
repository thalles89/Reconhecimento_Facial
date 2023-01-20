package reconhecimento;

import java.io.File;
import java.nio.IntBuffer;

import static org.bytedeco.opencv.global.opencv_core.*; 
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.MatVector;
import org.bytedeco.opencv.opencv_core.Size;
import org.bytedeco.opencv.opencv_face.*;
import static org.bytedeco.opencv.global.opencv_imgcodecs.*;
import static org.bytedeco.opencv.global.opencv_imgproc.resize;

public class TreinamentoYale {
    public static void main(String[] args) {
        File diretorio = new File("src\\yalefaces\\treinamento");
        File[] arquivos = diretorio.listFiles();
        MatVector fotos = new MatVector(arquivos.length);
        Mat rotulos = new Mat(arquivos.length, 1, CV_32SC1);
        IntBuffer rotulosBuffer = rotulos.createBuffer();
        int contador = 0;
      
        for (File imagem : arquivos) {
            Mat foto = imread(imagem.getAbsolutePath(), IMREAD_GRAYSCALE);
            int classe = Integer.parseInt(imagem.getName().substring(7,9));
            resize(foto, foto, new Size(160, 160));
            fotos.put(contador, foto);
            rotulosBuffer.put(contador, classe);
            contador++;
        }
                                                                                // MUDANÇAS
        FaceRecognizer eigenface = EigenFaceRecognizer.create(30, 0);            // antes: createEigenFaceRecognizer();
        FaceRecognizer fisherface = FisherFaceRecognizer.create(30, 0);          // antes: createFisherFaceRecognizer();
        FaceRecognizer lbph = LBPHFaceRecognizer.create(12, 10, 15, 15, 0);      // antes: createLBPHFaceRecognizer();

        eigenface.train(fotos, rotulos);
        eigenface.save("src\\recursos\\classificadorEigenfacesYale.yml");
        
        fisherface.train(fotos, rotulos);
        fisherface.save("src\\recursos\\classificadorFisherfacesYale.yml");
        
        lbph.train(fotos, rotulos);
        lbph.save("src\\recursos\\classificadorLBPHYale.yml");
    }
}