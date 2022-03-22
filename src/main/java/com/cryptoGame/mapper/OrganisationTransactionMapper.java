package com.cryptoGame.mapper;

import com.cryptoGame.domain.OrganisationTransaction;
import com.cryptoGame.domain.dtos.OrganisationTransactionDto;
import com.cryptoGame.repository.OrganisationRepository;
import com.cryptoGame.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrganisationTransactionMapper {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrganisationRepository organisationRepository;

    public OrganisationTransaction mapToTransaction(OrganisationTransactionDto dto) {
        return new OrganisationTransaction(
                dto.getId(),
                userRepository.findById(dto.getUser_id()).get(),
                organisationRepository.findByGroupName(dto.getOrganisation_name()).get(),
                dto.getTransactionDate(),
                dto.getCryptoSymbol(),
                dto.getCryptoAmount(),
                dto.getMoney(),
                dto.getWorthNow());
    }

    public OrganisationTransactionDto mapToTransactionDto(OrganisationTransaction transaction){
        return new OrganisationTransactionDto(transaction.getId(),
                transaction.getUser().getId(),
                transaction.getOrganisation().getGroupName(),
                transaction.getTransactionDate(),
                transaction.getCryptoSymbol(),
                transaction.getCryptoAmount(),
                transaction.getMoney(),
                transaction.getWorthNow());
    }

    public List<OrganisationTransactionDto> mapToTransactionDtoList(final List<OrganisationTransaction> transactions){
        return transactions.stream().map(this::mapToTransactionDto).collect(Collectors.toList());
    }
}
