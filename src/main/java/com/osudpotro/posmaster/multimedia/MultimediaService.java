package com.osudpotro.posmaster.multimedia;

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

@Service
public class MultimediaService {
    @Autowired
    private MultimediaRepository multimediaRepository;
    @Autowired
    private MultimediaMapper multimediaMapper;
    @Autowired
    private AuthService authService;
    @Value("${file.upload-dir}")
    private String rootDir;

    public List<MultimediaDto> getAllMultimedias() {
        return multimediaRepository.findAll()
                .stream()
                .map(multimediaMapper::toDto)
                .toList();
    }

    public List<MultimediaDto> getMultimediaListByIds(List<Long> multimediaIds) {
        return multimediaRepository.getMultimediaListByIds(multimediaIds, 3L).stream()
                .map(multimediaMapper::toDto)
                .toList();
    }

    public MultimediaDto createMultimedia(MultipartFile file) {
        String uploadDir = rootDir + "/uploads";
        var user = authService.getCurrentUser();
        Multimedia multimedia = new Multimedia();
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
            multimedia.setName(file.getOriginalFilename());
            multimedia.setImageUrl(fileName);
        } catch (Exception e) {
            throw new CopyFileException();
        }

        multimedia.setCreatedBy(user);
        multimediaRepository.save(multimedia);
        return multimediaMapper.toDto(multimedia);
    }

    public MultimediaDto createMultimediaFile(MultimediaCreateRequest request) {
        String uploadDir = rootDir + "/uploads";
        var user = authService.getCurrentUser();
        MultipartFile file = request.getFilepond();
        if (file == null || file.isEmpty()) {
            throw new FileRequiredException();
        }

        Multimedia multimedia = new Multimedia();
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
            multimedia.setName(file.getOriginalFilename());
            multimedia.setImageUrl(fileName);
        } catch (Exception e) {
            throw new CopyFileException();
        }
        if (request.getMediaType() != null) {
            multimedia.setMediaType(request.getMediaType());
        }
        multimedia.setCreatedBy(user);
        multimediaRepository.save(multimedia);
        return multimediaMapper.toDto(multimedia);
    }

    public MultimediaDto updateMultimedia(Long multimedia, MultimediaUpdateRequest request) {
        var picture = multimediaRepository.findById(multimedia).orElseThrow(MultimediaNotFoundException::new);
        var user = authService.getCurrentUser();
        multimediaMapper.update(request, picture);
        picture.setUpdatedBy(user);
        multimediaRepository.save(picture);
        return multimediaMapper.toDto(picture);
    }

    public MultimediaDto getMultimedia(Long multimedia) {
        var picture = multimediaRepository.findById(multimedia).orElseThrow(() -> new MultimediaNotFoundException("Multimedia not found with ID: " + multimedia));
        return multimediaMapper.toDto(picture);
    }

    public Multimedia getMultimediaEntity(Long multimedia) {
        return multimediaRepository.findById(multimedia).orElseThrow(() -> new MultimediaNotFoundException("Multimedia not found with ID: " + multimedia));
    }

    public MultimediaDto activateMultimedia(Long multimedia) {
        var picture = multimediaRepository.findById(multimedia).orElseThrow(() -> new MultimediaNotFoundException("Multimedia not found with ID: " + multimedia));
        var user = authService.getCurrentUser();
        picture.setStatus(1);
        picture.setUpdatedBy(user);
        multimediaRepository.save(picture);
        return multimediaMapper.toDto(picture);
    }

    public MultimediaDto deactivateMultimedia(Long multimedia) {
        var picture = multimediaRepository.findById(multimedia).orElseThrow(() -> new MultimediaNotFoundException("Multimedia not found with ID: " + multimedia));
        var user = authService.getCurrentUser();
        picture.setStatus(2);
        picture.setUpdatedBy(user);
        multimediaRepository.save(picture);
        return multimediaMapper.toDto(picture);
    }

    public MultimediaDto deleteMultimedia(Long multimedia) {
        var picture = multimediaRepository.findById(multimedia).orElseThrow(() -> new MultimediaNotFoundException("Multimedia not found with ID: " + multimedia));
        var user = authService.getCurrentUser();
        picture.setStatus(3);
        picture.setUpdatedBy(user);
        multimediaRepository.save(picture);
        return multimediaMapper.toDto(picture);
    }


}
