package com.portfolio.luisfmdc.sbatch_employee_importer.domain;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Funcionario {

    @NotNull
    @Size(min = 1, max = 150)
    @Pattern(regexp = "^[a-zA-Z\\s]*$", message = "O nome deve conter apenas letras e espaços.")
    private String nome;

    @NotNull
    @Size(min = 5, max = 200)
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "O email deve estar no formato válido.")
    private String email;

    @NotNull
    @Size(min = 1, max = 100)
    private String departamento;

    @NotNull
    private Double salario;

    @NotNull
    private String dataAdmissao;
}