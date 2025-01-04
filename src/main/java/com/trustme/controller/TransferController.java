package com.trustme.controller;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.trustme.dto.request.TransferRequest;
import com.trustme.dto.response.TransferResponse;
import com.trustme.dto.response.TransfersResponse;
import com.trustme.model.PendingTransfer;
import com.trustme.service.AuthService;
import com.trustme.service.QRCodeService;
import com.trustme.service.TransferService;
import com.trustme.service.UserService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class TransferController {
    private static final Logger log = LoggerFactory.getLogger(TransferController.class);

    private final QRCodeService qrCodeService;
    private final TransferService transferService;

    @GetMapping("/transfer")
    public ResponseEntity<String> getTransfer(){
        return ResponseEntity.ok("Choose an account to transfer money to.");
    }

    @PostMapping("/transfer")
    public ResponseEntity<Void> postTransfer(@RequestBody TransferRequest transferRequest){
        // bat dau transaction
        transferService.startProcessingTransfer(transferRequest);
        URI nextStep = URI.create("/api/transfer/step-one");
        log.info("Proceeding to the first transaction step!");
        return ResponseEntity.status(HttpStatus.FOUND).location(nextStep).build();
    }

    @GetMapping("/transfer/step-one")
    public ResponseEntity<String> stepOneGet() {
        return ResponseEntity.status(HttpStatus.FOUND).body("Please confirm your transaction!");

    }
    @PostMapping("/transfer/step-one")
    public ResponseEntity<Void> stepOne() {
        // next step: validate pin ...
        URI nextStep = URI.create("/api/transfer/step-two");
        log.info("Proceeding to the second transaction step!");
        return ResponseEntity.status(HttpStatus.FOUND).location(nextStep).build();
    }
    @GetMapping("/transfer/step-two")
    public ResponseEntity<String> stepTwoGet() {
        return ResponseEntity.status(HttpStatus.FOUND).body("Finishing transaction!");
    }

    @PostMapping("/transfer/step-two")
    public ResponseEntity<TransferResponse> stepTwo() {
        // validate OTP
        PendingTransfer pendingTransfer = transferService.getPendingTransfer();

        TransferResponse transferResponse = transferService.transferMoney(pendingTransfer.getAmount(),
                pendingTransfer.getReceiverName(),
                pendingTransfer.getDescription());
        return ResponseEntity.status(transferResponse.getCode()).body(transferResponse);
    }





    @GetMapping("/qr-transfer")
    public ResponseEntity<String> getQrTransfer(@RequestParam MultipartFile file){
        String response = null;
        try {
            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());

            LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

            Result result = new MultiFormatReader().decode(bitmap);
            response=result.getText();
            } catch (NotFoundException e) {
            return ResponseEntity.badRequest().body("No QR code found in the image.");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error processing the file.");
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/qr/generate")
    public ResponseEntity<byte[]> generateQRCode(@AuthenticationPrincipal Jwt jwt) {
        try {

            byte[] pngData = qrCodeService.generateAccountQRCode(jwt.getSubject());

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
        return qrCodeService.decodeQRCode(file);
    }

}
