package br.com.senai.p2m02.devinsales.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class TokenDTO {
    private String token;
    private String tipo;
}
