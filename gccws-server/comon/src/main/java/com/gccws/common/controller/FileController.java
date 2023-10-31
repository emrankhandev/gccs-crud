package com.gccws.common.controller;


import com.gccws.common.utils.CommonUtils;
import com.gccws.common.utils.CommonConstants;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author  Md. Mizanur Rahman
 * @Email   kmmizanurrahmanjp@gmail.com
 * @Since   March 09, 2023
 * @version 1.0.0
 */

@AllArgsConstructor
@RestController
@RequestMapping(CommonConstants.COMMON_END_POINT + "file")
public class FileController {

    private static final Logger LOG = LoggerFactory.getLogger(FileController.class);
    private final CommonUtils commonUtils;


    @GetMapping("/view/{filename}")
    public ResponseEntity<?> viewFile(@PathVariable("filename") String fileName){
        try {
            String fileExtension = fileName.substring(fileName.lastIndexOf("."));
            Resource fileUrl = commonUtils.getFileFromServer(fileName);
            if(!fileUrl.exists()){
                fileUrl = commonUtils.getImageFromServer(fileName);
            }
            if(fileExtension.equalsIgnoreCase(".pdf")){
                return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/pdf"))
                        .body(fileUrl);
            }else if(fileExtension.equalsIgnoreCase(".png") || fileExtension.equalsIgnoreCase(".jpg") || fileExtension.equalsIgnoreCase(".jpeg")){
                return ResponseEntity.ok().contentType(MediaType.parseMediaType("image/png"))
                    .body(fileUrl);
            }else if(fileExtension.equalsIgnoreCase(".docx")) {
                return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document"))
                        .body(fileUrl);
            }else if(fileExtension.equalsIgnoreCase(".xlsx")) {
                return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/xlsx"))
                        .body(fileUrl);
            }else {
                return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/octet-stream"))
                        .body(fileUrl);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/view/image/{filename}")
    public ResponseEntity<?> viewImageUrl(@PathVariable("filename") String fileName){
        try {
            return ResponseEntity.ok().contentType(MediaType.parseMediaType("image/png"))
                    .body(commonUtils.getImageFromServer(fileName));
        } catch (Exception e1) {
            e1.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/view/pdf/{filename}")
    public ResponseEntity<?> viewPdfUrl(@PathVariable("filename") String fileName){
        try {
            return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/pdf"))
                    .body(commonUtils.getFileFromServer(fileName));
        } catch (Exception e1) {
            e1.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }



}
