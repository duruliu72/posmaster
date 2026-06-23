package com.osudpotro.posmaster.ocr;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@RestController
@RequestMapping("/ocr-doc")
public class OcrController {
    @PostMapping("/upload")
    public void createMultimedia(@RequestParam("filepond") MultipartFile file){
        ITesseract tesseract = new Tesseract();
    }
}
