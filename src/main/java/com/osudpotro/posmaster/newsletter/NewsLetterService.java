package com.osudpotro.posmaster.newsletter;

import com.osudpotro.posmaster.auth.AuthService;
import com.osudpotro.posmaster.utility.CsvReader;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
@AllArgsConstructor
@Service
public class NewsLetterService {
    private final AuthService authService;
    private final NewsLetterRepository newsLetterRepository;
    private final NewsLetterMapper newsLetterMapper;
    private final CsvReader csvReader;

    public List<NewsLetterDto> gerAllNewsLetters() {
        return newsLetterRepository.findAll()
                .stream()
                .map(newsLetterMapper::toDto)
                .toList();
    }
    public NewsLetterDto createNewsLetter(NewsLetterCreateRequest request){
        if(newsLetterRepository.existsByName(request.getName())){
            throw new DuplicateNewsLetterException();
        }
        var user=authService.getCurrentUser();
        var generic=newsLetterMapper.toEntity(request);
        generic.setCreatedBy(user);
        newsLetterRepository.save(generic);
        return newsLetterMapper.toDto(generic);
    }
    public NewsLetterDto updateNewsLetter(Long genericId, NewsLetterUpdateRequest request){
        var generic= newsLetterRepository.findById(genericId).orElseThrow(NewsLetterNotFoundException::new);
        var user = authService.getCurrentUser();
        newsLetterMapper.update(request, generic);
        generic.setUpdatedBy(user);
        newsLetterRepository.save(generic);
        return newsLetterMapper.toDto(generic);
    }
    public NewsLetterDto getNewsLetter(Long genericId){
        var generic= newsLetterRepository.findById(genericId).orElseThrow(NewsLetterNotFoundException::new);
        return newsLetterMapper.toDto(generic);
    }
    public NewsLetter getNewsLetterEntity(Long genericId){
        return newsLetterRepository.findById(genericId).orElseThrow(NewsLetterNotFoundException::new);
    }
    public NewsLetterDto activeNewsLetter(Long genericId){
        var generic=newsLetterRepository.findById(genericId).orElseThrow(() -> new NewsLetterNotFoundException("NewsLetter not found with ID: " + genericId));
        var user=authService.getCurrentUser();
        generic.setStatus(1);
        generic.setUpdatedBy(user);
        newsLetterRepository.save(generic);
        return newsLetterMapper.toDto(generic);
    }
    public NewsLetterDto deactivateNewsLetter(Long genericId){
        var generic=newsLetterRepository.findById(genericId).orElseThrow(() -> new NewsLetterNotFoundException("NewsLetter not found with ID: " + genericId));
        var user=authService.getCurrentUser();
        generic.setStatus(2);
        generic.setUpdatedBy(user);
        newsLetterRepository.save(generic);
        return newsLetterMapper.toDto(generic);
    }
    public NewsLetterDto deleteNewsLetter(Long genericId){
        var generic=newsLetterRepository.findById(genericId).orElseThrow(() -> new NewsLetterNotFoundException("NewsLetter not found with ID: " + genericId));
        var user=authService.getCurrentUser();
        generic.setStatus(3);
        generic.setUpdatedBy(user);
        newsLetterRepository.save(generic);
        return newsLetterMapper.toDto(generic);
    }
    public Page<NewsLetterDto> getNewsLetters(NewsLetterFilter filter, Pageable pageable) {
        return newsLetterRepository.findAll(NewsLetterSpecification.filter(filter), pageable).map(newsLetterMapper::toDto);
    }

    public int importNewsLetters(MultipartFile file) {
        var user = authService.getCurrentUser();
        List<String[]> rows = csvReader.readCSV(file);
        boolean hasHeader = true;
        int count = 0;
        List<NewsLetter> generics = new ArrayList<>();
        for (int i = hasHeader ? 1 : 0; i < rows.size(); i++) {
            String[] cols = rows.get(i);
            // Expecting: name, description
            String name = cols.length > 0 ? cols[0] : null;
            if (name == null || name.trim().isEmpty()) {
                continue; // Skip invalid rows
            }
            NewsLetter generic = new NewsLetter();
            generic.setName(name.trim());
            generic.setCreatedBy(user);
            generics.add(generic);
            count++;
        }
        newsLetterRepository.saveAll(generics);
        return count;
    }
    public int deleteBulkNewsLetter(List<Long> genericIds) {
        return newsLetterRepository.deleteBulkNewsLetter(genericIds, 3L);
    }
}
