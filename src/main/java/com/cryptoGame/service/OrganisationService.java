package com.cryptoGame.service;

import com.cryptoGame.domain.Organisation;
import com.cryptoGame.domain.User;
import com.cryptoGame.exceptions.OrganisationNotFoundException;
import com.cryptoGame.exceptions.UserNotFoundException;
import com.cryptoGame.repository.OrganisationRepository;
import com.cryptoGame.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrganisationService {

    @Autowired
    private OrganisationRepository repository;

    @Autowired
    private UserRepository userRepository;

    public Organisation saveOrganisation(Organisation organisation){
        return repository.save(organisation);
    }

    public Organisation findOrganisation(Long id) throws OrganisationNotFoundException {
        return repository.findById(id).orElseThrow(OrganisationNotFoundException::new);
    }

    public void removeOrganisation(Long id) {
        repository.deleteById(id);
    }

    public List<Organisation> getAllOrganisations(){
        return repository.findAll();
    }

    public void joinToOrganisation(Long userId, Long organisationId) throws UserNotFoundException, OrganisationNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Organisation organisation = findOrganisation(organisationId);
        user.setOrganisation(organisation);
        userRepository.save(user);
        repository.save(organisation);
    }
}
