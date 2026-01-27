package com.osudpotro.posmaster.tag;

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
public class TagService {
    private final AuthService authService;
    private final TagRepository tagRepository;
    private final TagMapper tagMapper;
    private final CsvReader csvReader;

    public List<TagDto> getAllTags() {
        return tagRepository.findAll()
                .stream()
                .map(tagMapper::toDto)
                .toList();
    }
    public TagDto createTag(TagCreateRequest request){
        if(tagRepository.existsByName(request.getName())){
            throw new DuplicateTagException();
        }
        var user = authService.getCurrentUser();
        var organization=tagMapper.toEntity(request);
        organization.setCreatedBy(user);
        tagRepository.save(organization);
        return tagMapper.toDto(organization);
    }
    public TagDto updateTag(Long organizationId,TagUpdateRequest request){
        var organization= tagRepository.findById(organizationId).orElseThrow(TagNotFoundException::new);
        var user = authService.getCurrentUser();
        tagMapper.update(request, organization);
        organization.setUpdatedBy(user);
        tagRepository.save(organization);
        return tagMapper.toDto(organization);
    }
    public TagDto getTag(Long organizationId){
        var organization=tagRepository.findById(organizationId).orElseThrow(() -> new TagNotFoundException("Tag not found with ID: " + organizationId));
        return tagMapper.toDto(organization);
    }
    public Tag getTagEntity(Long organizationId){
        return tagRepository.findById(organizationId).orElseThrow(() -> new TagNotFoundException("Tag not found with ID: " + organizationId));
    }
    public TagDto activeTag(Long organizationId){
        var organization = tagRepository.findById(organizationId).orElseThrow(() -> new TagNotFoundException("Tag not found with ID: " + organizationId));
        var user = authService.getCurrentUser();
        organization.setStatus(1);
        organization.setUpdatedBy(user);
        tagRepository.save(organization);
        return tagMapper.toDto(organization);
    }
    public TagDto deactivateTag(Long organizationId){
        var organization=tagRepository.findById(organizationId).orElseThrow(() -> new TagNotFoundException("Tag not found with ID: " + organizationId));
        var user=authService.getCurrentUser();
        organization.setStatus(2);
        organization.setUpdatedBy(user);
        tagRepository.save(organization);
        return tagMapper.toDto(organization);
    }
    public TagDto deleteTag(Long organizationId){
        var organization=tagRepository.findById(organizationId).orElseThrow(() -> new TagNotFoundException("Tag not found with ID: " + organizationId));
        var user=authService.getCurrentUser();
        organization.setStatus(3);
        organization.setUpdatedBy(user);
        tagRepository.save(organization);
        return tagMapper.toDto(organization);
    }
    public Page<TagDto> getTags(TagFilter filter, Pageable pageable) {
        return tagRepository.findAll(TagSpecification.filter(filter), pageable).map(tagMapper::toDto);
    }
    public int importTag(MultipartFile file) {
        var user = authService.getCurrentUser();
        List<String[]> rows = csvReader.readCSV(file);
        boolean hasHeader = true;
        int count = 0;
        List<Tag> tags = new ArrayList<>();
        for (int i = hasHeader ? 1 : 0; i < rows.size(); i++) {
            String[] cols = rows.get(i);
            // Expecting: name, description
            String name = cols.length > 0 ? cols[0] : null;
            if (name == null) {
                continue; // Skip invalid rows
            }
            name = name.replaceAll("^(\"{1,2})|(\\\"{1,2})$", "");
            name = name.replaceAll("^(\"{1,2})|(\\\"{1,2})$", "");
            if(name.trim().isEmpty()){
                continue; // Skip invalid rows
            }
            String finalName = name;
            boolean exists = tags.stream()
                    .anyMatch(m -> m.getName().equals(finalName));

            if (exists) {
                continue; // Skip invalid rows
            }
            Tag tag = new Tag();
            tag.setName(name.trim());
            //For Alias
            String alias = name.replace(" ", "-").toLowerCase();
            tag.setAlias(alias);
            tag.setCreatedBy(user);
            tags.add(tag);
            count++;
        }
        tagRepository.saveAll(tags);
        return count;
    }
    public int deleteBulkTag(List<Long> organizationIds) {
        return tagRepository.deleteBulkTag(organizationIds, 3L);
    }
}
