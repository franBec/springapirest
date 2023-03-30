package com.fbecvort.springapirest.exception.crud;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EntidadConElementosAsociadosException extends RuntimeException{

    public EntidadConElementosAsociadosException(){
        super();
    }

    public EntidadConElementosAsociadosException(String entidadClassname, Long entidadId, String elementosClassName ){
        super("No se pudo eliminar el elemento "+entidadClassname+" con id="+entidadId+" debido a que tiene asociado al menos un/a "+elementosClassName);
    }
}
