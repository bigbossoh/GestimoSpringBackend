package com.bzdata.gestimospringbackend.Services.Impl;

import static com.bzdata.gestimospringbackend.constant.SecurityConstant.APP_ROOT;

import java.io.IOException;
import java.util.Optional;

import com.bzdata.gestimospringbackend.Models.ImageData;
import com.bzdata.gestimospringbackend.Services.StorageService;
import com.bzdata.gestimospringbackend.Utils.ImageUtils;
import com.bzdata.gestimospringbackend.repository.StorageRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StorageServiceImpl implements StorageService {
    final StorageRepository repository;
    @Override
    public String uploadImage(MultipartFile file) throws IOException {
        ImageData imageData=new ImageData();
        imageData.setNameImage(file.getOriginalFilename());
        imageData.setTypeImage(file.getOriginalFilename());
        imageData.setProfileAgenceImageUrl(APP_ROOT +"/images/"+file.getOriginalFilename());
        imageData.setImageData(ImageUtils.compressImage(file.getBytes()));
        repository.save(imageData);
        if (imageData != null) {
            return "file uploaded successfully : " + file.getOriginalFilename();
        }
        return null;
    }

    @Override
    public byte[] downloadImage(String fileName) {
        Optional<ImageData> dbImageData = repository.findByNameImage(fileName);
        byte[] images=ImageUtils.decompressImage(dbImageData.get().getImageData());
        return images;
    }

//     @Override
//     public String uploadImageToFileSystem(MultipartFile file) throws IOException {


//         String filePath=FOLDER_PATH+file.getOriginalFilename();
//         FileData fileData= new FileData();
//         fileData.setNameFileData(file.getOriginalFilename());
//         fileData.setTypeFileData(file.getContentType());
//         fileData.setFilePathFileData(filePath);
//         FileData save = fileDataRepository.save(fileData);
// //        FileData fileData=fileDataRepository.save(FileData.builder()
// //                .nameFileData(file.getOriginalFilename())
// //                .typeFileData(file.getContentType())
// //                .filePathFileData(filePath).build());

//         file.transferTo(new File(filePath));

//         if (fileData != null) {
//             return "file uploaded successfully : " + filePath;
//         }
//         return null;
//     }

    // @Override
    // public byte[] downloadImageFromFileSystem(String fileName) throws IOException {
    //     log.info("we are in downloadImageFromFileSystem");
    //     FileData fileData = fileDataRepository.findByNameFileData(fileName).orElse(null);
    //     if(fileData !=null){
    //         System.out.println("here is "+fileName);
    //     String filePath=fileData.getFilePathFileData();
    //     System.out.println(filePath);
    //     byte[] images = Files.readAllBytes(new File(filePath).toPath());
    //     log.info("file name {}",filePath);
    //     return images;}
    //     else{
    //         System.out.println("here is null "+fileName);
    //         return null;
    //     }
  //  }

}
