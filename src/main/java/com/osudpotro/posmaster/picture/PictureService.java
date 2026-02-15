package com.osudpotro.posmaster.picture;

import com.osudpotro.posmaster.user.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

//@AllArgsConstructor
@Service
public class PictureService {
    @Autowired
    private PictureRepository pictureRepository;
    @Autowired
    private  PictureMapper pictureMapper;
    @Autowired
    private AuthService authService;
    @Value("${file.upload-dir}")
    private String rootDir;
    public List<PictureDto> getAllPictures(){
        return pictureRepository.findAll()
                .stream()
                .map(pictureMapper::toDto)
                .toList();
    }
    public PictureDto createPicture(MultipartFile file) {
        String uploadDir=rootDir+"/uploads";
        var user = authService.getCurrentUser();
        Picture picture= new Picture();
        // 1. Ensure upload folder exists
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            try {
                Files.createDirectories(uploadPath);
            } catch (Exception e) {
                throw new DirectoryAlreadyExist();
            }
        }
        // 2. Unique file name
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        // 3. Save file
        Path filePath = uploadPath.resolve(fileName);
        try {
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            picture.setName(file.getOriginalFilename());
            picture.setImageUrl(fileName);
        } catch (Exception e) {
            throw new CopyFileException();
        }

        picture.setCreatedBy(user);
        pictureRepository.save(picture);
        return pictureMapper.toDto(picture);
    }
    public PictureDto createPictureFile(PictureCreateRequest request) {
        String uploadDir=rootDir+"/uploads";
        var user = authService.getCurrentUser();
        MultipartFile file = request.getFilepond();
        if (file == null || file.isEmpty()) {
            throw new FileRequiredException();
        }

        Picture picture= new Picture();
        // 1. Ensure upload folder exists
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            try {
                Files.createDirectories(uploadPath);
            } catch (Exception e) {
                throw new DirectoryAlreadyExist();
            }
        }
        // 2. Unique file name
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        // 3. Save file
        Path filePath = uploadPath.resolve(fileName);
        try {
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            picture.setName(file.getOriginalFilename());
            picture.setImageUrl(fileName);
        } catch (Exception e) {
            throw new CopyFileException();
        }

        picture.setCreatedBy(user);
        pictureRepository.save(picture);
        return pictureMapper.toDto(picture);
    }
    public PictureDto updatePicture(Long pictureId, PictureUpdateRequest request){

        var picture= pictureRepository.findById(pictureId).orElseThrow(PictureNotFoundException::new);
        var user = authService.getCurrentUser();
        pictureMapper.update(request, picture);
        picture.setUpdatedBy(user);
        pictureRepository.save(picture);
        return pictureMapper.toDto(picture);
    }

    public PictureDto getPicture(Long pictureId){
        var picture= pictureRepository.findById(pictureId).orElseThrow(() -> new PictureNotFoundException("Picture not found with ID: " + pictureId));
        return pictureMapper.toDto(picture);
    }
    public Picture getPictureEntity(Long pictureId){
        return pictureRepository.findById(pictureId).orElseThrow(() -> new PictureNotFoundException("Picture not found with ID: " + pictureId));
    }
    public PictureDto activatePicture(Long pictureId){
        var picture = pictureRepository.findById(pictureId).orElseThrow(() -> new PictureNotFoundException("Picture not found with ID: " + pictureId));
        var user = authService.getCurrentUser();
        picture.setStatus(1);
        picture.setUpdatedBy(user);
        pictureRepository.save(picture);
        return pictureMapper.toDto(picture);
    }
    public PictureDto deactivatePicture(Long pictureId){
        var picture= pictureRepository.findById(pictureId).orElseThrow(() -> new PictureNotFoundException("Picture not found with ID: " + pictureId));
        var user=authService.getCurrentUser();
        picture.setStatus(2);
        picture.setUpdatedBy(user);
        pictureRepository.save(picture);
        return pictureMapper.toDto(picture);
    }
    public PictureDto deletePicture(Long pictureId){
        var picture= pictureRepository.findById(pictureId).orElseThrow(() -> new PictureNotFoundException("Picture not found with ID: " + pictureId));
        var user=authService.getCurrentUser();
        picture.setStatus(3);
        picture.setUpdatedBy(user);
        pictureRepository.save(picture);
        return pictureMapper.toDto(picture);
    }

}
