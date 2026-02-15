package com.osudpotro.posmaster.organization;
import com.osudpotro.posmaster.user.auth.AuthService;
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
public class OrganizationService {
    private final AuthService authService;
    private final OrganizationRepository organizationRepository;
    private final OrganizationMapper organizationMapper;
    private final CsvReader csvReader;

    public List<OrganizationDto> getAllOrganizations() {
        return organizationRepository.findAll()
                .stream()
                .map(organizationMapper::toDto)
                .toList();
    }
    public OrganizationDto createOrganization(OrganizationCreateRequest request){
        if(organizationRepository.existsByName(request.getName())){
            throw new DuplicateOrganizationException();
        }
        var user = authService.getCurrentUser();
        var organization=organizationMapper.toEntity(request);
        organization.setCreatedBy(user);
        organizationRepository.save(organization);
        return organizationMapper.toDto(organization);
    }
    public OrganizationDto updateOrganization(Long organizationId,OrganizationUpdateRequest request){
        var organization= organizationRepository.findById(organizationId).orElseThrow(OrganizationNotFoundException::new);
        var user = authService.getCurrentUser();
        organizationMapper.update(request, organization);
        organization.setUpdatedBy(user);
        organizationRepository.save(organization);
        return organizationMapper.toDto(organization);
    }
    public OrganizationDto getOrganization(Long organizationId){
        var organization=organizationRepository.findById(organizationId).orElseThrow(() -> new OrganizationNotFoundException("Organization not found with ID: " + organizationId));
        return organizationMapper.toDto(organization);
    }
    public Organization getOrganizationEntity(Long organizationId){
        return organizationRepository.findById(organizationId).orElseThrow(() -> new OrganizationNotFoundException("Organization not found with ID: " + organizationId));
    }
    public OrganizationDto activeOrganization(Long organizationId){
        var organization = organizationRepository.findById(organizationId).orElseThrow(() -> new OrganizationNotFoundException("Organization not found with ID: " + organizationId));
        var user = authService.getCurrentUser();
        organization.setStatus(1);
        organization.setUpdatedBy(user);
        organizationRepository.save(organization);
        return organizationMapper.toDto(organization);
    }
    public OrganizationDto deactivateOrganization(Long organizationId){
        var organization=organizationRepository.findById(organizationId).orElseThrow(() -> new OrganizationNotFoundException("Organization not found with ID: " + organizationId));
        var user=authService.getCurrentUser();
        organization.setStatus(2);
        organization.setUpdatedBy(user);
        organizationRepository.save(organization);
        return organizationMapper.toDto(organization);
    }
    public OrganizationDto deleteOrganization(Long organizationId){
        var organization=organizationRepository.findById(organizationId).orElseThrow(() -> new OrganizationNotFoundException("Organization not found with ID: " + organizationId));
        var user=authService.getCurrentUser();
        organization.setStatus(3);
        organization.setUpdatedBy(user);
        organizationRepository.save(organization);
        return organizationMapper.toDto(organization);
    }
    public Page<OrganizationDto> getOrganizations(OrganizationFilter filter, Pageable pageable) {
        return organizationRepository.findAll(OrganizationSpecification.filter(filter), pageable).map(organizationMapper::toDto);
    }
    public int importOrganization(MultipartFile file) {
        var user = authService.getCurrentUser();
        List<String[]> rows = csvReader.readCSV(file);
        boolean hasHeader = true;
        int count = 0;
        List<Organization> organizations = new ArrayList<>();
        for (int i = hasHeader ? 1 : 0; i < rows.size(); i++) {
            String[] cols = rows.get(i);
            // Expecting: name, description
            String name = cols.length > 0 ? cols[0] : null;
            if (name == null || name.trim().isEmpty()) {
                continue; // Skip invalid rows
            }
            Organization organization = new Organization();
            organization.setName(name.trim());
            organization.setCreatedBy(user);
            organizations.add(organization);
            count++;
        }
        organizationRepository.saveAll(organizations);
        return count;
    }
    public int deleteBulkOrganization(List<Long> organizationIds) {
        return organizationRepository.deleteBulkOrganization(organizationIds, 3L);
    }
}