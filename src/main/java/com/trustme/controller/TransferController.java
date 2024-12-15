package com.trustme.controller;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.trustme.dto.request.TransferRequest;
import com.trustme.service.QRCodeService;
import com.trustme.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestController
@RequestMapping("/api")
public class TransferController {
    @Autowired
    QRCodeService qrCodeService;
    @Autowired
    TransferService transferService;
    @GetMapping("/transfer")
    public ResponseEntity<String> getTransfer(){
        return ResponseEntity.ok("Choose an account to transfer money to.");
    }
    @PostMapping("/transfer")
    public ResponseEntity<String> postTransfer(@RequestBody TransferRequest transferRequest){
        return transferService.transferMoney(transferRequest.getAmount(),transferRequest.getReceiver(), transferRequest.getDescription());
    }
    @GetMapping("/qr/generate")
    public ResponseEntity<byte[]> generateQRCode() {
        try {
            byte[] pngData = qrCodeService.generateAccountQRCode("QR");

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"qrcode.png\"")
                    .contentType(MediaType.IMAGE_PNG)
                    .body(pngData);
        } catch (WriterException | IOException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    @PostMapping("/qr/decode")
    public ResponseEntity<String> decodeQRCode(@RequestParam("file") MultipartFile file) {
        try {
            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());

            LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

            Result result = new MultiFormatReader().decode(bitmap);

            return ResponseEntity.ok("QR Code Content: " + result.getText());
        } catch (NotFoundException e) {
            return ResponseEntity.badRequest().body("No QR code found in the image.");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error processing the file.");
        }
    }

}
