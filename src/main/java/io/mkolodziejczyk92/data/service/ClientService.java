package io.mkolodziejczyk92.data.service;

import io.mkolodziejczyk92.data.entity.Client;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

    private static ClientRepository repository;

    public ClientService(ClientRepository repository) {
        this.repository = repository;
    }

    public Optional<Client> get(Long id) {
        return repository.findById(id);
    }

    public Client update(Client entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<Client> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Client> list(Pageable pageable, Specification<Client> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

    public static List<Client> allClients(){ return repository.findAll();}

}


