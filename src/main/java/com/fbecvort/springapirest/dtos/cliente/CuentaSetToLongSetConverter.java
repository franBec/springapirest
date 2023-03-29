package com.fbecvort.springapirest.dtos.cliente;

import com.fbecvort.springapirest.entities.Cuenta;
import org.dozer.DozerConverter;

import java.util.HashSet;
import java.util.Set;

public class CuentaSetToLongSetConverter extends DozerConverter<Set, Set> {

    public CuentaSetToLongSetConverter() {
        super(Set.class, Set.class);
    }

    @Override
    public Set convertTo(Set source, Set destination) {
        System.out.println("llamando convertTo");

        if (source == null)
            return null;

        Set<Cuenta> cuentas = (Set<Cuenta>) source;
        Set<Long> cuentasId = new HashSet<>();

        for (Cuenta cuenta : cuentas) {
            cuentasId.add(cuenta.getCuentaId());
        }

        return cuentasId;
    }

    @Override
    public Set convertFrom(Set source, Set destination) {
        throw new UnsupportedOperationException("This converter is only intended for one-way mapping.");
    }
}
