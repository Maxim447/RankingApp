package ru.hse.rankingapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hse.rankingapp.dto.aboutus.AboutUsInfoDto;
import ru.hse.rankingapp.dto.paging.PageRequestDto;
import ru.hse.rankingapp.dto.paging.PageResponseDto;
import ru.hse.rankingapp.dto.partner.PartnerCreateDto;
import ru.hse.rankingapp.dto.partner.PartnerInfoDto;
import ru.hse.rankingapp.dto.partner.PartnerUpdateDto;
import ru.hse.rankingapp.dto.sponsor.SponsorCreateDto;
import ru.hse.rankingapp.dto.sponsor.SponsorUpdateDto;
import ru.hse.rankingapp.dto.sponsor.SponsorsInfoDto;
import ru.hse.rankingapp.entity.PartnerEntity;
import ru.hse.rankingapp.entity.SponsorEntity;
import ru.hse.rankingapp.exception.BusinessException;
import ru.hse.rankingapp.repository.AboutUsRepository;
import ru.hse.rankingapp.repository.PartnerRepository;
import ru.hse.rankingapp.repository.SponsorRepository;

@Service
@RequiredArgsConstructor
public class AboutUsService {

    private final AboutUsRepository aboutUsRepository;
    private final PartnerRepository partnerRepository;
    private final SponsorRepository sponsorRepository;
    private final FileService fileService;

    /**
     * Обновить информацию о нас.
     */
    @Transactional
    public void updateAboutUs(String description) {
        aboutUsRepository.updateTextById(1L, description);
    }

    /**
     * Получить текст о нас.
     */
    public AboutUsInfoDto getAboutUsInfo() {
        return aboutUsRepository.findDescriptionById(1L);
    }

    /**
     * Информация о партнерах.
     */
    public PageResponseDto<PartnerInfoDto> getPartners(PageRequestDto pageRequestDto) {
        Page<PartnerInfoDto> page = partnerRepository.findAll(pageRequestDto.toPageRequest())
                .map(entity -> new PartnerInfoDto()
                        .setId(entity.getId())
                        .setPartnerLogo(entity.getPartnerLogo())
                        .setPartnerDescription(entity.getPartnerDescription()));

        return new PageResponseDto<>(page.getTotalElements(), page.getTotalPages(), page.getContent());
    }

    /**
     * Информация о спонсорах.
     */
    public PageResponseDto<SponsorsInfoDto> getSponsors(PageRequestDto pageRequestDto) {
        Page<SponsorsInfoDto> page = sponsorRepository.findAll(pageRequestDto.toPageRequest())
                .map(entity -> new SponsorsInfoDto()
                        .setId(entity.getId())
                        .setSponsorLogo(entity.getSponsorLogo())
                        .setSponsorDescription(entity.getSponsorDescription()));

        return new PageResponseDto<>(page.getTotalElements(), page.getTotalPages(), page.getContent());
    }

    /**
     * Создать партнера.
     */
    @Transactional
    public void addPartner(PartnerCreateDto createDto) {
        PartnerEntity partner = new PartnerEntity();
        partner.setPartnerDescription(createDto.getDescription());

        String logo = fileService.saveFile(createDto.getLogo());
        partner.setPartnerLogo(logo);

        partnerRepository.save(partner);
    }

    /**
     * Создать спонсора.
     */
    @Transactional
    public void addSponsor(SponsorCreateDto createDto) {
        SponsorEntity sponsor = new SponsorEntity();
        sponsor.setSponsorDescription(createDto.getDescription());

        String logo = fileService.saveFile(createDto.getLogo());
        sponsor.setSponsorLogo(logo);

        sponsorRepository.save(sponsor);
    }

    /**
     * Удалить партнера по id.
     */
    @Transactional
    public void deletePartnerById(Long id) {
        partnerRepository.deletePartnerById(id);
    }

    /**
     * Удалить спонсора по id.
     */
    @Transactional
    public void deleteSponsorById(Long id) {
        sponsorRepository.deleteSponsorById(id);
    }

    /**
     * Обновить спонсора.
     */
    @Transactional
    public void updateSponsor(Long id, SponsorUpdateDto updateDto) {
        SponsorEntity sponsor = sponsorRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Не найден спонсор по id = " + id, HttpStatus.NOT_FOUND));

        if (updateDto.getDescription() != null) {
            sponsor.setSponsorDescription(updateDto.getDescription());
        }

        if (Boolean.TRUE.equals(updateDto.getIsNeedUpdateLogo()) && updateDto.getLogo() != null) {
            fileService.deleteFile(sponsor.getSponsorLogo());
            String logo = fileService.saveFile(updateDto.getLogo());
            sponsor.setSponsorLogo(logo);
        }
    }

    /**
     * Обновить партнера.
     */
    @Transactional
    public void updatePartner(Long id, PartnerUpdateDto updateDto) {
        PartnerEntity partner = partnerRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Не найден партнер по id = " + id, HttpStatus.NOT_FOUND));

        if (updateDto.getDescription() != null) {
            partner.setPartnerDescription(updateDto.getDescription());
        }

        if (Boolean.TRUE.equals(updateDto.getIsNeedUpdateLogo()) && updateDto.getLogo() != null) {
            fileService.deleteFile(partner.getPartnerLogo());
            String logo = fileService.saveFile(updateDto.getLogo());
            partner.setPartnerLogo(logo);
        }
    }
}
