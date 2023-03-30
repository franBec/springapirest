package com.fbecvort.springapirest.service;

import com.fbecvort.springapirest.dto.cliente.ClienteRequestDTO;
import com.fbecvort.springapirest.dto.cliente.ClienteResponseDTO;
import com.fbecvort.springapirest.entity.Cliente;
import com.fbecvort.springapirest.entity.Cuenta;
import com.fbecvort.springapirest.exception.crud.EntityWithAssociatedElementsException;
import com.fbecvort.springapirest.exception.crud.NoSuchElementException;
import com.fbecvort.springapirest.repository.ClienteRepository;
import com.fbecvort.springapirest.util.PaginationUtils;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
public class ClienteServiceImpl implements ClienteService{

    @Autowired
    ClienteRepository clienteRepository;

    @Override
    @Transactional
    public ClienteResponseDTO save(ClienteRequestDTO request) {
        return clienteToResponseDTO(clienteRepository.save(new DozerBeanMapper().map(request, Cliente.class)));
    }

    @Override
    public Page<ClienteResponseDTO> findAll(int page, int size, String sortBy, String sortOrder) {
        Pageable pageable = PaginationUtils.createPageable(page, size, sortBy, sortOrder);

        Page<Cliente> clientes = clienteRepository.findAll(pageable);

        return new PageImpl<>(
                clientes.getContent().stream().map(this::clienteToResponseDTO).collect(Collectors.toList()),
                pageable,
                clientes.getTotalElements()
        );
    }

    @Override
    public ClienteResponseDTO findById(Long id) {
        return clienteToResponseDTO(
                clienteRepository
                        .findById(id)
                        .orElseThrow(()->new NoSuchElementException("Cliente","id", id))
        );
    }

    @Override
    @Transactional
    public ClienteResponseDTO update(Long id, ClienteRequestDTO request) {

        // recupero id de la instancia de Cliente a actualizar
        // esto de paso sirve para arrojar NoSuchElementException si lo que se intenta actualizar no existe
        Long currentClienteId = clienteRepository
                .findById(id)
                .orElseThrow(()->new NoSuchElementException("Cliente","id", id))
                .getPersonaId();

        // creo una nueva instancia de Cliente a partir del request
        Cliente updatedCliente = new DozerBeanMapper().map(request, Cliente.class);

        // le seteo la id, la cual estoy totalmente seguro que es legitima
        updatedCliente.setPersonaId(currentClienteId);

        // udpate and return
        return clienteToResponseDTO(clienteRepository.save(updatedCliente));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Cliente cliente = clienteRepository
                .findById(id)
                .orElseThrow(()-> new NoSuchElementException("Cliente", "id", id));

        if(!cliente.getCuentas().isEmpty()) {
            throw new EntityWithAssociatedElementsException("Cliente", id, "Cuenta");
        }

        clienteRepository.deleteById(id);
    }

    private ClienteResponseDTO clienteToResponseDTO(Cliente cliente) {
        ClienteResponseDTO response = new DozerBeanMapper().map(cliente, ClienteResponseDTO.class);

        response.setCuentasId(
                cliente
                        .getCuentas()
                        .stream()
                        .map(Cuenta::getCuentaId)
                        .collect(Collectors.toSet())
        );

        return response;
    }
}
